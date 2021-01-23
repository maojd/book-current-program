package com.damaojd.booknote.current.ch43.stm;

import org.apache.log4j.Logger;
import com.damaojd.booknote.current.ch43.stm.interfac.Txn;

//支持事务的引用
public class TxnRef<T>
{
    private static Logger log = Logger.getLogger(TxnRef.class);
    
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
        log.info(" txn.set txn " + txn + ", value " + "  " + value);
    }
}
