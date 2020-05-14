package com.dingjianjun.basetech.jvm;

import sun.misc.Launcher;

/**
 * @author : Jianjun.Ding
 * @description: 类加载器剖析
 * @date 2020/4/23
 */
public class ClassLoaderDemo {
    public static void main(String[] args) {
//        /**
//         * Launcher 启动类（构造方法初始化ExtClassLoader实例、AppClassLoader实例、
//         * 设置当前线程的contextClassLoader为AppClassLoader实例）
//         *
//         * BootStrap类加载器（jvm c++ 实现，java没有对应的类）
//         * ExtClassLoader
//         * AppClassLoader
//         * 自定义类加载器
//         */
//        try {
//            System.out.println(ClassLoaderDemo.class.getClassLoader());
//            System.out.println(ClassLoaderDemo.class.getClassLoader().getParent());
//            System.out.println(ClassLoaderDemo.class.getClassLoader().getClass().getClassLoader());
//            Class<?> clazz = ClassLoaderDemo.class.getClassLoader().loadClass("com.dingjianjun.basetech.jvm.ClassLoaderDemo");
//            System.out.println(clazz.getName());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        ClassLoader classLoader = new CustomClassLoader();
        try {
            Class<?> clazz = classLoader.loadClass("com.dingjianjun.basetech.jvm.Hello");
            System.out.println(clazz.getClassLoader());
            System.out.println(clazz.getClassLoader().getParent());
            try {
                Object obj = clazz.newInstance();
                System.out.println(obj.getClass());
                if (obj instanceof com.dingjianjun.basetech.jvm.Hello) {
                    System.out.println("obj instanceof com.dingjianjun.basetech.jvm.Hello?" + true);
                } else {
                    System.out.println("obj instanceof com.dingjianjun.basetech.jvm.Hello?" + false);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

//            System.out.println(ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
