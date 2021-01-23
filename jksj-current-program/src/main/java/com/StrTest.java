package com;

public class StrTest
{
    public static void main(String[] args)
    {
        String str = "05.3666";
        Double d = Double.valueOf(str);
        System.out.println(getNumberDecimalDigits(str));
    }
    
    private static int getNumberDecimalDigits(String str)
    {
        int dcimalDigits = 0;
        int indexOf = str.indexOf(".");
        if(indexOf > 0)
        {
            dcimalDigits = str.length() - 1 - indexOf;
        }
        return dcimalDigits;
    }
}
