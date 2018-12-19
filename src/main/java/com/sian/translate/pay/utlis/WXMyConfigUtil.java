package com.sian.translate.pay.utlis;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.InputStream;

public class WXMyConfigUtil implements WXPayConfig {

    private byte[] certData;

//    /**应用id**/
//    @Value("${pay.weixin.appid}")
//    private String appid;
//
//    /**商户号**/
//    @Value("${pay.weixin.mchId}")
//    private String mchId;
//
//    /**api密钥**/
//    @Value("${pay.weixin.key}")
//    private String key;
//
//    /**连接超时时间**/
//    @Value("${pay.weixin.connectTimeoutMs}")
//    private int connectTimeoutMs;
//
//    /**读超时时间**/
//    @Value("${pay.weixin.mchId}")
//    private int readTimeoutMs;


    public WXMyConfigUtil() throws Exception {
//        String certPath = "证书地址";//从微信商户平台下载的安全证书存放的目录,退款使用
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
    }

    @Override
    public String getAppID() {
        return "wxd678efh567hg6787";
    }

    @Override
    public String getMchID() {
        return "1230000109";
    }

    @Override
    public String getKey() {
        return "2016092200571547";
    }

    @Override
    public InputStream getCertStream() {
//        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
//        return certBis;
        return  null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

}
