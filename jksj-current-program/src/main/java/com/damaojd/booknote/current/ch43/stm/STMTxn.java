package com.damaojd.booknote.current.ch43.stm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

/**
 * STM 事务实现类
 */
public class STMTxn<T> implements Txn
{
    // 事务id生成器
    private static AtomicLong txnSeq = new AtomicLong(0);
    
    private Map<TxnRef, VersionedRef> inTxnMap = new HashMap<TxnRef, VersionedRef>();
    
    private Map<TxnRef, Object> writeMap = new HashMap<TxnRef, Object>();
    
    // 当前事务id
    private long txnId;
    
    public STMTxn()
    {
        txnId = txnSeq.incrementAndGet();
        System.out.println("txnId == " + txnId);
    }
    
    // 获取当前事务的数据
    public Object get(TxnRef ref)
    {
        // 将需要读取的数据放入 intxnMap
        if(!inTxnMap.containsKey(ref))
        {
            inTxnMap.put(ref, ref.curRef);
        }
        // ref.curRef.value
        return inTxnMap.get(ref).value;
    }
    
    // 当前事务修改数据
    public void set(TxnRef ref, Object value)
    {
        // 需要修改的数据也加入inTxnMap
        if(!inTxnMap.containsKey(ref))
        {
            inTxnMap.put(ref, ref.curRef);
        }
        writeMap.put(ref, value);
    }
    
    boolean commit()
    {
        synchronized (STM.commitLock)
        {
            boolean isValid = true;
            
            for(Entry<TxnRef, VersionedRef> entry : inTxnMap.entrySet())
            {
//                inTxnMap.put(txnRef, txnRef.curRef);
                VersionedRef curRef = entry.getKey().curRef;// txnRef.curRef
                VersionedRef readRef = entry.getValue();
            }
            
            return isValid;
        }
        
    }
    
    public static void main(String[] args)
    {
//        STMTxn stm = new STMTxn();
//        System.out.println(stm + " " + JSON.toJSONString(stm));
//        stm = new STMTxn();
//        System.out.println(stm + " " + JSON.toJSONString(stm));
        STMTxn stm = new STMTxn();
        
        //继续。 如果map里面 obj.xxx变化。
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        Map<Object, STMTxn> map = new HashMap<>();
        map.put(obj1, stm);
        map.put(obj2, stm);
        map.put(obj3, stm);
        map.forEach((k, v) -> {
            System.out.println(k + "---" + v);
            Object obj4 = new Object();
            k = obj4;
        });
        
        System.out.println("====");
        map.forEach((k, v) -> {
            System.out.println(k + "---" + v);
            Object obj4 = new Object();
            k = obj4;
        });
    }
    
}
