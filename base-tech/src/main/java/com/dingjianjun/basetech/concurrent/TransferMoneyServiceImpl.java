package com.dingjianjun.basetech.concurrent;

import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author : Jianjun.Ding
 * @description: 转账接口
 * @date 2020/4/23
 */
@Slf4j
public class TransferMoneyServiceImpl implements TransferMoneyService {
    @Override
    public boolean transferMoney(Account fromAcc,
                                 Account toAcc,
                                 BigDecimal amount,
                                 long timeout,
                                 TimeUnit unit) throws InterruptedException {
        // 超时时间戳（纳秒）
        long nanosTimeout = System.nanoTime() + unit.toNanos(timeout);
        while (true) {
            // tryLock
            if (fromAcc.getLock().tryLock()) {
                try {
                    if (toAcc.getLock().tryLock()) {
                        try {
                            // 判断发起方账户的余额是否足够
                            if (fromAcc.getBalance().compareTo(amount) < 0) {
                                log.info("{} transfer money amount {} not enough");
                                // 抛业务异常
                                return false;
                            } else {
                                fromAcc.setBalance(fromAcc.getBalance().subtract(amount));
                                toAcc.setBalance(toAcc.getBalance().add(amount));
                                return true;
                            }
                        } finally {
                            toAcc.getLock().unlock();
                        }
                    }
                } finally {
                    fromAcc.getLock().unlock();
                }

            }

            // 已经超时，则返回转账失败
            if (System.nanoTime() >= nanosTimeout) {
                return false;
            }

            // 休眠一段时间 （固定时间和随机时间组成）
            TimeUnit.NANOSECONDS.sleep(getRandomTimeOut(timeout, unit, 5));
        }
    }

    private long getRandomTimeOut(long timeout, TimeUnit unit, int n) {
        long nanosTime = unit.toNanos(timeout);
        long baseTime = nanosTime / n;
        Random r = new Random();
        long randomTime = r.nextLong() % baseTime;
        return baseTime + randomTime;
    }

    public static void main(String[] args) {
        TransferMoneyService service = new TransferMoneyServiceImpl();
        Account acc = new Account(1L, "ds");
        acc.setBalance(new BigDecimal(20000.0d));
        Account acc2 = new Account(2L, "xk");
        acc2.setBalance(new BigDecimal(500.0d));
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 60L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
                new MyThreadFactory("机房1"),
                new ThreadPoolDemo.MyRejectedExecutionHandler());
        Future<Boolean> f1 = executor.submit(() ->
             service.transferMoney(acc, acc2, new BigDecimal(100.0d), 5L, TimeUnit.SECONDS)
        );

        Future<Boolean> f2 = executor.submit(() ->
             service.transferMoney(acc2, acc, new BigDecimal(200.0d), 5L, TimeUnit.SECONDS)
        );

        try {
            f1.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            f2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        log.info("acc accId:{} balance:{}, acc2 accId:{} balance:{}", acc.getAccountId(), acc.getBalance(),
                acc2.getAccountId(), acc2.getBalance());


    }
}
