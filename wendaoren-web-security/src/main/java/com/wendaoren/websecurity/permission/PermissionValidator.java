package com.wendaoren.websecurity.permission;

import com.wendaoren.core.exception.BusinessException;
import com.wendaoren.websecurity.annotation.RequiresPermission;
import com.wendaoren.websecurity.annotation.RequiresRole;
import com.wendaoren.websecurity.exception.PermissionException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@FunctionalInterface
public interface PermissionValidator {

	/**
	 * 权限验证
	 * 注：参数requiresRole和requiresPermission一定不会同时为null或empty，但允许仅验证Role或Permission
	 * @param request
	 * @param method
	 * @param requiresRole 需要角色
	 * @param requiresPermission 需要权限
	 * @throws BusinessException
	 */
	void verify(HttpServletRequest request, Method method, RequiresRole requiresRole, RequiresPermission requiresPermission) throws PermissionException;

}