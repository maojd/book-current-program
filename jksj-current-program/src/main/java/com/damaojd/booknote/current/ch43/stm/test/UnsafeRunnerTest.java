package com.damaojd.booknote.current.ch43.stm.test;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.damaojd.booknote.current.ch43.UnsafeAccount;

class UnsafeRunnerTest implements Runnable
{
    
    public static UnsafeAccount acc1 = new UnsafeAccount(10000);
    
//    public static UnsafeAccount acc2 = new UnsafeAccount(0);
    
    // 一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)
    private CyclicBarrier barrier;
    
    private String name;
    
    UnsafeRunnerTest(CyclicBarrier barrier, String name)
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
            Thread.sleep(100 * (new Random()).nextInt(8));
            System.out.println(name + " 准备好了...");
            // barrier的await方法，在所有参与者都已经在此 barrier 上调用 await 方法之前，将一直等待。
            barrier.await();
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
        
        // 不安全的转账
        UnsafeAccount accTmp = new UnsafeAccount(0);
        acc1.transfer(accTmp, 1);
    }
    
    public static void main(String[] args)
    {
        
//        UnsafeAccount accTmp = new UnsafeAccount(0);
//        acc1.trunsfer(accTmp, 20000);
//        System.out.println(RunnerTest.acc1.getBalance());
        
        int count = 8000;
        // 如果将参数改为4，但是下面只加入了3个选手，这永远等待下去
        // Waits until all parties have invoked await on this barrier.
        CyclicBarrier barrier = new CyclicBarrier(count);
        ExecutorService executor = Executors.newFixedThreadPool(count);
        for(int i = 0; i < count; i++)
        {
            executor.submit(new UnsafeRunnerTest(barrier, "选手" + i));
        }
        
        System.out.println(UnsafeRunnerTest.acc1.getBalance());
        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        // 余额 应该为 5000 - count
        // eg:count = 1,余额为 4999
        // 实际出现run 4.5次就会出现一次： 10000 转账 8000次，剩余 2001,2003的情况
        System.out.println(UnsafeRunnerTest.acc1.getBalance());
        executor.shutdown();
        
    }
}