package com.tchain.websecurity.session.validator;

import com.tchain.core.exception.table.CommonErrorCodeTable;
import com.tchain.websecurity.annotation.CheckSession;
import com.tchain.websecurity.exception.SessionException;
import com.tchain.websecurity.session.Session;
import com.tchain.websecurity.session.SessionContext;
import com.tchain.websecurity.session.SessionValidator;
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