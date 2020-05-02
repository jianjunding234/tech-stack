package com.dingjianjun.basetech.jvm;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author : Jianjun.Ding
 * @description: java对象内存布局
 * @date 2020/4/27
 */
@Slf4j
public class ObjectLayoutDemo {
    public static void main(String[] args) {
        Test1 test = new Test1();
        ClassLayout classLayout = ClassLayout.parseInstance(test);
        log.info("对象布局 ------{}", classLayout.toPrintable());
        log.info("对象hashcode-----{}", classLayout.hashCode());
        log.info("对象头占用内存字节数-----{}", classLayout.headerSize());
        log.info("对象占用内存字节数-----{}", classLayout.instanceSize());
        log.info("对象fields--------{}", classLayout.fields());

        
    }
}
