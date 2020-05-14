package com.dingjianjun.basetech.reflect;

import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.*;

/**
 * @author : Jianjun.Ding
 * @description: 反射剖析
 * @date 2020/5/2
 */
@Slf4j
public class ReflectDemo {
    public static void main(String[] args) {
        // 数组的反射
        int[] arr = (int[]) Array.newInstance(int.class, 3);
        Array.set(arr, 0, 100);
        log.info("{}", Array.get(arr, 0));
        try {
            // 数组元素类型
            Class clazz = getClass("long");
            // 数组类型
            Class<?> aClass = Array.newInstance(clazz, 0).getClass();
            log.info("{}|{}", aClass, aClass.isArray());
            // 获取数组的元素类型
            Class<?> componentType = aClass.getComponentType();
            log.info("{}", componentType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    public static Class getClass(String className) throws ClassNotFoundException {
        if ("int".equals(className)) {
            return int.class;
        } else if ("long".equals(className)) {
            return long.class;
        } else if ("short".equals(className)) {
            return short.class;
        } else if ("byte".equals(className)) {
            return byte.class;
        } else if ("boolean".equals(className)) {
            return boolean.class;
        } else if ("char".equals(className)) {
            return char.class;
        } else if ("float".equals(className)) {
            return float.class;
        } else if ("double".equals(className)) {
            return double.class;
        }

        return Class.forName(className);
    }
}
