package com.damaojd.booknote.current.ch26;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Ch26ForkJoinTest
{
    public static void main(String[] args)
    {
        ForkJoinPool fjp = new ForkJoinPool(4);
        
        // 创建分支任务 task。 计算 斐波那契数列
        Fibonacci fibTask = new Fibonacci(6);
//        FibonacciTest2 fibTask = new FibonacciTest2(6);
        Integer result = fjp.invoke(fibTask);
        System.out.println(result);
        
        /**
         * 两个demo的主要区别在于compute方法。 极客时间用的是 a.fork ,b.join
         * 
         * 廖雪峰的教程是： a.fork b.fork invokeAll(subtask1, subtask2); 汇总两个任务的结果。
         * 
         * 
         */
        
        // 廖雪峰 blog主要代码
//        class SumTask extends RecursiveTask<Long> {
//            protected Long compute() {
//                // “分裂”子任务:
//                SumTask subtask1 = new SumTask(...);
//                SumTask subtask2 = new SumTask(...);
//                // invokeAll会并行运行两个子任务:
//                invokeAll(subtask1, subtask2);
//                // 获得子任务的结果:
//                Long subresult1 = subtask1.join();
//                Long subresult2 = subtask2.join();
//                // 汇总结果:
//                return subresult1 + subresult2;
//            }
//        }
        
    }
    
    /**
     * 极客时间 官方demo
     * 
     * @author：maojd
     * @createtime ： 2020年10月30日 上午9:08:46
     * @description 一句话描述
     * @since version 初始于版本
     */
    // 0,1, 1, 2,3,5,8
    static class Fibonacci extends RecursiveTask<Integer>
    {
        int n;
        
        Fibonacci(int n)
        {
            this.n = n;
        }
        
        @Override
        protected Integer compute()
        {
            if(n <= 1)
            {
                return n;
            }
            Fibonacci f1 = new Fibonacci(n - 1);
            // 创建子任务
            f1.fork();
            
            Fibonacci f2 = new Fibonacci(n - 2);
            
            int f2Com = f2.compute();
            int f1Join = f1.join();
            System.out.println("n=" + n + ", 前前一个 f2.compute() " + f2Com);
            System.out.println("n=" + n + "，前一个   f1.join()" + f1Join);
            
            return f2Com + f1Join;
//            return f2.compute() + f1.join();
        }
        
    }
    
    /**
     * 参考 廖雪峰 fork join 。 主要区别在compute方法。
     * https://www.liaoxuefeng.com/wiki/1252599548343744/1306581226487842
     * 
     * @author：maojd
     * @createtime ： 2020年10月30日 上午9:09:09
     * @description 一句话描述
     * @since version 初始于版本
     */
    // 0,1, 1, 2,3,5,8
    static class FibonacciTest2 extends RecursiveTask<Integer>
    {
        int n;
        
        FibonacciTest2(int n)
        {
            this.n = n;
        }
        
        @Override
        protected Integer compute()
        {
            if(n <= 1)
            {
                return n;
            }
            FibonacciTest2 f1 = new FibonacciTest2(n - 1);
            // 创建子任务
//            f1.fork();
            
            FibonacciTest2 f2 = new FibonacciTest2(n - 2);
            
            super.invokeAll(f1, f2);
            int f1Join = f1.join();
            int f2Join = f2.join();
            System.out.println("n=" + n + ", 前前一个 f2.join()" + f2Join);
            System.out.println("n=" + n + "，前一个   f1.join()" + f1Join);
            
            return f1Join + f2Join;
        }
        
    }
}
