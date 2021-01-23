package com.damaojd.booknote.current.ch43.stm;

public final class VersionedRef<T>
{
    final T value;
    
    final long version;
    
    public VersionedRef(T value, long version)
    {
        super();
        this.value = value;
        this.version = version;
        System.out.println(String.format(" new  VersionedRef  value [%s] version[%s]", value, version));
    }
    
}
