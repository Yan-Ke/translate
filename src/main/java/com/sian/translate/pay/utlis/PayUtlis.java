package com.sian.translate.pay.utlis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PayUtlis {


    /****
     * 生成唯一订单id
     * @param userId 用户id
     * @return
     */
    public static String getOrderId(Integer userId ){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result + userId;
    }
}
