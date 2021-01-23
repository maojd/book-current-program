package com.damaojd.booknote.current.ch43.stm.interfac;

//@FunctionalInterface标记在接口上，“函数式接口”是指仅仅只包含一个抽象方法的接口。
@FunctionalInterface
public interface TxnRunnable
{
    void run(Txn txn);
    
    // 如果一个接口中包含不止一个抽象方法，那么不能使用@FunctionalInterface，编译会报错。
//    void run2(Txn txn);
}
