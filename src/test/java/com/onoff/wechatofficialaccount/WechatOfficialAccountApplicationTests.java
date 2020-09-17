package com.onoff.wechatofficialaccount;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.onoff.wechatofficialaccount.entity.DO.Cycle;
import com.onoff.wechatofficialaccount.entity.DO.WeekLeaderboard;
import com.onoff.wechatofficialaccount.entity.Https;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.mapper.DAO.BAMDao;
import com.onoff.wechatofficialaccount.service.BAMService;
import com.onoff.wechatofficialaccount.service.WeChatService;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import com.onoff.wechatofficialaccount.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Slf4j
@SpringBootTest
class WechatOfficialAccountApplicationTests {

//    @Autowired
//    WeChatService service;
//
//    @Autowired
//    BAMService bamService;

    @Autowired
    BAMDao dao;

    //    //&next_openid=
//    @Test
//    public void test() {
//        String http = Https.userListHttps;
//        http = http.replace("ACCESS_TOKEN", service.getAccessToken()).replace("NEXT_OPENID", "oQpgp0-KEkcQqU3aJqq2je6TbN7Y");
//        log.info(http);
//        String result = WeChatUtils.get(http);
//        JSONObject jsonObject = JSON.parseObject(result);
//        String total = jsonObject.getString("total");
//        String count = jsonObject.getString("count");
//        String next_openid = jsonObject.getString("next_openid");
//        String openid = jsonObject.getString("data");
//        jsonObject=JSON.parseObject(openid);
//        JSONArray array=jsonObject.getJSONArray("openid");
//        log.info("total"+total+";count"+count+";next_openid"+next_openid);
//        for (int i=0;i<array.size();i++){
//            service.verifyUser(array.get(i).toString());
//            log.info(i+"");
//        }
//        log.info("total"+total+";count"+count+";next_openid"+next_openid);
//    }
//
    @Test
    public void test2() {


    }
//
//    public static void recursion(File[] fs, List<String> list) // 递归得所有子目录的.mp3文件.
//    {
//        for (File f : fs)
//        {
//            if (f.isDirectory())
//            {
//                recursion(f.listFiles(), list);
//            }
//            else
//            {
//                if (f.getName().endsWith(".mp3"))
//                {
//                    list.add(f.getAbsolutePath());
//                }
//            }
//        }
//    }
//    private static boolean combine(String outFile, String[] inFiles) throws Exception
//    {
//        File out = new File(outFile);
//        File[] files = new File[inFiles.length];
//        for (int i = 0; i < files.length; i++)
//        {
//            files[i] = new File(inFiles[i]);
//        }
//        FileInputStream fis = null;
//        FileOutputStream fos = new FileOutputStream(outFile, true); // 合并其实就是文件的续写,写成true
//        for (int i = 0; i < files.length; i++)
//        {
//            fis = new FileInputStream(files[i]);
//            int len = 0;
//            for (byte[] buf = new byte[1024 * 1024]; (len = fis.read(buf)) != -1;)
//            {
//                fos.write(buf, 0, len);
//            }
//        }
//        fis.close();
//        fos.close();
//        return true;
//    }

}
