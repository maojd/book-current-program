package com.damaojd.booknote.current.ch43.stm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Logger;
import com.damaojd.booknote.current.ch43.stm.interfac.Txn;

/**
 * STM 事务实现类
 */
public class STMTxn<T> implements Txn
{
    private static Logger log = Logger.getLogger(STMTxn.class);
    
    // 事务id生成器
    private static AtomicLong txnSeq = new AtomicLong(0);
    
    private Map<TxnRef, VersionedRef> inTxnMap = new HashMap<TxnRef, VersionedRef>();
    
    private Map<TxnRef, Object> writeMap = new HashMap<TxnRef, Object>();
    
    // 当前事务id
    private long txnId;
    
    public STMTxn()
    {
        txnId = txnSeq.incrementAndGet();
//        System.out.println("txnId == " + txnId);
        log.info("txnId " + txnId);
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
    
    // 提交事务
    boolean commit()
    {
        synchronized (STM.commitLock)
        {
            boolean isValid = true;
            // 校验读到的数据是否发生变化
            for(Entry<TxnRef, VersionedRef> entry : inTxnMap.entrySet())
            {
//                inTxnMap.put(txnRef, txnRef.curRef);
                VersionedRef curRef = entry.getKey().curRef;// txnRef.curRef
                VersionedRef readRef = entry.getValue();
                if(curRef.version != readRef.version)
                {
                    isValid = false;
                    break;
                }
            }
            
            // 如果校验通过，则所有更改生效
            if(isValid)
            {
// 只更新当前事务中的最新值， value存放的快照并没有更新。
// 如果事务对象发生版本更新/事务对象.cruRef指向新的值（k.curRef = new  VersionedRef(v, txnId);）。但是map里面的value依然指向旧的值。
                writeMap.forEach((k, v) -> {
                    k.curRef = new VersionedRef<>(v, txnId);
                    System.out.println("提交 txnId " + txnId + ", v=" + v);
                });
            }
            log.info("校验不通过 " + txnId);
            return isValid;
        }
        
    }
    
    public static void main(String[] args)
    {
        
        // 模拟 inTxnMap里面的版本号变化start
        TxnRef<Integer> balance = new TxnRef<Integer>(500);
        System.out.println(" balance 500   =" + balance);
        System.out.println("balance.curRef =" + balance.curRef);
        
//        Map<TxnRef, VersionedRef> map2 = new HashMap<TxnRef, VersionedRef>();
        
        // key: 带有当前事务的对象， value: 事务快照的值。
        // 如果事务对象发生版本更新/事务对象.cruRef指向新的值（k.curRef = new
        // VersionedRef(v, txnId);）。但是map里面的value依然指向旧的值。
        Map<TxnRef, VersionedRef> inTxnMap = new HashMap<TxnRef, VersionedRef>();
        inTxnMap.put(balance, balance.curRef);
//        map2.put(balance, balance.curRef);
        
        System.out.println("forEach======");
        inTxnMap.forEach((k, v) -> {
            System.out.println("k=" + k + ",   v=" + v + ", k.curRef=" + k.curRef);
            k.curRef = new VersionedRef<>(v, 333);// 新的版本号
            System.out.println("k=" + k + ",   v=" + v + ", k.curRef=" + k.curRef);
        });
        System.out.println("forEach end======");
        
//        System.out.println("forEachMap2======");
//        map2.forEach((k, v) -> {
//            System.out.println("map2 k=" + k + ",   v=" + v + ", k.curRef=" + k.curRef);
//        });
//        System.out.println("forEachMap2 end======");
        
        for(Map.Entry<TxnRef, VersionedRef> entry : inTxnMap.entrySet())
        {
            VersionedRef curRef = entry.getKey().curRef;
            VersionedRef readRef = entry.getValue();
            // 通过版本号来验证数据是否发生过变化
            System.out.println(readRef);// 0
            System.out.println(readRef.version);// 0
            System.out.println(curRef);// 333
            System.out.println(curRef.version);// 333
            if(curRef.version != readRef.version)
            {
                break;
            }
        }
        // 模拟 map里面的版本号变化end
    }
    
}
