package com.onoff.wechatofficialaccount.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/13 12:09
 * @VERSION 1.0
 **/
@Slf4j
public class WeChatUtils {

    //验证微信请求TOKEN
    public static final String TOKEN = "zhurongzheng";
    //订阅号APPID  wxbcce35eda766550a
    public static final String APPID = "wxbcce35eda766550a";
    //订阅号APPSECRET  040924f25ead5851bcf39b050b5711d3
    public static final String APPSECRET = "040924f25ead5851bcf39b050b5711d3";
    //服务号APPID
    public static final String FWAPPID ="wx10ace3211b9a7334";
    //服务号APPSECRET
    public static final String FWAPPSECRET = "c750a471a54ffb2672d627c7e0bbca5c";
    //用于存储access_token
    public static String access_token;
    //用于存储access_token过期时间
    public static long expireTime;


    /**
     * 请求微信接口获取token
     */
    public static void getToken() {
        //公众号access_token接口
        String GET_ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String url=GET_ACCESS_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET",APPSECRET);
        String tokenStr= get(url);
        JSONObject jsonObject = JSONObject.parseObject(tokenStr);
        access_token= jsonObject.getString("access_token");
        //返回有效时间单位秒
        String expireIn = jsonObject.getString("expires_in");
        //计算access_token过期时间
        expireTime=System.currentTimeMillis()+(Integer.parseInt(expireIn)-60)*1000;
        log.info("------------------->生成一次TOKEN");
    }

    /**
     * 向指定的地址发送一个post请求，带着data数据
     *
     * @param url
     * @param data
     */
    public static String post(String url, String data) {
        try {
            URL urlObj = new URL(url);
            URLConnection connection = urlObj.openConnection();
            // 要发送数据出去，必须要设置为可发送数据状态
            connection.setDoOutput(true);
            // 获取输出流
            OutputStream os = connection.getOutputStream();
            // 写出数据
            os.write(data.getBytes());
            os.close();
            // 获取输入流
            InputStream is = connection.getInputStream();
            byte[] b = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向指定的地址发送get请求
     *
     * @param url
     */
    public static String get(String url) {
        try {
            URL urlObj = new URL(url);
            // 开连接
            URLConnection connection = urlObj.openConnection();
            InputStream is = connection.getInputStream();
            byte[] b = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * sha1加密
     *
     * @param src
     * @return
     */
    public static String sha1(String src) {
        try {
            //获取一个加密对象
            MessageDigest md = MessageDigest.getInstance("sha1");
            //加密
            byte[] digest = md.digest(src.getBytes());
            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuffer sb = new StringBuffer();
            //处理加密结果
            for (byte b : digest) {
                sb.append(chars[(b >> 4) & 15]);
                sb.append(chars[b & 15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
