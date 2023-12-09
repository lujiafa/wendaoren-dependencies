package com.wendaoren.websecurity.permission;


import com.wendaoren.utils.common.AnnotationUtils;
import com.wendaoren.web.util.WebUtils;
import com.wendaoren.websecurity.annotation.RequiresPermission;
import com.wendaoren.websecurity.annotation.RequiresRole;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class PermissionValidatorHandler {

    private PermissionValidator permissionValidator;

    public PermissionValidatorHandler(PermissionValidator permissionValidator) {
        this.permissionValidator = permissionValidator;
    }

    @Pointcut("@within(com.wendaoren.websecurity.annotation.RequiresPermission) || @annotation(com.wendaoren.websecurity.annotation.RequiresPermission)")
    public void permissionPointcut() {}

    @Pointcut("@within(com.wendaoren.websecurity.annotation.RequiresRole) || @annotation(com.wendaoren.websecurity.annotation.RequiresRole)")
    public void rolePointcut() {}

    @Pointcut("permissionPointcut() || rolePointcut()")
    public void combinedPointcut() {}

    @Around(value = "combinedPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RequiresRole requiresRole = AnnotationUtils.getAnnotationByPriorityMethod(method, RequiresRole.class);
        RequiresPermission requiresPermission = AnnotationUtils.getAnnotationByPriorityMethod(method, RequiresPermission.class);
        if ((requiresRole == null || requiresRole.value().length == 0)
                && (requiresPermission == null || requiresPermission.value().length == 0)) {
            return pjp.proceed();
        }
        permissionValidator.verify(WebUtils.getRequest(), method, requiresRole, requiresPermission);
        return pjp.proceed();
    }

}
