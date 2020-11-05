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
        
//        CyclicBarrier 的回调函数使用了一个固定大小为 1 的线程池，是否合理？我觉得是合理的，可以从以下两个方面来分析。
        
//        第一个是线程池大小是 1，只有 1 个线程，主要原因是 check() 方法的耗时比 getPOrders() 和 getDOrders() 都要短，
//        所以没必要用多个线程，同时单线程能保证访问的数据不存在并发问题。
        
//        第二个是使用了线程池，如果不使用，直接在回调函数里调用 check() 方法是否可以呢？绝对不可以。为什么呢？这个要分析一下回调函数和唤醒等待线程之间的关系。
//        下面是 CyclicBarrier 相关的源码，通过源码你会发现 CyclicBarrier 是同步调用回调函数之后才唤醒等待的线程，
//        如果我们在回调函数里直接调用 check() 方法，那就意味着在执行 check() 的时候，是不能同时执行 getPOrders() 和 getDOrders() 的，
//        这样就起不到提升性能的作用。
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
