package com.damaojd.booknote.current.ch29;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Ch29RouterTable
{
    // key router.iface, value routerSet
    ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> rt = new ConcurrentHashMap<>();
    
    public Set<Router> get(String iface)
    {
        return rt.get(iface);
    }
    
//  // 增加路由
    public void add(Router route)
    {
//        CopyOnWriteArraySet<Router> set = rt.get(route.getIface());
//        if(null == set)
//        {
//            set = new CopyOnWriteArraySet<>();
//        }
//        set.add(route);
//        rt.put(route.getIface(), set);
        
        Set<Router> set2 = rt.computeIfAbsent(
                route.getIface(), r -> new CopyOnWriteArraySet<>());
        set2.add(route);
    }
    
    public void remove(Router router)
    {
        Set<Router> set = rt.get(router.getIface());
        if(set != null)
        {
            set.remove(router);
        }
    }
    
    public static void main(String[] args)
    {
        Ch29RouterTable t = new Ch29RouterTable();
        Router r1 = new Router("182.23", 123, "sku/pageList");
        Router r2 = new Router("182.24", 123, "sku/pageList");
        
        t.add(r1);
        t.add(r1);
        t.add(r2);
        System.out.println(t.rt);
        
        t.remove(r1);
        System.out.println(t.rt);
        
//        目前 Copy-on-Write 在 Java 并发编程领域知名度不是很高，很多人都在无意中把它忽视了，
//        但其实 Copy-on-Write 才是最简单的并发解决方案。它是如此简单，
//        以至于 Java 中的基本数据类型 String、Integer、Long 等都是基于 Copy-on-Write 方案实现的。
        
//        Copy-on-Write 是一项非常通用的技术方案，在很多领域都有着广泛的应用。
//        不过，它也有缺点的，那就是消耗内存，每次修改都需要复制一个新的对象出来，
//        好在随着自动垃圾回收（GC）算法的成熟以及硬件的发展，这种内存消耗已经渐渐可以接受了。
//        所以在实际工作中，如果写操作非常少，那你就可以尝试用一下 Copy-on-Write，效果还是不错的。
    }
    
}
