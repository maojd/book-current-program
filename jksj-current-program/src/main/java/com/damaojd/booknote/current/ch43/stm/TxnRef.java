package com.damaojd.booknote.current.ch43.stm;

//支持事务的引用
public class TxnRef<T>
{
    // 当前数据，带版本号. 系统中的最新值
    volatile VersionedRef<T> curRef;
    
//    public TxnRef(VersionedRef curRef)
//    {
//        super();
//        this.curRef = curRef;
//    }
    public TxnRef(T value)
    {
        super();
        this.curRef = new VersionedRef<T>(value, 0L);
    }
    
    public T getValue(Txn<T> txn)
    {
        return txn.get(this);
    }
    
    public void setValue(T value, Txn<T> txn)
    {
        txn.set(this, value);
    }
}
