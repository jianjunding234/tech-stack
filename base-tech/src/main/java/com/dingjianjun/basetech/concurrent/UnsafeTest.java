package com.dingjianjun.basetech.concurrent;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author : Jianjun.Ding
 * @description: 利用Unsafe对象直接读写内存
 * @date 2020/4/9
 */
public class UnsafeTest {
    private static UnsafeTest t = new UnsafeTest();
    private int i = 0;

    public static void main(String[] args) {
        // 通过反射机制获取Unsafe类的Class对象拥有所有已声明的Field
        Field declaredField = Unsafe.class.getDeclaredFields()[0];
        // 设置Field对象可访问
        declaredField.setAccessible(true);
        try {
            Unsafe unsafe = (Unsafe) declaredField.get(null);
            try {
                // 相对对象占用内存的起始地址的偏移
                long valueOffset = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("i"));
                System.out.println("UnsafeTest filed i offset:" + valueOffset);
                boolean result = unsafe.compareAndSwapInt(t, valueOffset, 0, 1);
                System.out.println(result);
                System.out.println(t.i);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
