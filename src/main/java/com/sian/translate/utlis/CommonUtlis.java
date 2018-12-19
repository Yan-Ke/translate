package com.sian.translate.utlis;

import com.sian.translate.hint.enums.HintMessageEnum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtlis {

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     * * 
     * * @param nowTime 当前时间
     * * @param startTime 开始时间
     * * @param endTime 结束时间
     * * @return
     *  * @author jqlin
     *      
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }


        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);


        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);


        Calendar end = Calendar.getInstance();
        end.setTime(endTime);


        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    /****
     * 传入具体日期 ，返回具体日期增加一个月。
     * @param dt
     * @return date
     * @throws ParseException
     */
    public static Date subMonth(Date dt,int month) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, month);
        Date dt1 = rightNow.getTime();
        return dt1;
    }

    /******
     * 和当前日期比较大小只比较年月日
     * @param dt2
     * @return 0相等 1 大于当前时间  -1 小于当前时间
     */
    public static int compareNowDate(Date dt2) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = simpleDateFormat.format(new Date());

        Date dt1 = null;
        try {
            dt1 = simpleDateFormat.parse(sDate);
        } catch (ParseException e) {
        }

        try {
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    //返回的对象实现CodeEnum接口
    public static <T extends HintMessageEnum> T getByCode(Class<T> enumClass, Integer code) {
        for (T each : enumClass.getEnumConstants()) {
            if(each.getCode()==code){
                return each;
            }
        }
        return null;
    }
}
