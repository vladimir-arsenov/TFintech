package org.example.executiontimeloggerstarter;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.time.Duration;
import java.time.Instant;

@Slf4j
class LogExecutionTimeInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        if(invocation.getMethod().isAnnotationPresent(LogExecutionTime.class) || invocation.getThis() != null &&
                invocation.getThis().getClass().isAnnotationPresent(LogExecutionTime.class)) {
            var start = Instant.now();
            var inv = invocation.proceed();
            log.info("{}.{}() started at {} and finished at {} with duration of {} millis",
                    invocation.getThis().getClass().getCanonicalName(),
                    invocation.getMethod().getName(), start, Instant.now(),
                    Duration.between(start, Instant.now()).toMillis()
            );
            return inv;
        }

        return invocation.proceed();
    }
}