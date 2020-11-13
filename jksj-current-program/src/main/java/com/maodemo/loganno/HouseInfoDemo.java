package com.maodemo.loganno;

import java.util.Date;
import java.util.List;
import com.maodemo.loganno.anno.MyLogFieldAnno;

//@Data
public class HouseInfoDemo
{
    
    @MyLogFieldAnno(name = "商品名称")
    private String skuName;
    
    @MyLogFieldAnno(name = "创建时间")
    private Date createTime;
    
    @MyLogFieldAnno(name = "地址")
    private String address;
    
    @MyLogFieldAnno(name = "测试")
    private String dd;
    
    private String buyiyang;
    
    @MyLogFieldAnno(name = "儿子")
    private HouseInfoDemo sun;
    
    @MyLogFieldAnno(name = "列表")
    private List<String> stringList;
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getDd()
    {
        return dd;
    }
    
    public void setDd(String dd)
    {
        this.dd = dd;
    }
    
    public String getBuyiyang()
    {
        return buyiyang;
    }
    
    public void setBuyiyang(String buyiyang)
    {
        this.buyiyang = buyiyang;
    }
    
    public HouseInfoDemo getSun()
    {
        return sun;
    }
    
    public void setSun(HouseInfoDemo sun)
    {
        this.sun = sun;
    }
    
    public List<String> getStringList()
    {
        return stringList;
    }
    
    public void setStringList(List<String> stringList)
    {
        this.stringList = stringList;
    }
    
    public String getSkuName()
    {
        return skuName;
    }
    
    public void setSkuName(String skuName)
    {
        this.skuName = skuName;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
}
