package com.projects.socialapp.aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.projects.socialapp.service.*.*(..))")
    public Object logExecutionDetails(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get the method name
        String methodName = joinPoint.getSignature().getName();

        // Get the method parameters
        Object[] methodArgs = joinPoint.getArgs();

        // Start time
        long startTime = System.currentTimeMillis();

        // Proceed with method execution
        Object result = joinPoint.proceed();

        // Calculate execution time
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Log details
        System.out.println("Method: " + methodName);
        System.out.println("Parameters: " + Arrays.toString(methodArgs));
        System.out.println("Execution time: " + executionTime + " ms");

        return result;
    }
}
