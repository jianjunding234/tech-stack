package com.dingjianjun.basetech.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author : Jianjun.Ding
 * @description: 剖析spi(service provider interface)
 * @date 2020/5/2
 */
public class SpiDemo {
    public static void main(String[] args) {
        ServiceLoader<Search> load = ServiceLoader.load(Search.class);
        Iterator<Search> it = load.iterator();
        while (it.hasNext()) {
            // 反射：实例化接口实现类
            Search search = it.next();
            search.search();
        }

    }
}
