package com.dingjianjun.basetech.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

/**
 * @author : Jianjun.Ding
 * @description: 线程池剖析 ThreadPoolExecutor、ForkJoinPool
 * @date 2020/4/21
 */
@Slf4j
public class ThreadPoolDemo {
    private static int[] datas = new int[10];
    private static final Random r = new Random();
    private static final int MAX_NUM_OF_PER_TASK = 5;
    static {
        IntStream.range(0, datas.length).forEach(i -> {
            datas[i] = r.nextInt(100);
        });
    }
    public static void main(String[] args) {
//        testForJoinPool();
        testScheduledExecutorService();
    }

    public static void useForJoinPool() {
        // 用于可以分解汇总的任务
        ForkJoinPool fjp = new ForkJoinPool();
        AddResultTask addTask = new AddResultTask(0, datas.length);
        fjp.execute(addTask);
        Long result = addTask.join();
        System.out.println("result:" + result);


    }

    public static void useThreadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4,
                0L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(4),
                new MyThreadFactory("demo"),
                new ThreadPoolExecutor.AbortPolicy());

        IntStream.range(0, 9).forEach(i -> {
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public static void useWorkStealingPool() {
       // log.info("ncpus:" + Runtime.getRuntime().availableProcessors());
        int len = datas.length;
        ExecutorService executorService = Executors.newWorkStealingPool();
        Future<Integer> future = executorService.submit(() -> {
            int count = 0;
            for (int i = 0; i < len / 4; i++) {
                if (isPrime(datas[i])) {
                    count++;
                }
            }
            log.info("future count:" + count);
            return count;
        });

        Future<Integer> future2 = executorService.submit(() -> {
            int count = 0;
            for (int i = len / 4; i < len / 2; i++) {
                if (isPrime(datas[i])) {
                    count++;
                }
            }
            log.info("future2 count:" + count);
            return count;
        });

        Future<Integer> future3 = executorService.submit(() -> {
            int count = 0;
            for (int i = len / 2; i < (len / 4) * 3; i++) {
                if (isPrime(datas[i])) {
                    count++;
                }
            }
            log.info("future3 count:" + count);
            return count;
        });

        Future<Integer> future4 = executorService.submit(() -> {
            int count = 0;
            for (int i = (len / 4) * 3; i < len; i++) {
                if (isPrime(datas[i])) {
                    count++;
                }
            }
            log.info("future4 count:" + count);
            return count;
        });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public static class MyRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // log 记录被拒绝任务的信息
            // save kafka、redis、mysql
            // try n 次 检查队列是否满，若未满，则当前任务添加到队列

            log.info("task {} rejected from {}", r.toString(), executor.toString());

        }
    }

    private static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }

        for (int i = 2; i < num; i++) {
            if (i % 2 == 0) {
                continue;
            }

            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }

    private static class AddTask extends RecursiveAction {
        /**
         * 起始位置下标
         */
        private int fromIndex;
        /**
         * 结束位置下标（不包括）
         */
        private int toIndex;

        public AddTask(int startIndex, int endIndex) {
            this.fromIndex = startIndex;
            this.toIndex = endIndex;
        }

        @Override
        protected void compute() {
            int sum = 0;
            if (toIndex - fromIndex <= MAX_NUM_OF_PER_TASK) {
                for (int i = fromIndex; i < toIndex; i++) {
                   sum += datas[i];
                   log.info("data[{}]={}, sum={}", i , datas[i], sum);
                }
                log.info("sum:{}", sum);
            } else {
                int midIndex = fromIndex + (toIndex - fromIndex) >> 1;
                log.info("mid:{}", midIndex);
                AddTask addTask1 = new AddTask(fromIndex, midIndex);
                AddTask addTask2 = new AddTask(midIndex + 1, toIndex);
                // 分解出子任务
                addTask1.fork();
                // 分解出子任务
                addTask2.fork();
            }

        }
    }

    private static class AddResultTask extends RecursiveTask<Long> {
        /**
         * 起始位置下标
         */
        private int fromIndex;
        /**
         * 结束位置下标（不包括）
         */
        private int toIndex;

        public AddResultTask(int startIndex, int endIndex) {
            this.fromIndex = startIndex;
            this.toIndex = endIndex;
        }

        @Override
        protected Long compute() {
            if (toIndex - fromIndex <= MAX_NUM_OF_PER_TASK) {
                long sum = 0;
                for (int i = fromIndex; i < toIndex; i++) {
                    sum += datas[i];
                }
                return sum;
            }

            int midIndex = fromIndex + (toIndex - fromIndex) >> 1;
            AddResultTask addTask1 = new AddResultTask(fromIndex, midIndex);
            AddResultTask addTask2 = new AddResultTask(midIndex + 1, toIndex);
            // 分解出子任务
            addTask1.fork();
            // 分解出子任务
            addTask2.fork();
            // 子任务汇总结果
            Long result = addTask1.join() + addTask2.join();
            return result;
        }
    }

    public static void testScheduledExecutorService() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2,
                new MyThreadFactory("scheduled-demo"), new MyRejectedExecutionHandler());
        long now = System.currentTimeMillis();
//        executorService.schedule(() -> {
//            long curTime = System.currentTimeMillis();
//
//            log.info("{} ms", curTime - now);
//        }, 3, TimeUnit.SECONDS);

//        ScheduledFuture<Long> future = executorService.schedule(() -> {
//            long sum = IntStream.range(0, 50).parallel().sum();
//            log.info("sum:{}", sum);
//            return sum;
//        }, 1, TimeUnit.SECONDS);
//
//        try {
//            log.info("delay:{}, result:{}", future.getDelay(TimeUnit.SECONDS), future.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

//         executorService.scheduleAtFixedRate(() -> {
//             long curTime = System.currentTimeMillis();
//             log.info("{} ms", curTime - now);
//         }, 5, 2, TimeUnit.SECONDS);

        executorService.scheduleWithFixedDelay(() -> {
            long curTime = System.currentTimeMillis();
            log.info("{} ms", curTime - now);
        }, 3, 2, TimeUnit.SECONDS);


    }


}
