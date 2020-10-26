package com.damaojd.booknote.current.ch25;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Ch25CompletionServiceTest
{
    private static int getPriceByS1() throws InterruptedException
    {
        System.out.println("getPriceByS1-----star");
        Thread.sleep(2000L);
        System.out.println("getPriceByS1-----");
        return 1;
    }
    
    private static int getPriceByS2() throws InterruptedException
    {
        System.out.println("getPriceByS2-----star");
        Thread.sleep(15000L);
        
        System.out.println("getPriceByS2-----");
        return 2;
    }
    
    private static int getPriceByS3() throws InterruptedException
    {
        System.out.println("getPriceByS3-----star");
        Thread.sleep(3000L);
        System.out.println("getPriceByS3-----");
        return 3;
    }
    
    private static void save(Integer r)
    {
        // TODO Auto-generated method stub
        System.out.println("保存价格-----" + r);
//        return 456;
    }
    
    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 异步向电商 S1 询价
        Future<Integer> f1 = executor.submit(
                () -> getPriceByS1());
        // 异步向电商 S2 询价
        Future<Integer> f2 = executor.submit(
                () -> getPriceByS2());
        // 异步向电商 S3 询价
        Future<Integer> f3 = executor.submit(
                () -> getPriceByS3());
        
        // 获取电商 S1 报价并保存
        Integer r1 = f1.get();
        System.out.println(r1);
        executor.execute(() -> save(r1));
        
        // 获取电商 S2 报价并保存
        Integer r2 = f2.get();
        System.out.println(r2);
        executor.execute(() -> save(r2));
        
        // 获取电商 S3 报价并保存
        Integer r3 = f3.get();
        System.out.println(r3);
        executor.execute(() -> save(r3));
        
        
        
        // 问题，如果某个电商的价格获取了很久，比如f2.get(); 花费10s，那么 r3的结果也需要等待。因为fx.get都在主线程中
        
        // 解决思路： 返回的结果，丢到一个阻塞队列中，监听这个队列，有消息就pop出来消费。
    }
    
}
