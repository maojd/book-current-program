package com.damaojd.booknote.current.ch43.stm;

public class VersionedRef<T>
{
    T value;
    
    long version;
    
    public VersionedRef(T value, long version)
    {
        super();
        this.value = value;
        this.version = version;
        System.out.println(String.format(" new  VersionedRef  value [%s] version[%s]", value, version));
    }
    
}
