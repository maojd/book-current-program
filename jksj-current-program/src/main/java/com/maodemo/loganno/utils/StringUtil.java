package com.maodemo.loganno.utils;

public class StringUtil
{
    public static boolean isNotBlank(String str)
    {
        return !StringUtil.isBlank(str);
    }
    
    public static boolean isBlank(String str)
    {
        int strLen;
        if(str == null || (strLen = str.length()) == 0)
        {
            return true;
        }
        for(int i = 0; i < strLen; i++)
        {
            if((Character.isWhitespace(str.charAt(i)) == false))
            {
                return false;
            }
        }
        return true;
    }
    
}
