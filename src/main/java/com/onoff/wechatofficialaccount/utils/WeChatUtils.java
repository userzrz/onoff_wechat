package com.onoff.wechatofficialaccount.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
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
public class WeChatUtils {

    //验证微信请求TOKEN
    public static final String TOKEN = "zhurongzheng";
    //公众号APPID
    public static final String APPID = "wx66b425717de760ce";
    //公众号APPSECRET
    public static final String APPSECRET = "2ebd982fc738d5ed36a04e9764cd0092";
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
