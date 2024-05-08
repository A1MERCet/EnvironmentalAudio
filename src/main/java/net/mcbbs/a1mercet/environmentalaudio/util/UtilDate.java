package net.mcbbs.a1mercet.environmentalaudio.util;

import java.util.Date;

public class UtilDate
{
    public static long getSecond(long var)
    {
        return var/1000L;
    }
    public static long getMinute(long var)
    {
        return var/1000L/60L;
    }
    public static long gethour(long var)
    {
        return var/1000L/60L/60L;
    }
    public static long getDay(long var)
    {
        return var/1000L/60L/60L/24L;
    }

    public static long tickToSecond(long tick)
    {
        return tick/20L*1000L;
    }
    public static long tickToMinute(long tick)
    {
        return tick/20L*1000L*60L;
    }
    public static long tickTohour(long tick)
    {
        return tick/20L*1000L*60L*60L;
    }
    public static long tickToDay(long tick)
    {
        return tick/20L*1000L*60L*60L*24L;
    }

    public static long toSecond(long var)
    {
        return 1000L*var;
    }
    public static long toMinute(long var)
    {
        return 1000L*60L*var;
    }
    public static long toHour(long var)
    {
        return 1000L*60L*60L*var;
    }
    public static long toDay(long var)
    {
        return 1000L*60L*60L*24L*var;
    }

    public static long getSecond(Date d1,Date d2)
    {
        long l1 = d1.getTime(),l2 = d2.getTime();
        long var = Math.max(l1,l2)-Math.min(l1,l2);
        return getSecond(var);
    }
    public static long getMinute(Date d1,Date d2)
    {
        long l1 = d1.getTime(),l2 = d2.getTime();
        long var = Math.max(l1,l2)-Math.min(l1,l2);
        return getMinute(var);
    }
    public static long gethour(Date d1,Date d2)
    {
        long l1 = d1.getTime(),l2 = d2.getTime();
        long var = Math.max(l1,l2)-Math.min(l1,l2);
        return gethour(var);
    }
    public static long getDay(Date d1,Date d2)
    {
        long l1 = d1.getTime(),l2 = d2.getTime();
        long var = Math.max(l1,l2)-Math.min(l1,l2);
        return getDay(var);
    }

    public static String formatTime(long v)
    {
        long day = UtilDate.getDay(v);
        long week = day/7L;
        long month = day/30L;
        long hour = UtilDate.gethour(v);
        long minute = UtilDate.getMinute(v);
        if(month>0)      return month    + "月";
        else if(week>0)  return week     + "周";
        else if(day>0)   return day      + "日";
        else if(hour>0)  return hour     + "时";
        else if(minute>0)return minute   + "分";
        else return UtilDate.getSecond(v)+ "秒";
    }
    public static String formatTimeFull(long v)
    {
        long day = UtilDate.getDay(v);
        long week = day/7L;
        long month = day/30L;
        long hour = UtilDate.gethour(v);
        long minute = UtilDate.getMinute(v);
        long sec = UtilDate.getSecond(v);

        StringBuilder b = new StringBuilder();

        if(month>0)      b.append(month).append("月");
        if(week%7>0)     b.append(week%7).append("周");
        if(day%30>0)     b.append(day%30).append("日");
        if(hour%24>0)    b.append(hour%24).append("时");
        if(minute%60>0)  b.append(minute%60).append("分");
        if(sec%60>0)     b.append(sec%60).append("秒");
        return b.toString();
    }

    public static Date afterSecond(Date d1, long var)
    {
        return new Date(d1.getTime()+toSecond(var));
    }
    public static Date afterMinute(Date d1, long var)
    {
        return new Date(d1.getTime()+toMinute(var));
    }
    public static Date afterHour(Date d1, long var)
    {
        return new Date(d1.getTime()+toHour(var));
    }
    public static Date afterDay(Date d1, long var)
    {
        return new Date(d1.getTime()+toDay(var));
    }

    public static Date beforeSecond(Date d1, long var)
    {
        return new Date(d1.getTime()-toSecond(var));
    }
    public static Date beforeMinute(Date d1, long var)
    {
        return new Date(d1.getTime()-toMinute(var));
    }
    public static Date beforeHour(Date d1, long var)
    {
        return new Date(d1.getTime()-toHour(var));
    }
    public static Date beforeDay(Date d1, long var)
    {
        return new Date(d1.getTime()-toDay(var));
    }

}
