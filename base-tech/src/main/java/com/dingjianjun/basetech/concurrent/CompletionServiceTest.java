package com.dingjianjun.basetech.concurrent;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author : Jianjun.Ding
 * @description: CompletionService结果的输出和线程的放入顺序无关(谁完成了谁就先输出！
 * 主线程总是能够拿到最先完成的任务的结果)，缩短等待时间
 * @date 2020/7/21
 */
public class CompletionServiceTest {
    public static void main(String[] args) {
        int threadNum = 4;
        ExecutorService executor = new ThreadPoolExecutor(threadNum, threadNum, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1000), new NamedThreadFactory("async"),
                new ThreadPoolExecutor.AbortPolicy());
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);
        for (int i = 0; i < threadNum; i++) {
            completionService.submit(assembleCallable(i));
        }

        executor.shutdown();
        int completedNum = 0;
        while (completedNum < threadNum) {
            try {
                // 检索并移除表示下一个已完成任务的 Future，如果目前不存在这样的任务，则等待。
                Future<String> future = completionService.take();
                try {
                    // 放入队列中的都是已经完成的任务
                    String result = future.get();
                    System.out.println("completed task result:" + result);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            completedNum++;
        }


    }

    public static Callable<String> assembleCallable(int no) {
        return () -> {
            System.out.println("executing task no " + no);
            TimeUnit.SECONDS.sleep(1L);
            int rand = new Random().nextInt(10);
            return String.valueOf(rand) + "-" + no;
        };
    }



}
