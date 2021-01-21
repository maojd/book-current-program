package com.damaojd.booknote.current.ch43.stm;

/**
 * Txn 代表的是读写操作所在的当前事务
 */
public interface Txn<T>
{
    public T get(TxnRef<T> ref);
    
    public void set(TxnRef<T> ref, T value);
}
