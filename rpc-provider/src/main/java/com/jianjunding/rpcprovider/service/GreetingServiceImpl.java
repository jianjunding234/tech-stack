package com.jianjunding.rpcprovider.service;

import com.dingjianjun.rpcapi.GreetingService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/12
 */
@Service(version = "${rpc-provider.service.version}", group = "${rpc-provider.service.group}")
public class GreetingServiceImpl implements GreetingService {
    /**
     * The default value of ${dubbo.application.name} is ${spring.application.name}
     */
    @Value("${dubbo.application.name}")
    private String serviceName;

    @Override
    public String sayHello(String name) {
        return String.format("[%s] : Hello, %s", serviceName, name);
    }
}
