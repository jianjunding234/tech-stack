package com.dingjianjun.basetech.dp;

/**
 * @author : Jianjun.Ding
 * @description: 单例（DCL + volatile禁止指令重排序，实现线程安全）
 * @date 2020/4/8
 */
public class Singleton {
    /**
     * volatile 内存可见性；禁止指令重排序
     */
    private static volatile Singleton singleton;

    private Singleton() {}

    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized(Singleton.class) {
               if (singleton == null) {
                   // 如果没有volatile修饰变量，可能存在指令重排序，发布出去的对象非线程安全（属性没有全部初始化，此时对象是半初始化状态）
                   singleton = new Singleton();
               }
            }
        }
        return singleton;
    }
}
