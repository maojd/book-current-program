package com.damaojd.booknote.current.ch43;

public class UnsafeAccount
{
    
    // 余额
    private long balance;
    
    public long getBalance()
    {
        return balance;
    }
    
    public void setBalance(long balance)
    {
        this.balance = balance;
    }
    
    public UnsafeAccount()
    {
    }
    
    public UnsafeAccount(long balance)
    {
        super();
        this.balance = balance;
    }
    
    public void transfer(UnsafeAccount to, long amount)
    {
//        if(this.balance >= amount)
//        {
        this.balance -= amount;
        to.balance += amount;
//        }
    }
    
    public static void main(String[] args)
    {
        UnsafeAccount acc1 = new UnsafeAccount(100L);
        UnsafeAccount acc2 = new UnsafeAccount(200L);
        acc1.transfer(acc2, 20L);
        System.out.println(acc1.getBalance());
        System.out.println(acc2.getBalance());
    }
}
