package com.ems.employee_service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(com.ems.employee_service..*)")
    public void applicationPackagePointcut() {

    }

    // Advice that logs methods before they are executed
    @Before("applicationPackagePointcut()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Executing: {}", joinPoint);
    }

    // Advice that logs methods after they are executed
    @After("applicationPackagePointcut()")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("Completed: {}", joinPoint);
    }
}
