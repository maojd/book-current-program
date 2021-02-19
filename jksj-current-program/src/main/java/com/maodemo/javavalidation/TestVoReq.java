package com.maodemo.javavalidation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TestVoReq
{
    
//    private static final Logger logger = LoggerFactory.getLogger(TestVoReq.class);
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "id不能为空")
    private Long id;
    
    @NotEmpty(message = "productBrand不能为空")
    private String productBrand;
    
//    @Size(min = 3, max = 200, message = "目标市场最小3个字,最多200个字")
    @Size(min = 3, max = 200)
    private String targetMarket;
    
    @NotNull(message = "baseInfo不能为空")
    private BaseInfo baseInfo;
    
    public static class BaseInfo
    {
        
        @NotNull(message = "corporationName不能为null")
        private String corporationName;
        
        @NotEmpty(message = "corporationCode不能为空")
        private String corporationCode;
        
        public String getCorporationName()
        {
            return corporationName;
        }
        
        public void setCorporationName(String corporationName)
        {
            this.corporationName = corporationName;
        }
        
        public String getCorporationCode()
        {
            return corporationCode;
        }
        
        public void setCorporationCode(String corporationCode)
        {
            this.corporationCode = corporationCode;
        }
        
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getProductBrand()
    {
        return productBrand;
    }
    
    public void setProductBrand(String productBrand)
    {
        this.productBrand = productBrand;
    }
    
    public String getTargetMarket()
    {
        return targetMarket;
    }
    
    public void setTargetMarket(String targetMarket)
    {
        this.targetMarket = targetMarket;
    }
    
    public BaseInfo getBaseInfo()
    {
        return baseInfo;
    }
    
    public void setBaseInfo(BaseInfo baseInfo)
    {
        this.baseInfo = baseInfo;
    }
    
    /**
     * 计算体积和外包装体积
     * 
     * @createtime ： 2021年1月20日 下午2:40:00
     * @description 一句话描述
     * @since version 初始于版本
     */
//    private void compVolumeAndPackVolume()
//    {
//        if(StringUtil.isNotBlank(prodLength) || StringUtil.isNotBlank(prodWidth) || StringUtil.isNotBlank(prodHeight))
//        {
//            DecimalFormat decimalFormat = new DecimalFormat("##########.######");
//            volume = decimalFormat.format(Double.valueOf(prodLength) * Double.valueOf(prodWidth) * Double.valueOf(prodHeight));
//        }
//        
//        if(StringUtil.isNotBlank(packingLength) && StringUtil.isNotBlank(packingWidth) && StringUtil.isNotBlank(packingHeight))
//        {
//            DecimalFormat decimalFormat = new DecimalFormat("##########.######");
//            packingVolume = decimalFormat
//                    .format(Double.valueOf(packingLength) * Double.valueOf(packingWidth) * Double.valueOf(packingHeight));
//        }
//    }
    
    public static void main(String[] args)
    {
        TestVoReq req = new TestVoReq();
        BaseInfo b = new BaseInfo();
        b.setCorporationCode("");
        b.setCorporationName("");
        req.setBaseInfo(b);
        req.setProductBrand("");
        req.setTargetMarket("xx");
        System.out.println(MyJavaValidatorUtils.validate(req));
        System.out.println(MyJavaValidatorUtils.validate(req.getBaseInfo()));
//        ELManager
    }
}
