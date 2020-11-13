package com.maodemo.loganno.anno;

import java.io.Serializable;

public class MyLogModifiedPropertyInfo implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    // 对应的属性名
    private String propertyName;
    
    // 对应的属性名描述
    private String propertyNameDesc;
    
    // 未修改之前的值
    private Object oldValue;
    
    // 修改后的值
    private Object newValue;
    
    public String getPropertyName()
    {
        return propertyName;
    }
    
    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }
    
    public String getPropertyNameDesc()
    {
        return propertyNameDesc;
    }
    
    public void setPropertyNameDesc(String propertyNameDesc)
    {
        this.propertyNameDesc = propertyNameDesc;
    }
    
    public Object getOldValue()
    {
        return oldValue;
    }
    
    public void setOldValue(Object oldValue)
    {
        this.oldValue = oldValue;
    }
    
    public Object getNewValue()
    {
        return newValue;
    }
    
    public void setNewValue(Object newValue)
    {
        this.newValue = newValue;
    }
    
}