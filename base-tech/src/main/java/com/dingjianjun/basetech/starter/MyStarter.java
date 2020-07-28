//package com.dingjianjun.basetech.starter;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author : Jianjun.Ding
// * @description:
// * @date 2020/6/15
// */
//@Configuration
//@ConditionalOnProperty(value = "enabled.autoConfiguration", matchIfMissing = true)
//public class MyStarter {
//    static {
//        System.out.println("MyStarter 正在进行类加载....");
//    }
//
//    @Bean
//    public Template template() {
//        return new Template();
//    }
//
//}
