package com.dingjianjun.basetech.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

/**
 * @author : Jianjun.Ding
 * @description: 反射剖析
 * @date 2020/5/2
 */
@Slf4j
public class ReflectDemo {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        UserController userController = new UserController();
//        Class<?> clazz = userController.getClass();
//        Stream.of(clazz.getDeclaredFields()).forEach(field -> {
//            Autowired annotation = field.getAnnotation(Autowired.class);
//            if (null != annotation) {
//                field.setAccessible(true);
//                Class<?> type = field.getType();
//                try {
//                    Object o = type.getConstructor().newInstance();
//                    field.set(userController, o);
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println(userController.getUserService());
//            }
//        });


//        UserService userService = new UserService();
//        Field field = clazz.getDeclaredField("userService");
////        // 修改字段可访问
//        field.setAccessible(true);
////        // 通过反射修改字段值
////        field.set(userController, userService);
//
//        String fieldName = field.getName();
//        // 拼接set方法
//        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
//        Method method = clazz.getMethod(methodName, UserService.class);
//        // 反射调用方法
//        method.invoke(userController, userService);






//
//        // 数组的反射
//        int[] arr = (int[]) Array.newInstance(int.class, 3);
//        Array.set(arr, 0, 100);
//        log.info("{}", Array.get(arr, 0));
//        try {
//            // 数组元素类型
//            Class clazz = getClass("long");
//            // 数组类型
//            Class<?> aClass = Array.newInstance(clazz, 0).getClass();
//            log.info("{}|{}", aClass, aClass.isArray());
//            // 获取数组的元素类型
//            Class<?> componentType = aClass.getComponentType();
//            log.info("{}", componentType);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }


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
