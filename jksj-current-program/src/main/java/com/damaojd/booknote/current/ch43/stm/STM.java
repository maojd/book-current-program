package com.damaojd.booknote.current.ch43.stm;

import org.apache.log4j.Logger;
import com.damaojd.booknote.current.ch43.stm.interfac.TxnRunnable;

public final class STM

{
    private static Logger log = Logger.getLogger(TxnRef.class);
    
    // 私有话构造方法
    private STM()
    {
        
    }
    
    // 提交数据的全局锁
    static final Object commitLock = new Object();
    
    // 原子化提交方法
    public static void atomic(TxnRunnable action)
    {
        boolean committed = false;
        // 没有提交成功,则一直重试
        while (!committed)
        {
            // 创建新的事务
            STMTxn txn = new STMTxn<>();
            log.info(" txn " + txn);
            // 执行业务逻辑
            action.run(txn);
            
            // 提交事务
            committed = txn.commit();
        }
    }
}
