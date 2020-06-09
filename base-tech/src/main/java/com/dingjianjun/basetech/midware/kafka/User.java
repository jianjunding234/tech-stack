package com.dingjianjun.basetech.midware.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {
    private int id;
    private String name;
    private Date birth;
}
