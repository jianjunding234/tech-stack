package com.dingjianjun.basetech.starter;

import lombok.Data;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/15
 */
@Data
public class Template {
    private String name;

    public Template() {}

    public Template(String name) {
        this.name = name;
    }

}
