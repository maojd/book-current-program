package com.maodemo.loganno;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import com.maodemo.loganno.anno.MyLogFieldAnno;
import com.maodemo.loganno.anno.MyLogModifiedPropertyInfo;
import com.maodemo.loganno.anno.MyLogPropertyModelInfo;
import com.maodemo.loganno.utils.DateUtils;
import com.maodemo.loganno.utils.StringUtil;

public class BusiLogAnnoStaticUtils<T>
{
//    private static final Logger LOGGER = LoggerFactory.getLogger(BusiLogAnnoStaticUtils.class);
    
    public static <T> String getLogDescByObj(T oldSkuBo, T newSkuBo, String... ignoreProperties)
    {
        List<MyLogModifiedPropertyInfo> viomiInfoLogModifiedPropertyInfos = new ArrayList<>();
        try
        {
            viomiInfoLogModifiedPropertyInfos = getDifferentProperty(oldSkuBo, newSkuBo, "", "",
                    viomiInfoLogModifiedPropertyInfos, ignoreProperties);
//            System.out.println(viomiInfoLogModifiedPropertyInfos);
        }
        catch (Exception e)
        {
//            LOGGER.error(" getLogDescBySkuDetailBo error.  e.message {}", e.getMessage(), e);
        }
        return getLogDescByCompareList(viomiInfoLogModifiedPropertyInfos);
    }
    
    public static String getLogDescByCompareList(List<MyLogModifiedPropertyInfo> busiLogcompareFiledResList)
    {
        StringBuffer descRes = new StringBuffer();
        if(null == busiLogcompareFiledResList)
        {
            return descRes.toString();
        }
        
        busiLogcompareFiledResList.forEach(n -> {
            String filedName = n.getPropertyNameDesc();
            Object oldValueObj = n.getOldValue() == null ? "" : n.getOldValue();
            Object newValueObj = n.getNewValue() == null ? "" : n.getNewValue();
            
            String oldValueObjStr = conver2StrValue(oldValueObj).toString();
            String newValueObjStr = conver2StrValue(newValueObj).toString();
            
            if(StringUtil.isBlank(oldValueObjStr))
                oldValueObjStr = "空值";
            if(StringUtil.isBlank(newValueObjStr))
                newValueObjStr = "空值";
            
            String nDesc = String.format("\"%s\"字段，将【%s】改为【%s】；\r\n", filedName, oldValueObjStr, newValueObjStr);
            descRes.append(nDesc);
        });
        return descRes.toString();
    }
    
