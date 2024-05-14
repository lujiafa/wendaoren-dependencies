package com.tchain.websecurity.handler;

import com.tchain.core.exception.BusinessException;
import com.tchain.core.exception.table.CommonErrorCodeTable;
import com.tchain.utils.common.AnnotationUtils;
import com.tchain.utils.common.MapUtils;
import com.tchain.utils.constant.CommonConstant;
import com.tchain.utils.web.WebUtils;
import com.tchain.web.view.SmartErrorView;
import com.tchain.websecurity.annotation.*;
import com.tchain.websecurity.constant.SecurityConstant;
import com.tchain.websecurity.exception.SessionException;
import com.tchain.websecurity.exception.SignatureException;
import com.tchain.websecurity.permission.PermissionValidator;
import com.tchain.websecurity.prop.SecurityProperties;
import com.tchain.websecurity.session.SessionContext;
import com.tchain.websecurity.session.SessionValidator;
import com.tchain.websecurity.sign.SignatureValidator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WebSecurityHandlerInterceptor implements HandlerInterceptor, Filter  {

    private SecurityProperties securityProperties;
    private SessionValidator sessionValidator;
    private PermissionValidator permissionValidator;
    private SignatureValidator signatureValidator;
    private RedisTemplate redisTemplate;

    public WebSecurityHandlerInterceptor(SecurityProperties securityProperties,
                                         SessionValidator sessionValidator,
                                         PermissionValidator permissionValidator,
                                         SignatureValidator signatureValidator,
                                         RedisTemplate redisTemplate) {
        this.securityProperties = securityProperties;
        this.sessionValidator = sessionValidator;
        this.permissionValidator = permissionValidator;
        this.signatureValidator = signatureValidator;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(request, response);
        SessionContext.releaseSession();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            try {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                if (securityProperties.isCheckSession()) {
                    checkSession(request, response, method);
                }
                Map<String, String> parameterMap = null;
                if (securityProperties.isCheckSign()) {
                    WebUtils.getRequestAllParameters(request);
                    parameterMap = MapUtils.toStringMap(WebUtils.getRequestAllParameters(request));
                    checkSign(request, response, method, parameterMap);
                }
                if (securityProperties.isCheckRepeat()) {
                    if (parameterMap == null) {
                        parameterMap = MapUtils.toStringMap(WebUtils.getRequestAllParameters(request));
                    }
                    checkRepeatRequest(request, response, method, parameterMap);
                }
            } catch (SessionException e) {
            } catch (SignatureException e) {
                throw new ModelAndViewDefiningException(new ModelAndView(new SmartErrorView(e.getErrorCode(), WebUtils.getResponseMediaType(request))));
            }
        }
        return true;
    }

    /**
     * 处理检查会话信息
     * @param request
     * @param response
     * @param method
     */
    protected void checkSession(HttpServletRequest request, HttpServletResponse response, Method method) {
        CheckSession checkSession = AnnotationUtils.getAnnotationByPriorityMethod(method, CheckSession.class);
        if (checkSession == null || !checkSession.value()) {
            return;
        }
        try {
            sessionValidator.verify(request, method, checkSession);
            request.setAttribute(SecurityConstant.SESSION_VALIDATOR_HANDLED_ATTR_NAME, true);
        } catch (SessionException e) {
            if (CommonErrorCodeTable.SESSION_EXPIRED.getCode() == (e.getErrorCode().getCode())
                    || CommonErrorCodeTable.SESSION_KICK_OUT_EXPIRED.getCode() == (e.getErrorCode().getCode())) {
                MediaType mediaType = WebUtils.getResponseMediaType(request);
                if (StringUtils.hasLength(securityProperties.getSession().getLoginUrl())
                        && (MediaType.TEXT_HTML.includes(mediaType)
                        || MediaType.APPLICATION_XHTML_XML.includes(mediaType))) {
                    try {
                        PrintWriter pw = response.getWriter();
                        pw.write("<html><script type=\"text/javascript\">top.location.href=" + securityProperties.getSession().getLoginUrl().trim() + "</script></html>");
                        pw.flush();
                        pw.close();
                    } catch (IOException e1) {}
                }
            }
        }
    }

    protected void checkSign(HttpServletRequest request, HttpServletResponse response, Method method, Map<String, String> parameterMap) {
        CheckSign checkSign = AnnotationUtils.getAnnotationByPriorityMethod(method, CheckSign.class);
        if (checkSign == null || !checkSign.value()) {
            return;
        }
        signatureValidator.verify(request, method, checkSign, parameterMap);
    }

    protected void checkRepeatRequest(HttpServletRequest request, HttpServletResponse response, Method method, Map<String, String> parameterMap) {
        CheckRepeatRequest checkRepeatRequest = AnnotationUtils.getAnnotationByPriorityMethod(method, CheckRepeatRequest.class);
        if (checkRepeatRequest == null) {
            return;
        }
        String requestId = null;
        if (securityProperties.isEnableHeader()) {
            requestId = request.getHeader(SecurityConstant.PARAM_REQUEST_ID_NAME);
        } else {
            requestId = parameterMap.get(SecurityConstant.PARAM_REQUEST_ID_NAME);
        }
        if (!StringUtils.hasLength(requestId)) {
            throw new SignatureException(CommonErrorCodeTable.PARAMS_EMPTY_P.toErrorCode(SecurityConstant.PARAM_REQUEST_ID_NAME));
        }
        // 防重放验证
        String cacheKey = String.format("common:repeat-request:%s", requestId);
        if (redisTemplate.boundValueOps(cacheKey).setIfAbsent(CommonConstant.EMPTY, 900, TimeUnit.SECONDS)) {
           throw new BusinessException(CommonErrorCodeTable.REQUEST_REPEAT.toErrorCode());
        }
    }
}
