package com.sian.translate.utlis;

import com.alibaba.fastjson.JSON;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.management.ueditor.enity.Ueditor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CommonUtlis {

    /**是否为会员 0 不是 1是 2过期会员**/
    public static final String VIPICON0 = "../../image/vip2.png";
    public static final String VIPICON1 = "../../image/vip1.png";
    public static final String VIPICON2 = "../../image/vip3.png";



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


    public static boolean  isNumerOrLetter(String content){

        for(int i=content.length();--i>=0;){

//            int chr=content.charAt(i);

            char ch = content.charAt(i);



            boolean isNum = (ch>='0' && ch<='9');
            boolean isLetter = (ch>='A' && ch<='Z'  ||  ch>='a' && ch<='z');


           if (!isNum && !isLetter){
               return false;
           }
        }

        return  true;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

//        return ipAddress;
        return "39.104.188.55";

    }

    public static String uploadImg(MultipartFile file,
                            HttpServletRequest request) throws IOException {
        Ueditor ueditor = new Ueditor();

        String path = request.getSession().getServletContext()
                .getRealPath("ueditor/jsp/upload/image");
        String ct = file.getContentType();
        String fileType = "";
        if (ct.indexOf("/") > 0) {
            fileType = ct.substring(ct.indexOf("/") + 1);
        }
        String fileName = UUID.randomUUID() + "." + fileType;
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        File targetFile2 = new File(path + "/" + fileName);
        if (!targetFile2.exists()) {
            targetFile2.createNewFile();
        }
        // 保存
        try {
            file.transferTo(targetFile2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ueditor.setState("SUCCESS");
        ueditor.setTitle(fileName);
        ueditor.setOriginal(fileName);
        ueditor.setUrl("http://39.104.188.55:8001/ueditor/jsp/upload/image" + File.separator + fileName);
        System.out.println(JSON.toJSONString(ueditor));
        return JSON.toJSONString(ueditor);
    }



}
