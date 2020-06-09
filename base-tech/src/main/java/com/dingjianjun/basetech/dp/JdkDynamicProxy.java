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
    // 需要代理的对象
    private Object targetObject;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if ("execute".equals(methodName)) {
            checkPermission();

        }
        // 反射进行委托类对象的方法调用
        Object ret = method.invoke(targetObject, args);
        return ret;
    }

    /**
     *  使用JDK动态代理前提：委托类一定要实现接口
     *  动态创建代理类，代理类继承Proxy类， 创建代理对象
     * @param targetObject 需要代理的对象
     * @return 代理对象
     */
    public Object createProxy(Object targetObject) {
        this.targetObject = targetObject;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(), this);
    }

    private boolean checkPermission() {
        System.out.println("权限校验通过......");
        return true;
    }

    public static void main(String[] args) {
        JobService jobService = new AudoCreateTaskJobService();
        JdkDynamicProxy jdkDynamicProxy = new JdkDynamicProxy();
        JobService proxy = (JobService) jdkDynamicProxy.createProxy(jobService);
        proxy.execute();
    }
}
