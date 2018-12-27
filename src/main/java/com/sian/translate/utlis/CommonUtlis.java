package com.sian.translate.utlis;

import com.sian.translate.hint.enums.HintMessageEnum;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CommonUtlis {


    /*****
     * 在传入日期上增加天数
     * @param nowTime 现在日期
     * @param day 增加的天数
     * @return
     */
    public static Date addDay(Date nowTime,int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);
        calendar.add(Calendar.DAY_OF_MONTH, day);

        return calendar.getTime();
    }


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

    /**
     * 将数组数据转换为实体类
     * 此处数组元素的顺序必须与实体类构造函数中的属性顺序一致
     *
     * @param list           数组对象集合
     * @param clazz          实体类
     * @param <T>            实体类
     * @param model          实例化的实体类
     * @return 实体类集合
     */
    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz, Object model) {
        List<T> returnList = new ArrayList<T>();
        if (list.isEmpty()) {
            return returnList;
        }
        //获取每个数组集合的元素个数
        Object[] co = list.get(0);

        //获取当前实体类的属性名、属性值、属性类别
        List<Map> attributeInfoList = getFiledsInfo(model);
        //创建属性类别数组
        Class[] c2 = new Class[attributeInfoList.size()];
        //如果数组集合元素个数与实体类属性个数不一致则发生错误
        if (attributeInfoList.size() != co.length) {
            return returnList;
        }
        //确定构造方法
        for (int i = 0; i < attributeInfoList.size(); i++) {
            c2[i] = (Class) attributeInfoList.get(i).get("type");
        }
        try {
            for (Object[] o : list) {
                Constructor<T> constructor = clazz.getConstructor(c2);
                    returnList.add(constructor.newInstance(o));
            }
        } catch (Exception ex) {
            log.error("实体数据转化为实体类发生异常：异常信息：{}", ex.getMessage());
            return returnList;
        }
        return returnList;
    }
    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     *
     * @param model 实体类
     * @return list集合
     */
    private static List<Map> getFiledsInfo(Object model) {
        Field[] fields = model.getClass().getDeclaredFields();
        List<Map> list = new ArrayList(fields.length);
        Map infoMap = null;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap(3);
            infoMap.put("type", fields[i].getType());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), model));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName 属性名
     * @param modle     实体类
     * @return 属性值
     */
    private static Object getFieldValueByName(String fieldName, Object modle) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = modle.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(modle, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

}
