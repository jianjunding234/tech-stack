package com.dingjianjun.basetech.concurrent;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author : Jianjun.Ding
 * @description: 转账接口
 * @date 2020/4/23
 */
public interface TransferMoneyService {
    /**
     * 一个账户向另一个账户转账
     * @param fromAcc 转账发起方账户
     * @param toAcc   转账接收方账户
     * @param amount  金额
     * @param timeout 转账超时时间
     * @param unit 时间单位
     * @return true:转账成功；false:转账失败
     */
    boolean transferMoney(Account fromAcc,
                          Account toAcc,
                          BigDecimal amount,
                          long timeout,
                          TimeUnit unit) throws InterruptedException;
}
