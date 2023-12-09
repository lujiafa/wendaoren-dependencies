package com.wendaoren.websecurity.session.validator;

import com.wendaoren.core.exception.table.CommonErrorCodeTable;
import com.wendaoren.websecurity.annotation.CheckSession;
import com.wendaoren.websecurity.constant.SecurityConstant;
import com.wendaoren.websecurity.exception.SessionException;
import com.wendaoren.websecurity.session.Session;
import com.wendaoren.websecurity.session.SessionContext;
import com.wendaoren.websecurity.session.SessionValidator;
import com.wendaoren.websecurity.sign.SignKeyGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class SimpleSessionValidator implements SessionValidator {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void verify(HttpServletRequest request, Method method, CheckSession checkSession) throws SessionException {
		String sessionId = SessionContext.getSessionId();
		if (!StringUtils.hasLength(sessionId)) {
			if (logger.isDebugEnabled()) {
				logger.debug("会话已过期，sessionId不存在({})", sessionId);
			}
			throw new SessionException(CommonErrorCodeTable.SESSION_EXPIRED.toErrorCode());
		}
		Session session = SessionContext.get();
		if (session == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("会话已过期({})", sessionId);
			}
			throw new SessionException(CommonErrorCodeTable.SESSION_EXPIRED.toErrorCode());
		}
		SessionContext.delay();
	}
}