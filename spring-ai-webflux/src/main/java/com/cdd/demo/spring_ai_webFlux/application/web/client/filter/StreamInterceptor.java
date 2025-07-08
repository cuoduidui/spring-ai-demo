package com.cdd.demo.spring_ai_webFlux.application.web.client.filter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import reactor.core.publisher.Flux;

public class StreamInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 前置处理：记录请求日志
        System.out.println("Stream request intercepted: " + invocation.getMethod().getName());

        // 执行原始流式调用
        Flux<?> originalFlux = (Flux<?>) invocation.proceed();

        // 后置处理：添加监控和错误处理
        return originalFlux
                .doOnNext(item ->
                        System.out.println("Stream item emitted: " + item)
                )
                .doOnError(e ->
                        System.err.println("Stream error: " + e.getMessage())
                );
    }
}
