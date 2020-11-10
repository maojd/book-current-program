package com.damaojd.booknote.current.ch29;

//路由信息
public final class Router
{
    private final String ip;
    
    private final Integer port;
    
    private final String iface;
    
// 构造函数
    public Router(String ip,
            Integer port, String iface)
    {
        this.ip = ip;
        this.port = port;
        this.iface = iface;
    }
    
// 重写 equals 方法
    public boolean equals(Object obj)
    {
        if(obj instanceof Router)
        {
            Router r = (Router) obj;
            return iface.equals(r.iface)
                    &&
                    ip.equals(r.ip) &&
                    port.equals(r.port);
        }
        return false;
    }

    public String getIface()
    {
        return iface;
    }
    
    
    
//    public int hashCode()
//    {
//        // 省略 hashCode 相关代码
//    }
}