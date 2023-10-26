package com.phoenixplaydev.musicapp.configuration.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

//@Aspect
@Component
public class RolesChecker {

    public synchronized void userHasAdminRole() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("Admin")))
            throw new AccessDeniedException("Access denied!");
    }

//    @Pointcut("execution(* com.phoenixplaydev.musicapp.service.*.*Resolver.*(..))")
    private void pointcut_ref(){}

//    @Before("pointcut_ref()")
    public void checkRole(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Method graphQLMethod = null;

        for (Method method : targetClass.getMethods()) {
            if (method.getName().equals(methodName)) {
                graphQLMethod = method;
            }
        }

        if (graphQLMethod != null && graphQLMethod.isAnnotationPresent(Secured.class)) {
            Secured annotation = graphQLMethod.getAnnotation(Secured.class);
            String role = annotation.value()[0];
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("role")))
                throw new AccessDeniedException("Access denied!");
        }

    }

}
