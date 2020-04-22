package com.dingjianjun.basetech.dp;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author : Jianjun.Ding
 * @description: JDK 动态代理剖析
 * @date 2020/4/21
 */
@Slf4j
public class JdkDynamicProxy implements InvocationHandler {
    // 委托类对象
    private Object realObject;

    public JdkDynamicProxy(Object realObject) {
        this.realObject = realObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        log.info("{} invoke {} before", proxy, methodName);
        // 反射进行委托类对象的方法调用
        Object ret = method.invoke(realObject, args);
        log.info("{} invoke {} after", proxy, methodName);
        return ret;
    }

    public static void main(String[] args) {
        JobService jobService = new AudoCreateTaskJobService();
        // 创建代理对象
        JobService proxy = (JobService)Proxy.newProxyInstance(JobService.class.getClassLoader(),
                AudoCreateTaskJobService.class.getInterfaces(), new JdkDynamicProxy(jobService));
        proxy.execute();
    }
}
