package com.dingjianjun.basetech.jvm;

import java.io.*;
import java.util.Objects;

/**
 * @author : Jianjun.Ding
 * @description: 自定义类加载器
 * @date 2020/4/24
 */
public class CustomClassLoader extends ClassLoader {

    /**
     * 自定义类加载器，重写方法findClass(String name)，遵循双亲委派模型
     * @param name 类的全限定名
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File file = new File("D:\\", name.replace(".", File.separator).concat(".class"));
        if (Objects.isNull(file) || !file.exists()) {
            return super.findClass(name);
        }
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[512];
            int len;
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }

            byte[] bytes = baos.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    /**
     * 自定义类加载器，重写方法loadClass(String name)，打破双亲委派设计
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        File file = new File("D:\\", name.replace(".", File.separator).concat(".class"));
        if (Objects.isNull(file) || !file.exists()) {
            return super.loadClass(name);
        }
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[512];
            int len;
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }

            byte[] bytes = baos.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.loadClass(name);
    }
}
