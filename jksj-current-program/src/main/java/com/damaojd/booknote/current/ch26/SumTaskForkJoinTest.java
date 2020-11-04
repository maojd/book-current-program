package com.damaojd.booknote.current.ch26;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306581226487842
 * 
 * @author：maojd
 * @createtime ： 2020年11月2日 下午3:35:44
 * @description 一句话描述
 * @since version 初始于版本
 */
public class SumTaskForkJoinTest
{
    public static void main(String[] args)
    {
        Random random = new Random();
//        long array[] = new long[] { 21, 3, 4, 5, 9 };
        long array[] = new long[2000];
        long expectedSum = 0;
        for(int i = 0; i < array.length; i++)
        {
            array[i] = random.nextInt(1000);
            expectedSum += array[i];
        }
        System.out.println("期望值： expectedSum = " + expectedSum);
        
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        long startTime = System.currentTimeMillis();
        Long result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");
    }
    
}

class SumTask extends RecursiveTask<Long>
{
    private static final long serialVersionUID = 1L;
    
    static final int THRESHOLD = 500;
    
    long[] array;
    
    int start;
    
    int end;
    
    public SumTask(long[] array, int start, int end)
    {
        super();
        this.array = array;
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected Long compute()
    {
        if(end - start <= THRESHOLD)
        {
            long sum = 0;
            for(int i = start; i < end; i++)
            {
                sum += array[i];
                // 故意放慢计算速度:
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException e)
                {
                }
            }
            return sum;
        }
        
        int middle = (end + start) / 2;
        System.out.println(String.format("split %d~%d ==> %d~%d, %d~%d", start, end, start, middle, middle, end));
        SumTask subtask1 = new SumTask(this.array, start, middle);
        SumTask subtask2 = new SumTask(this.array, middle, end);
        invokeAll(subtask1, subtask2);
        Long subresult1 = subtask1.join();
        Long subresult2 = subtask2.join();
        Long result = subresult1 + subresult2;
        System.out.println("result = " + subresult1 + " + " + subresult2 + " ==> " + result);
        return result;
    }
    
}
