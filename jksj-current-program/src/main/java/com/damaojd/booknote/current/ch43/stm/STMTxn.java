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
        
        // 模拟 inTxnMap里面的版本号变化start
        TxnRef<Integer> balance = new TxnRef<Integer>(500);
        System.out.println(" balance 500   =" + balance);
        System.out.println("balance.curRef =" + balance.curRef);
        
        Map<TxnRef, VersionedRef> inTxnMap = new HashMap<TxnRef, VersionedRef>();
        inTxnMap.put(balance, balance.curRef);
        
        System.out.println(inTxnMap.get(balance));
        
        System.out.println("forEach======");
        inTxnMap.forEach((k, v) -> {
            System.out.println("k " + k + ",   v " + v);
            k.curRef = new VersionedRef<>(v, 333);// 新的版本号
        });
        System.out.println("forEach end======");
        
        for(Map.Entry<TxnRef, VersionedRef> entry : inTxnMap.entrySet())
        {
            VersionedRef curRef = entry.getKey().curRef;
            VersionedRef readRef = entry.getValue();
            // 通过版本号来验证数据是否发生过变化
            System.out.println(curRef.version);// 333
            System.out.println(readRef.version);// 0
            if(curRef.version != readRef.version)
            {
                break;
            }
        }
        // 模拟 map里面的版本号变化end
    }
    
}
