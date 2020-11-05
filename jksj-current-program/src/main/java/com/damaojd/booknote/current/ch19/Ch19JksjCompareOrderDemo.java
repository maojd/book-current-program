package com.damaojd.booknote.current.ch19;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Ch19JksjCompareOrderDemo
{
    public static void main(String[] args)
    {
        Ch19JksjCompareOrderDemo d = new Ch19JksjCompareOrderDemo();
        d.checkAll();
//        checkAll启动了两个线程。 执行了 barrier.await两个线程相互等待。 满足条件以后（查询到了订单和运单）,会自动触发 barrier的回调
        
    }
    
    // 订单队列
    Vector<P> pos = new Vector<>();
    
    // 派送单队列
    Vector<D> dos = new Vector<>();
    
    // 执行回调的线程池
    Executor executor = Executors.newFixedThreadPool(1);
    
    final CyclicBarrier barrier = new CyclicBarrier(2, () -> {
        // 满足条件后的回调。
        executor.execute(() -> check());
    });
    
    void check()
    {
        System.out.println(String.format(Thread.currentThread().getName() + "满足了一次条件。合并任务  pos订单size %s , dSize %s ", pos.size(),
                dos.size()));
        P p = pos.remove(0);
        D d = dos.remove(0);
        // 执行对账操作
        int diff = checkComparePD(p, d);
        // 差异写入差异库
        save(diff);
    }
    
    void checkAll()
    {
        // 循环查询订单库
        Thread T1 = new Thread(() -> {
//            while (存在未对账订单)
            while (true)
            {
                // 查询订单库
                pos.add(getPOrders());
                // 等待
                try
                {
                    System.out.println(Thread.currentThread().getName() + "订单准备好了");
                    barrier.await();
                }
                catch (InterruptedException | BrokenBarrierException e)
                {
                }
            }
        });
        T1.start();
        // 循环查询运单库
        Thread T2 = new Thread(() -> {
//            while (存在未对账订单)
            while (true)
            {
                // 查询运单库
                dos.add(getDOrders());
                // 等待
                try
                {
                    System.out.println(Thread.currentThread().getName() + "运单准备好了");
                    barrier.await();
                }
                catch (InterruptedException | BrokenBarrierException e)
                {
                }
            }
        });
        T2.start();
    }
    
    private void save(int diff)
    {
        System.out.println(Thread.currentThread().getName() + " save------");
    }
    
    private int checkComparePD(P p, D d)
    {
        System.out.println(Thread.currentThread().getName() + " checkComparePD ---- ");
        return 0;
    }
    
    private D getDOrders()
    {
        System.out.println(Thread.currentThread().getName() + " 查询运单开始 ");
        try
        {
            Thread.sleep(new Random().nextInt(5000));
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " 查询运单结束 -- 慢 ");
        return new D();
    }
    
    private P getPOrders()
    {
        System.out.println(Thread.currentThread().getName() + " 查询订单库 ");
        return new P();
    }
}

class P
{
    
}

class D
{
    
}
