package com.maodemo.loganno.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
//    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
    
    public static final String SECOND_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    public static final String SECOND_PATTERN1 = "yyyy.MM.dd HH:mm:ss";
    
    public static final String MINUTE_PATTERN = "yyyy.MM.dd HH:mm";
    
    public static final String MINUTE_PATTERN1 = "yyyy-MM-dd HH:mm";
    
    public static final String DAY_PATTERN = "yyyy-MM-dd";
    
    public static final String DAY_PATTERN2 = "yyyy.MM.dd";
    
    public static final String DAY_PATTERN_NOINTERVAL = "yyyyMMdd";
    
    public static void main(String[] args) throws Exception
    {
        
        // Date d1 = parseDate("2016-01", "yyyy-MM");
        // Date d2 = parseDate("2016-04-14", "yyyy-MM-dd");
        // System.out.println(monthDiff(d1, d2));
        // Date date = getMonthBeginTime(getMiPaasNowChinaTime());
        // System.out.println(date.getTime());
        // System.out.println(Calendar.getInstance(Locale.CHINA).getTime());
        // System.out.println(Calendar.getInstance(Locale.CHINA).getTime());
        System.out.println(format(new Date(), "yyyy-MM-dd hh:mm:ss"));
        Date date2 = new Date(1465292886120L);
        
    }
    
    /**
     * @param date
     *            时间对象
     * @param timePattern
     *            时间模式字符串
     * @return
     */
    public static String format(Date date, String timePattern)
    {
        if(date != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
            return sdf.format(date);
        }
        return null;
    }
}
