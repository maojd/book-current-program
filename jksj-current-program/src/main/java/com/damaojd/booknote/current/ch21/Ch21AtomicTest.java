package com.damaojd.booknote.current.ch21;

public class Ch21AtomicTest
{
    long count2 = 0;
    
    void add10K2()
    {
        int idx = 0;
        while (idx++ < 100000)
        {
            count2 += 1;
        }
    }
    
    public static void main(String[] args)
    {
        
        Long count = 0L;
        do
        {
            Ch21AtomicTest t = new Ch21AtomicTest();
            t.add10K2();
//            System.out.println(t.count2);
            count = t.count2;
        }
        while (count == 100000L);
        System.out.println(count);
        System.out.println("结束循环");

        
    }
}
