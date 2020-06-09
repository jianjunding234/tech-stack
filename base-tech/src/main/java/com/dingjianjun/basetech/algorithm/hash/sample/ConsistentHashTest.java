package com.dingjianjun.basetech.algorithm.hash.sample;

import com.dingjianjun.basetech.algorithm.hash.ConsistentHashRouter;
import com.dingjianjun.basetech.algorithm.hash.Node;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author : Jianjun.Ding
 * @description: test
 * @date 2020/5/14
 */
public class ConsistentHashTest {
    public static void main(String[] args) {
        RealNode<String,String> pNode1 = new RealNode("HZ1", "127.0.0.1", 8080);
        RealNode<String,String> pNode2 = new RealNode("HZ1", "127.0.0.1", 8088);
        RealNode<String,String> pNode3 = new RealNode("HZ2", "127.0.0.1", 8080);
        RealNode<String,String> pNode4 = new RealNode("HZ2", "127.0.0.1", 8088);
        RealNode<String,String> pNode5 = new RealNode("HZ2", "127.0.0.1", 6379);
        Set<RealNode<String,String>> pNodes = Sets.newHashSet(pNode1, pNode2, pNode3, pNode4, pNode5);
        ConsistentHashRouter<String,String,RealNode<String,String>> router = new ConsistentHashRouter(pNodes, 10);
        String key1 = "192.168.0.1";
        String value1 = "xxx1";
        String key2 = "192.168.0.2";
        String value2 = "xxx2";
        String key3 = "192.168.0.3";
        String value3 = "xxx3";
        String key4 = "192.168.0.4";
        String value4 = "xxx4";
        router.set(key1, value1);
        router.set(key2, value2);
        router.set(key3, value3);
        router.set(key4, value4);
        printRoutes(router, new String[] {key1, key2, key3, key4});
        System.out.println("----------------------------------");

        router.addNode(new RealNode("HZ3", "127.0.0.1", 2181), 10);
        printRoutes(router, new String[] {key1, key2, key3, key4});
        System.out.println("----------------------------------");

        router.removeNode(pNode1);
        printRoutes(router, new String[] {key1, key2, key3, key4});
    }

    private static void printRoutes(ConsistentHashRouter<String,String,RealNode<String,String>> router, String... keys) {
        if (null == keys) {
            return;
        }

        for (String key : keys) {
            Node<String,String> destNode = router.route(key);
            System.out.println("key: " + key + " mapped realNode: " + destNode + " {key:" + key + ",value:" + router.get(key) + "}");
        }

    }


}
