package com.jianjunding.rpcconsumer.controller;

import com.dingjianjun.rpcapi.GreetingService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/12
 */
@RequestMapping("/api")
@RestController
public class MainController {
    @Reference(group = "${rpc-provider.service.group}", version = "${rpc-provider.service.version}")
    private GreetingService greetingService;

    @GetMapping("/hi")
    public String getHi(String name) {
         return greetingService.sayHello(name);
    }
}