    /**
     * 比较两个对象属性值是否相同
     * 如果不同返回修改过的属性信息
     *
     * @param oldObj
     * @param newObj
     * @param ignoreProperties
     * @param <T> preDescName前面解析名， preproName前面属性名
     * @return 修改过的属性字段
     * @throws Exception
     */
    public static <T> List<MyLogModifiedPropertyInfo> getDifferentProperty(T oldObj, T newObj, String preDescName,
            String preproName, List<MyLogModifiedPropertyInfo> ViomiInfoLogModifiedPropertyInfos,
            String... ignoreProperties) throws Exception
    {
        
        if(oldObj == null && newObj != null)
        {
            Constructor<?> cons = newObj.getClass().getConstructor();
            oldObj = (T) cons.newInstance();
        }
        else if(oldObj != null && newObj == null)
        {
            newObj = (T) oldObj.getClass().getConstructor().newInstance();
        }
        
        if(oldObj != null && newObj != null)
        {
            Class<?> objClass = oldObj.getClass();
            Field field = null;
            MyLogFieldAnno ViomiInfoLogField = null;
            // 比较全部字段
            if(ignoreProperties == null || ignoreProperties.length > 0)
            {
                if(oldObj.equals(newObj))
                {
                    return Collections.emptyList();
                }
            }
            List<MyLogPropertyModelInfo> oldObjectPropertyValue = getObjectPropertyValue(oldObj, ignoreProperties);
//            if(!CollectionUtils.isEmpty(oldObjectPropertyValue))
            if(null != oldObjectPropertyValue && oldObjectPropertyValue.size() > 0)
            {
                
                List<MyLogPropertyModelInfo> newObjectPropertyValue = getObjectPropertyValue(newObj, ignoreProperties);
                Map<String, Object> objectMap = new HashMap<>(newObjectPropertyValue.size());
                // 获取新对象所有属性值
                for(MyLogPropertyModelInfo ViomiInfoLogPropertyModelInfo : newObjectPropertyValue)
                {
                    String propertyName = ViomiInfoLogPropertyModelInfo.getPropertyName();
                    Object value = ViomiInfoLogPropertyModelInfo.getValue();
                    objectMap.put(propertyName, value);
                }
                
                for(MyLogPropertyModelInfo ViomiInfoLogPropertyModelInfo : oldObjectPropertyValue)
                {
                    String propertyName = ViomiInfoLogPropertyModelInfo.getPropertyName();
                    Object value = ViomiInfoLogPropertyModelInfo.getValue();
                    if(objectMap.containsKey(propertyName))
                    {
                        Object newValue = objectMap.get(propertyName);
                        if(value != null && newValue != null)
                        {
                            if(!value.equals(newValue))
                            {
                                isbreak(oldObj, newObj, ViomiInfoLogPropertyModelInfo,
                                        ViomiInfoLogModifiedPropertyInfos,
                                        preDescName, preproName, value, newValue);
                            }
                        }
                        else if(value != null && newValue == null || value == null && newValue != null)
                        {
                            // 是否存在于所有父类的属性中
//                            field = isexist(oldObj, propertyName);
//                            if (field == null) {
//                                field = objClass.getDeclaredField(propertyName);// 获取权限提升
//                            }
//                            Class<?> type = ViomiInfoLogPropertyModelInfo.getReturnType();
//                            ViomiInfoLogField = field.getAnnotation(ViomiInfoLogField.class);
//                            if (type == null) {
//                                //list<obj>类型直接跳过
//                                break;
//                            } else if(!isNeed(ViomiInfoLogField)){
//                                ///无注解或过滤字段跳过
//                                break;
//                            }else if (isBaseType(type, true)) {
//                                String descName = ViomiInfoLogField.name();
//                                AssertUtils.notEmpty(descName,"ViomiInfoLogField的name属性不能为空");
//                                ViomiInfoLogModifiedPropertyInfo.setPropertyNameDesc(preDescName + descName);
//                                // 如果前面有目录则拼接
//                                if (StringUtil.isNotBlank(preproName))
//                                    propertyName = preproName + "." + propertyName;
//                                ViomiInfoLogModifiedPropertyInfo.setPropertyName(propertyName);
//                                ViomiInfoLogModifiedPropertyInfo.setOldValue(value);
//                                ViomiInfoLogModifiedPropertyInfo.setNewValue(newValue);
//                                ViomiInfoLogModifiedPropertyInfos.add(ViomiInfoLogModifiedPropertyInfo);
//
//                            } else {
//                                // 非基本数据类型特殊处理
//                                if (field.isAnnotationPresent(ViomiInfoLogField.class)) {
//                                    ViomiInfoLogField = field.getAnnotation(ViomiInfoLogField.class);
//                                }
//                                field.setAccessible(true); // 私有属性必须设置访问权限
//                                getDifferentProperty(field.get(oldObj), field.get(newObj),
//                                        ViomiInfoLogField.name() + "", field.getName(), ViomiInfoLogModifiedPropertyInfos);
//                            }
                            isbreak(oldObj, newObj, ViomiInfoLogPropertyModelInfo,
                                    ViomiInfoLogModifiedPropertyInfos,
                                    preDescName, preproName, value, newValue);
                        }
                    }
                    // 重置
                    ViomiInfoLogField = null;
                }
                return ViomiInfoLogModifiedPropertyInfos;
            }
        }
        if(oldObj == null)
            return null;
        return new ArrayList<MyLogModifiedPropertyInfo>();
    }
    
