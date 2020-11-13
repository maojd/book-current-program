package com.maodemo.loganno.anno;

public class MyLogPropertyModelInfo
{
    // 属性名
    private String propertyName;
    
    // 属性值
    private Object value;
    
    // 返回值类型
    private Class<?> returnType;
    
    public String getPropertyName()
    {
        return propertyName;
    }
    
    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }
    
    public Object getValue()
    {
        return value;
    }
    
    public void setValue(Object value)
    {
        this.value = value;
    }
    
    public Class<?> getReturnType()
    {
        return returnType;
    }
    
    public void setReturnType(Class<?> returnType)
    {
        this.returnType = returnType;
    }
    
}