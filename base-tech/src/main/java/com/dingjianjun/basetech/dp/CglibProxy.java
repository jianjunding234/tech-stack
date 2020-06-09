package com.dingjianjun.basetech.dp;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author : Jianjun.Ding
 * @description: cglib使用字节码技术动态代理，主要是对指定的类生成一个子类，覆盖其中的方法，
 * 使用继承，前提条件：该类或方法不要声明成final，对于final类或方法，是无法继承的。
 * @date 2020/5/28
 */
public class CglibProxy implements MethodInterceptor {
    // cglib 需要代理的对象
    private Object target;

    /**
     * 生成目标类（该类或方法不要声明成final）的子类作为代理类，创建代理对象
     * @param target 需要代理的对象
     * @return 代理对象
     */
    public Object createProxy(Object target) {
        this.target = target;
        // 增强类
        Enhancer enhancer = new Enhancer();
        // 设置代理类的父类
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if ("execute".equals(method.getName())) {
            checkPermission();
        }

        Object retObj = method.invoke(target, objects);
        return retObj;
    }

    private boolean checkPermission() {
        System.out.println("权限校验通过......");
        return true;
    }

    public static void main(String[] args) {
        AudoCreateTaskJobService jobService = new AudoCreateTaskJobService();
        CglibProxy cglibProxy = new CglibProxy();
        AudoCreateTaskJobService proxy = (AudoCreateTaskJobService)cglibProxy.createProxy(jobService);
        proxy.execute();
    }

}
