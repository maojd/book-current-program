package com.damaojd.booknote.current.ch01;

public class DemoCountCpu
{
    
    private long i = 0;
    
    public void add10K()
    {
        int c = 0;
        while (c++ < 10000)
        {
//            i = i + 1;
            i += 1;
        }
    }
    
    public static long calc() throws InterruptedException
    {
        final DemoCountCpu test = new DemoCountCpu();
        Thread th1 = new Thread(() -> {
            test.add10K();
        });
        Thread th2 = new Thread(() -> {
            test.add10K();
        });
        // 启动两个线程
        th1.start();
        th2.start();
        // 等待两个线程执行结束
        th1.join();
        th2.join();
        return test.i;
    }
    
    public static void main(String[] args) throws InterruptedException
    {
//        DemoCountCpu c = new DemoCountCpu();
//        c.add10K();
//        
        // 10000
//        System.out.println(c.i);
        
        // 16416 18833 远远小于20000. 原因cpu是按照指令的原子操作执行。
        
        System.out.println(calc());
        
        /** note **/
//        指令 1：首先，需要把变量 count 从内存加载到 CPU 的寄存器；
//        指令 2：之后，在寄存器中执行 +1 操作；
//        指令 3：最后，将结果写入内存（缓存机制导致可能写入的是 CPU 缓存而不是内存）。
    }
}
