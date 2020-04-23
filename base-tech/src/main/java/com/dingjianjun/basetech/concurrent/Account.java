package com.dingjianjun.basetech.concurrent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : Jianjun.Ding
 * @description: 账户
 * @date 2020/4/23
 */
public class Account implements Serializable {
    private static final long serialVersionUID = 8443439738228856318L;
    private final Long accountId;
    private final String accountName;
    private BigDecimal balance;
    private final ReentrantLock lock = new ReentrantLock();

    public Account(Long accountId, String accountName) {
        this.accountId = accountId;
        this.accountName = accountName;
        balance = new BigDecimal(0.0d);
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", balance=" + balance +
                '}';
    }
}
