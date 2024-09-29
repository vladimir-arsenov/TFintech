package org.example.executiontimeloggerstarter;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class LogExecutionTimePostProcessor implements BeanPostProcessor {

    private final Map<String, Class<?>> map = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        if (beanClass.isAnnotationPresent(LogExecutionTime.class) ||
                !map.containsKey(beanName) &&
                Arrays.stream(beanClass.getMethods())
                        .anyMatch(method -> method.isAnnotationPresent(LogExecutionTime.class))
        ) {
            map.put(beanName, beanClass);
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (map.containsKey(beanName)) {
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            proxyFactory.addAdvice(new LogExecutionTimeInterceptor());
            return proxyFactory.getProxy();
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}


