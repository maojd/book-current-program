package com.damaojd.booknote.current.ch19;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class RunnerTest implements Runnable
{
    
    public static Set container1 = new HashSet();
    
    public static Set container = Collections.synchronizedSet(container1); // 返回了一个线程安全的Set
    
    public static List<String> list = new ArrayList<String>();
    
    // 一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)
    private CyclicBarrier barrier;
    
    private String name;
    
    RunnerTest(CyclicBarrier barrier, String name)
    {
        super();
        this.barrier = barrier;
        this.name = name;
    }
    
    @Override
    public void run()
    {
        try
        {
            Thread.sleep(new Random().nextInt(2000));
            System.out.println( Thread.currentThread().getName() + name + " 第一轮 准备好了...");
            // barrier的await方法，在所有参与者都已经在此 barrier 上调用 await 方法之前，将一直等待。
            barrier.await();
            System.out.println( Thread.currentThread().getName() + name + " 第一轮 run...");
            
            Thread.sleep(new Random().nextInt(5000));
            System.out.println( Thread.currentThread().getName() + name + " 第二轮 准备好了...");
            barrier.await();
            System.out.println( Thread.currentThread().getName() + name + " 第二轮 一起run...");
            
            Thread.sleep(new Random().nextInt(10000));
            System.out.println( Thread.currentThread().getName() + name + " 第三轮 准备好了...");
            barrier.await();
            System.out.println( Thread.currentThread().getName() + name + " 第三轮 一起run...");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (BrokenBarrierException e)
        {
            e.printStackTrace();
        }
        String uuid = UUID.randomUUID().toString();
//		System.out.println(name + "---" + uuid);
        container.add(uuid);
        list.add(uuid);
//        System.out.println(System.currentTimeMillis() + "  " + name + " 起跑！");  
    }
}

public class Ch19MyCyclicBarrierTest
{
    
    public static void main(String[] args)
    {
        int count = 5;
        
        // 如果将参数改为pool 4，但是下面只加入了3个选手，这永远等待下去
        // Waits until all parties have invoked await on this barrier.
//        CyclicBarrier barrier = new CyclicBarrier(count);
        CyclicBarrier barrier = new CyclicBarrier(count, new Runnable() {
            // 当计数器为0时，立即执行
            @Override
            public void run()
            {
                System.out.println("计数器为0 最后一个线程。汇总线程：" + Thread.currentThread().getName() + " 任务合并。");
            }
        });
        ;
        ExecutorService executor = Executors.newFixedThreadPool(10000);
        for(int i = 0; i < count; i++)
        {
            executor.submit(new RunnerTest(barrier, "选手" + i));
//			RunnerTest.container.add(UUID.randomUUID());
        }
//		executor.shutdown(); 
//		exe.submit(new RunnerTest(barrier, "选手4"));
        
        System.out.println(RunnerTest.container.size());
        
        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
//		System.out.println(RunnerTest.container);
//		
        System.out.println("==================="  + RunnerTest.container.size());
//		executor.shutdown(); 
        
    }
    
}
