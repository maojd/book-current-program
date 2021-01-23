package com.damaojd.booknote.current.ch43.stm;

public class Account
{
    // 余额
    private TxnRef<Integer> balance;
    
    public Account(int balance)
    {
        this.balance = new TxnRef<Integer>(balance);
    }
    
    public void transfer(Account target, int amount)
    {
        STM.atomic((txn) -> {
            Integer from = balance.getValue(txn);
            balance.setValue(from - amount, txn);
            System.out.println("提交 atomic 余额 "  + (from - amount));
            
            Integer to = target.balance.getValue(txn);
            target.balance.setValue(to + amount, txn);
        });
    }
    
    // 自己写的获取余额
    public Integer getBalance2()
    {
        STMTxn txn = new STMTxn<>();
        if(null != balance)
        {
            return balance.getValue(txn);
        }
        else
        {
            return 0;
        }
    }
    
}
