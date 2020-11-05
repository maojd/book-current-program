package com.damaojd.booknote.current.ch21;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 设置安全库存demo
 * 
 * @param upper
 * @param lower
 */
public class Ch21SafeWMTest
{
    
    final AtomicReference<WMRange> rf = new AtomicReference<WMRange>(new WMRange(0, 0));
    
    // 设置库存上限
    void setUpper(int v)
    {
        WMRange nr;
        WMRange or;
        // 原代码在这里
        // WMRange or=rf.get();
        do
        {
            // 移动到此处
            // 每个回合都需要重新获取旧值
            or = rf.get();
            System.out.println(Thread.currentThread().getName() + "尝试 get旧的 old " + or);
            
            try
            {
                Thread.sleep(new Random().nextInt(2000));
            }
            catch (InterruptedException e)
            {
            }
            
            // 检查参数合法性
            if(v < or.lower)
            {
                throw new IllegalArgumentException();
            }
            nr = new WMRange(v, or.lower);
            System.out.println(Thread.currentThread().getName() + "尝试 set新的 new " + nr);
        }
//        while (!rf.compareAndSet(or, nr));
        while (!copraseAndSet(nr, or));// 只是为了打印日志
        
        System.out.println(
                Thread.currentThread().getName() + String.format("成功 set  low %s  upper  %s ", rf.get().lower, rf.get().upper));
    }
    
    private boolean copraseAndSet(WMRange nr, WMRange or)
    {
        System.out.println(Thread.currentThread().getName() + "比较 新的------- new " + nr + ", old期望值 " + or);
        boolean b = rf.compareAndSet(or, nr);
        System.out.println(Thread.currentThread().getName() + "比较 新的------- new " + nr + ", old期望值 " + or + ", 结果 " + b);
        return b;
    }
    
//    // 设置库存上限. 会出现死循环。
//    void setUpper(int v)
//    {
//        WMRange nr;
//        // 如果线程1 运行到WMRange or = rf.get();停止，切换到线程2
//        // 更新了值，切换回到线程1，进入循环将永远比较失败死循环，解决方案是将读取的那一句放入循环里，CAS每次自旋必须要重新检查新的值才有意义
//        WMRange or = rf.get();
//        try
//        {
//            Thread.sleep(new Random().nextInt(2000));
//        }
//        catch (InterruptedException e)
//        {
//        }
//        
//        do
//        {
//            // 检查参数合法性
//            if(v < or.lower)
//            {
//                throw new IllegalArgumentException();
//            }
//            nr = new WMRange(v, or.lower);
//            System.out.println(" -----尝试set 库存上限- ");
//        }
//        while (!rf.compareAndSet(or, nr));
//    }
    
    class WMRange
    {
        final int upper;
        
        final int lower;
        
        public WMRange(int upper, int lower)
        {
            super();
            this.upper = upper;
            this.lower = lower;
        }
        
    }
    
    public static void main(String[] args) throws InterruptedException
    {
        Ch21SafeWMTest t = new Ch21SafeWMTest();
        
        Thread t1 = new Thread(new Runnable() {
            
            @Override
            public void run()
            {
                System.out.println("t1 start");
                t.setUpper(20);
            }
        });
        Thread t2 = new Thread(new Runnable() {
            
            @Override
            public void run()
            {
                System.out.println("t2 start");
                t.setUpper(30);
            }
        });
        
        t1.start();
        t2.start();
        
        Thread.sleep(5000L);
        System.out.println(t.rf.get().lower);
        System.out.println(t.rf.get().upper);
        
        // 重点： setUpper 方法
    }
}
