package com.damaojd.booknote.current.ch43.stm;

import org.apache.log4j.Logger;

public class AccountTest
{
    private static Logger log = Logger.getLogger(AccountTest.class);
    
    public static void main(String[] args)
    {
        Account accfrom = new Account(500);
        Account accTo = new Account(0);
        
        accfrom.transfer(accTo, 1);
        log.info("accFrom剩余 " + accfrom.getBalance2());
        log.info("accTo剩余 " + accTo.getBalance2());
    }
}
