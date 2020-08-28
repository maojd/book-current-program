package com.damaojd.booknote.current.ch01;

public class Demo2Singleton
{
//    private static  Demo2Singleton instance;
    
    private static volatile Demo2Singleton instance;
    
    public static Demo2Singleton getInstance()
    {
        if(null == instance)
        {
            synchronized (Demo2Singleton.class)
            {
                if(null == instance)
                {
                    instance = new Demo2Singleton();
                }
            }
        }
        return instance;
    }
    
    public static void testGetNull() throws InterruptedException
    {
        
//        final Demo2Singleton test = new Demo2Singleton();
        Thread th1 = new Thread(() -> {
            Demo2Singleton s = Demo2Singleton.getInstance();
            System.out.println(s);
            if(null == s)
            {
                System.out.println("获取到了空对象1");
            }
        });
        
        Thread th2 = new Thread(() -> {
            Demo2Singleton s = Demo2Singleton.getInstance();
            System.out.println(s);
            if(null == s)
            {
                System.out.println("获取到了空对象2");
            }
        });
        // 启动两个线程
        th1.start();
        th2.start();
        // 等待两个线程执行结束
        th1.join();
        th2.join();
        
    }
    
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println(Demo2Singleton.getInstance());
        System.out.println(Demo2Singleton.getInstance());
        /** 笔记 **/
//      1.两次判断为空
//       2.注意使用 volatile 修饰 变量。 否则 cpu从排序,可能获取null. 很低的概率。
        
        /** 原因 **/
//        这看上去一切都很完美，无懈可击，但实际上这个 getInstance() 方法并不完美。问题出在哪里呢？
//        出在 new 操作上，我们以为的 new 操作应该是：
//        分配一块内存 M；
//        在内存 M 上初始化 Singleton 对象；
//        然后 M 的地址赋值给 instance 变量。
//        但是实际上优化后的执行路径却是这样的：
//
//        分配一块内存 M；
//        将 M 的地址赋值给 instance 变量；
//        最后在内存 M 上初始化 Singleton 对象。
        
        
//        thread1 判断 == null,   分配一块地址 M, 还没有执行初始化给实例。  
//        cpu切换到了 thread2, thread2 判断 == null? 结果不为null,直接return . 但是拿到了 未初始化的实例。 程序依然会报空指针 
//        Singletone.png
    }
}
