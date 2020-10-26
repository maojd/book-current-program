package com.damaojd.booknote.current.ch25;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class Ch25CompletionServiceTest2
{
    
    private static void save(Integer r)
    {
        // TODO Auto-generated method stub
        System.out.println("保存价格-----" + r);
//        return 456;
    }
    
    private static int queryPrice()
    {
        Random random = new Random();
        int max = 5000;
        int min = 1000;
        int s = random.nextInt(max) % (max - min + 1) + min;
        try
        {
            Thread.sleep(s);
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }
        System.out.println("返回价格结果:" + s);
        return s;
    }
    
    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        // 创建线程池。 5个线程用来询问价格，5个线程用到保存结果
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
//        ExecutorCompletionService(Executor executor, BlockingQueue<Future<V>> completionQueue)。
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        // 问题，如果某个电商的价格获取了很久，比如f2.get(); 花费10s，那么 r3的结果也需要等待。因为fx.get都在主线程中
        
        // 解决思路： 返回的结果，丢到一个阻塞队列中，监听这个队列，有消息就pop出来消费。
        // 实际不用手工实现阻塞队列，java内置已经帮我们实现好了。 cs.take().get();
        // 向5个电商获取价格。
        
        List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
        for(int i = 0; i < 5; i++)
        {
//            Future<Integer> f = executor.submit(() -> {
            // 这里换成cs
            Future<Integer> f = cs.submit(() -> {
                int price = queryPrice();
                return price;
            });
            futures.add(f);
        }
        
        // 将5个电商询价结果异步保存到数据库
//        for(int j = 0; j < 5; j++)
//        {
//            Integer r = cs.take().get();
//            System.out.println(r);
//            executor.execute(() -> save(r));
//        }
        
//        // 需求1. 获取到一个，就可以了。
//        Integer r = 0;
//        try
//        {
//            // 只要有一个成功返回，则 break
//            for(int i = 0; i < 3; ++i)
//            {
//                r = cs.take().get();
//                // 简单地通过判空来检查是否成功返回
//                if(r != null)
//                {
//                    break;
//                }
//            }
//        }
//        finally
//        {
//            // 取消所有任务
//            for(Future<Integer> f : futures)
//            {
//                try
//                {
//                    
//                    f.cancel(true);
//                }
//                catch (Exception e)
//                {
//                    System.out.println("终止任务时异常, " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        }
//        // 返回结果
//        System.out.println(" 获取到最快的一个结果  " + r);
        
        // 需求2. 获取到所有的报价，比较价格，获取最低的价格。
        // 将询价结果异步保存到数据库
        // 并计算最低报价
        AtomicReference<Integer> m = new AtomicReference<>(Integer.MAX_VALUE);
        CountDownLatch latch = new CountDownLatch(5);
        for(int i = 0; i < 5; i++)
        {
            executor.execute(() -> {
                Integer r = null;
                try
                {
                    r = cs.take().get();
                }
                catch (Exception e)
                {
                }
                
                save(r);
                // 计数器减一
                latch.countDown();
                
                // 这里存在并发的问题。线程不安全
                // TODO Math.min不是原子操作
                m.set(Integer.min(m.get(), r));
            });
        }
        
        while (true)
        {
            if(latch.getCount() == 0)
            {
                // 获取到了全部的结果。
                System.out.println("获取到了全部的结果,最低价为:" + m);
                break;
            }
            else
            {
                System.out.println("暂时休息1s");
                Thread.sleep(1000L);
            }
            
        }
    }
    
}
