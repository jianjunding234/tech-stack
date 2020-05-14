package com.dingjianjun.basetech.algorithm.hash.sample;

import com.dingjianjun.basetech.algorithm.hash.ConsistentHashRouter;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author : Jianjun.Ding
 * @description: test
 * @date 2020/5/14
 */
public class ConsistentHashTest {
    public static void main(String[] args) {
        RealNode pNode1 = new RealNode("HZ1", "127.0.0.1", 8080);
        RealNode pNode2 = new RealNode("HZ1", "127.0.0.1", 8088);
        RealNode pNode3 = new RealNode("HZ2", "127.0.0.1", 8080);
        RealNode pNode4 = new RealNode("HZ2", "127.0.0.1", 8088);
        RealNode pNode5 = new RealNode("HZ2", "127.0.0.1", 6379);
        Set<RealNode> pNodes = Sets.newHashSet(pNode1, pNode2, pNode3, pNode4, pNode5);
        ConsistentHashRouter<RealNode> router = new ConsistentHashRouter(pNodes, 10);
        String ip1 = "192.168.0.1";
        printRoutes(router, ip1);
        System.out.println("----------------------------------");

        router.addNode(new RealNode("HZ3", "127.0.0.1", 2181), 10);
        String ip2 = "192.168.0.2";
        String ip3 = "192.168.0.3";
        String ip4 = "192.168.0.1";
        printRoutes(router, new String[] {ip1, ip2, ip3, ip4});
        System.out.println("----------------------------------");

        router.removeNode(pNode3);
        printRoutes(router, new String[] {ip1, ip2, ip3, ip4});




    }

    private static void printRoutes(ConsistentHashRouter<RealNode> router, String... ips) {
        if (null == ips) {
            return;
        }

        for (String ip : ips) {
            System.out.println("ip: " + ip + " mapped realNode: " + router.route(ip));
        }

    }


}
