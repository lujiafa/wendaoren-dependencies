package com.wendaoren.websecurity.session.validator;

import com.wendaoren.core.constant.ErrorCodeConstant;
import com.wendaoren.core.exception.ErrorCode;
import com.wendaoren.websecurity.annotation.CheckSession;
import com.wendaoren.websecurity.exception.SessionException;
import com.wendaoren.websecurity.session.Session;
import com.wendaoren.websecurity.session.SessionContext;
import com.wendaoren.websecurity.session.SessionValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class SimpleSessionValidator implements SessionValidator {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void verify(HttpServletRequest request, Method method, CheckSession checkSession) throws SessionException {
		String sessionId = SessionContext.getSessionId();
		if (!StringUtils.hasLength(sessionId)) {
			if (logger.isDebugEnabled()) {
				logger.debug("会话已过期，sessionId不存在({})", sessionId);
			}
			throw new SessionException(ErrorCode.build(ErrorCodeConstant.SESSION_EXPIRED, request.getLocale()));
		}
		Session session = SessionContext.get();
		if (session == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("会话已过期({})", sessionId);
			}
			throw new SessionException(ErrorCode.build(ErrorCodeConstant.SESSION_EXPIRED, request.getLocale()));
		}
		SessionContext.delay();
	}
}