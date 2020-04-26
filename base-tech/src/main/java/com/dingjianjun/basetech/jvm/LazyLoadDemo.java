package com.dingjianjun.basetech.jvm;

/**
 * @author : Jianjun.Ding
 * @description: 懒加载类
 * @date 2020/4/24
 */
public class LazyLoadDemo {
    public static void main(String[] args) {
        P p;
        System.out.println(P.i);
//        System.out.println(P.j);
        // 加载子类前先加载父类
        System.out.println(S.k);
    }


    private static class P {
        // final 可以不加载类
        static final int i = 8;
        static int j = 9;
        static {
            System.out.println("P");
        }
    }

    private static class S extends P {
        static int k = 10;
    }
}
