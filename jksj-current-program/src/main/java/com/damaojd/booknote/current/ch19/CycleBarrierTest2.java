package com.damaojd.booknote.current.ch19;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CycleBarrierTest2
{
    //添加线程，要修改这个数量
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
        // 当计数器为0时，立即执行
        @Override
        public void run()
        {
            System.out.println("计数器为0 最后一个线程。汇总线程：" + Thread.currentThread().getName() + " 任务合并。");
        }
    });
    
    public static void main(String[] args)
    {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        
        // 将线程A添加到线程池
        executorService.submit(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("线程A：" + Thread.currentThread().getName() + "执行第1步。" + cyclicBarrier.getNumberWaiting());
                    cyclicBarrier.await();
                    
//                    Thread.sleep(2000L);
                    
                    System.out.println("线程A：" + Thread.currentThread().getName() + "执行第2步。" + cyclicBarrier.getNumberWaiting());
                    cyclicBarrier.await();
                    System.out.println("线程A：" + Thread.currentThread().getName() + "执行第3步。" + cyclicBarrier.getNumberWaiting());
                    
                    cyclicBarrier.await();
                    System.out.println("线程A：" + Thread.currentThread().getName() + "执行第4步。" + cyclicBarrier.getNumberWaiting());
                    
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        
        // 将线程B添加到线程池
        executorService.submit(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("线程B：" + Thread.currentThread().getName() + "执行第1步。" + cyclicBarrier.getNumberWaiting());
                    cyclicBarrier.await();
                    System.out.println("线程B：" + Thread.currentThread().getName() + "执行第2步。" + cyclicBarrier.getNumberWaiting());
                    
                    cyclicBarrier.await();
                    System.out.println("线程B：" + Thread.currentThread().getName() + "执行第3步。" + cyclicBarrier.getNumberWaiting());
                    
                    Thread.sleep(3000L);
                    cyclicBarrier.await();
                    System.out.println("线程B：" + Thread.currentThread().getName() + "执行第4步。" + cyclicBarrier.getNumberWaiting());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        
        executorService.submit(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    System.out.println("线程C：" + Thread.currentThread().getName() + "执行第1步。" + cyclicBarrier.getNumberWaiting());
                    cyclicBarrier.await();
                    System.out.println("线程C：" + Thread.currentThread().getName() + "执行第2步。" + cyclicBarrier.getNumberWaiting());
                    
                    cyclicBarrier.await();
                    System.out.println("线程C：" + Thread.currentThread().getName() + "执行第3步。" + cyclicBarrier.getNumberWaiting());
                    
                    Thread.sleep(3000L);
                    cyclicBarrier.await();
                    System.out.println("线程C：" + Thread.currentThread().getName() + "执行第4步。" + cyclicBarrier.getNumberWaiting());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        
        // 关闭线程池
        executorService.shutdown();
    }
}