    /**
     * 通过反射获取对象的属性名称、getter返回值类型、属性值等信息
     *
     * @param obj
     * @param ignoreProperties
     * @param <T>
     * @return
     */
    public static <T> List<MyLogPropertyModelInfo> getObjectPropertyValue(T obj, String... ignoreProperties)
    {
        if(obj != null)
        {
            
            Class<?> objClass = obj.getClass();
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(objClass);
            List<MyLogPropertyModelInfo> modelInfos = new ArrayList<>(propertyDescriptors.length);
            
            List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
            for(PropertyDescriptor propertyDescriptor : propertyDescriptors)
            {
                Method readMethod = propertyDescriptor.getReadMethod();
                String name = propertyDescriptor.getName();
                if(readMethod != null && (ignoreList == null || !ignoreList.contains(name)))
                {
                    Object invoke = null;
                    Class<?> returnType = readMethod.getReturnType();
                    try
                    {
//                        if(isBaseType(returnType))
                        invoke = readMethod.invoke(obj);
                        MyLogPropertyModelInfo ViomiInfoLogPropertyModelInfo = new MyLogPropertyModelInfo();
                        ViomiInfoLogPropertyModelInfo.setPropertyName(name);
                        ViomiInfoLogPropertyModelInfo.setValue(invoke);
                        if(!name.equals("class"))
                        {
                            // 是否存在于所有父类的属性中
                            Field field = isexist(obj, name);
                            if(field == null)
                            {
                                field = objClass.getDeclaredField(name);// 获取权限提升
                            }
                            Class<?>[] parameterizedType = getParameterizedType(field);
                            if(isList(returnType)
                                    && parameterizedType != null &&
                                    !isBaseType(parameterizedType[0], true))
                            {
                                ViomiInfoLogPropertyModelInfo.setReturnType(null);
                            }
                            else
                            {
                                // list<obj>用null标记
                                ViomiInfoLogPropertyModelInfo.setReturnType(returnType);
                            }
                        }
                        else
                        {
                            ViomiInfoLogPropertyModelInfo.setReturnType(returnType);
                        }
                        modelInfos.add(ViomiInfoLogPropertyModelInfo);
                    }
                    catch (IllegalAccessException | InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
                    catch (NoSuchFieldException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            return modelInfos;
        }
        return Collections.emptyList();
    }
    
    public static boolean isBaseType(Class className, boolean incString)
    {
        if(incString && className.equals(String.class))
        {
            return true;
        }
        return className.equals(Integer.class)
                ||
                className.equals(int.class) ||
                className.equals(Byte.class) ||
                className.equals(byte.class) ||
                className.equals(Long.class) ||
                className.equals(long.class) ||
                className.equals(Double.class) ||
                className.equals(double.class) ||
                className.equals(Float.class) ||
                className.equals(float.class) ||
                className.equals(Character.class) ||
                className.equals(char.class) ||
                className.equals(Short.class) ||
                className.equals(short.class) ||
                className.equals(Boolean.class) ||
                className.equals(boolean.class) ||
                className.equals(List.class) ||
                className.equals(Map.class) ||
                className.equals(Set.class) ||
                className.equals(Date.class) ||
                className.equals(Array.class);
        
    }
    
    public static boolean isCollection(Class className)
    {
        return className.equals(List.class)
                ||
                className.equals(Map.class) ||
                className.equals(Set.class) ||
                className.equals(Array.class);
    }
    
    private static Object conver2StrValue(Object valueObj)
    {
        if(null != valueObj)
        {
            if(valueObj instanceof Date)
            {
                Date date = (Date) valueObj;
                valueObj = DateUtils.format(date, DateUtils.SECOND_PATTERN);
            }
        }
        return valueObj;
    }
    
    // 判断该属性是否存在于父类,存在则返回该属性
    public static Field isexist(Object bean, final String propertyName)
    {
        Class<?> clazz = bean.getClass();
        List<Field> fields = new ArrayList<>();
        for(; clazz != Object.class; clazz = clazz.getSuperclass())
        {// 向上循环 遍历父类
            Field[] field = clazz.getDeclaredFields();
            for(Field f : field)
            {
                f.setAccessible(true);
                fields.add(f);
            }
        }
        List<Field> list = fields.stream().filter(a -> a.getName().equals(propertyName)).collect(Collectors.toList());
//        return CollectionUtils.isEmpty(list) ? null : list.get(0);
        return null == list ? null : list.get(0);
    }
    
    public static boolean isList(Class className)
    {
        return className.equals(List.class);
    }
    
    // 判断list的泛型
    public static Class<?>[] getParameterizedType(Field f)
    {
// 获取f字段的通用类型
        Type fc = f.getGenericType(); // 关键的地方得到其Generic的类型
// 如果不为空并且是泛型参数的类型
        if(fc != null && fc instanceof ParameterizedType)
        {
            ParameterizedType pt = (ParameterizedType) fc;
            
            Type[] types = pt.getActualTypeArguments();
            
            if(types != null && types.length > 0)
            {
                Class<?>[] classes = new Class<?>[types.length];
                for(int i = 0; i < classes.length; i++)
                {
                    classes[i] = (Class<?>) types[i];
                }
                return classes;
            }
        }
        return null;
    }
    
    public static boolean isNeed(MyLogFieldAnno viomiInfoLogField)
    {
        if(viomiInfoLogField == null)
            return false;
        String need = viomiInfoLogField.need();
        String descName = viomiInfoLogField.name();
        return !need.contains(descName);
    }
    
    // 抽取方法
    public static void isbreak(Object oldObj, Object newObj, MyLogPropertyModelInfo logInfo,
            List<MyLogModifiedPropertyInfo> ViomiInfoLogModifiedPropertyInfos,
            String preDescName, String preproName,
            Object value, Object newValue) throws Exception
    {
        String propertyName = logInfo.getPropertyName();
        Class<?> objClass = oldObj.getClass();
        Field field = isexist(oldObj, propertyName);
        MyLogFieldAnno ViomiInfoLogField = null;
        MyLogModifiedPropertyInfo ViomiInfoLogModifiedPropertyInfo = new MyLogModifiedPropertyInfo();
        // 是否存在于所有父类的属性中
        if(field == null)
        {
            field = objClass.getDeclaredField(propertyName);// 获取权限提升
        }
        Class<?> type = logInfo.getReturnType();
        ViomiInfoLogField = field.getAnnotation(MyLogFieldAnno.class);
        if(type == null)
        {
            // list<obj>类型直接跳过
            return;
        }
        else if(!isNeed(ViomiInfoLogField))
        {
            /// 无注解或过滤字段跳过
            return;
        }
        else if(isBaseType(type, true))
        {
            String descName = ViomiInfoLogField.name();
            
            if(StringUtil.isBlank(descName))
            {
                throw new Exception("LogField的name属性不能为空");
            }
            
            if(StringUtil.isNotBlank(preDescName))
            {
                ViomiInfoLogModifiedPropertyInfo.setPropertyNameDesc(preDescName + descName);
            }
            else
            {
                ViomiInfoLogModifiedPropertyInfo.setPropertyNameDesc(descName);
            }
            // 如果前面有目录则拼接
            if(StringUtil.isNotBlank(preproName))
                propertyName = preproName + "." + propertyName;
            ViomiInfoLogModifiedPropertyInfo.setPropertyName(propertyName);
            ViomiInfoLogModifiedPropertyInfo.setOldValue(value);
            ViomiInfoLogModifiedPropertyInfo.setNewValue(newValue);
            ViomiInfoLogModifiedPropertyInfos.add(ViomiInfoLogModifiedPropertyInfo);
            
        }
        else
        {
            // 非基本数据类型特殊处理
            field.setAccessible(true); // 私有属性必须设置访问权限
            getDifferentProperty(field.get(oldObj), field.get(newObj),
                    ViomiInfoLogField.name() + "", field.getName(), ViomiInfoLogModifiedPropertyInfos);
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        List<String> list1 = new ArrayList<>();
        list1.add("旧的8748");
        list1.add("旧的8888888");
        List<String> list2 = new ArrayList<>();
        list2.add("旧的8902");
        HouseInfoDemo houseInfo = new HouseInfoDemo();
        houseInfo.setAddress("旧的test1");
        houseInfo.setDd("旧的aa");
        houseInfo.setBuyiyang("旧的不一样");
        houseInfo.setStringList(list1);
        houseInfo.setSkuName("商品名称旧的");
        
        HouseInfoDemo houseInfo2 = new HouseInfoDemo();
        houseInfo2.setAddress("新的test2");
        houseInfo2.setDd("dd");
//        houseInfo.setBuyiyang("不一样");
        houseInfo2.setStringList(list2);
        
        HouseInfoDemo houseInfo3 = new HouseInfoDemo();
        houseInfo3.setAddress("longgong");
        HouseInfoDemo houseInfo4 = new HouseInfoDemo();
        houseInfo4.setAddress("tiangong");
        houseInfo3.setDd("gg");
        houseInfo4.setDd("cc");
        houseInfo.setSun(houseInfo3);
        houseInfo2.setSun(houseInfo4);
        houseInfo2.setCreateTime(new Date());
        // 不改变值
        houseInfo2.setAddress("test1");
        
//        System.out.println("-------------------调用这两行代码只是为了 打印出来--------------------------------------");
//        List<ViomiInfoLogModifiedPropertyInfo> compareList = new ArrayList<>();
//        compareList = getDifferentProperty(houseInfo, houseInfo2, "", "", compareList);
//        System.out.println(getLogDescByCompareList(compareList));
        
        //
        System.out.println("--------------只调用这一行代码也可以-----");
        System.out.println(getLogDescByObj(houseInfo, houseInfo2));
    }
}
