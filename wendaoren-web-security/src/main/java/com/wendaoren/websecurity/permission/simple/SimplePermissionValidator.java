package com.wendaoren.websecurity.permission.simple;

import com.wendaoren.core.exception.table.CommonErrorCodeTable;
import com.wendaoren.websecurity.annotation.RequiresPermission;
import com.wendaoren.websecurity.annotation.RequiresRole;
import com.wendaoren.websecurity.exception.PermissionException;
import com.wendaoren.websecurity.exception.SessionException;
import com.wendaoren.websecurity.exception.table.WebSecurityErrorCodeTable;
import com.wendaoren.websecurity.permission.Logic;
import com.wendaoren.websecurity.permission.PermissionValidator;
import com.wendaoren.websecurity.session.Session;
import com.wendaoren.websecurity.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class SimplePermissionValidator implements PermissionValidator {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void verify(HttpServletRequest request, Method method, RequiresRole requiresRole, RequiresPermission requiresPermission) throws PermissionException {
		Session session = SessionContext.get();
		if (session == null) {
			logger.debug("权限验证失败，会话已过期");
			throw new SessionException(CommonErrorCodeTable.SESSION_EXPIRED.toErrorCode());
		}
		if (requiresRole != null && requiresRole.value().length > 0) {
			if (!verify(session.getRoles(), requiresRole.value(), requiresRole.logic())) {
				logger.debug("权限验证|角色权限验证失败[sessionId={}, method={}]", session.getId(), method.getName());
				throw new PermissionException(WebSecurityErrorCodeTable.NO_OPERATION_PERMISSION.toErrorCode());
			}
		}
		if (requiresPermission != null && requiresPermission.value().length > 0) {
			if (!verify(session.getPermissions(), requiresPermission.value(), requiresPermission.logic())) {
				logger.debug("权限验证|权限验证失败[sessionId={}, method={}]", session.getId(), method);
				throw new PermissionException(WebSecurityErrorCodeTable.NO_OPERATION_PERMISSION.toErrorCode());
			}
		}
	}

	private boolean verify(Set<String> ownSet, String[] requires, Logic logic) {
		if (requires == null || requires.length == 0) {
			return true;
		}
		if (Logic.AND.equals(logic)) {
			return Arrays.stream(requires)
				.allMatch((r) -> ownSet.contains(r));
		}
		return Arrays.stream(requires)
				.anyMatch((r) -> ownSet.contains(r));
	}
	
}