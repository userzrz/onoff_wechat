package com.onoff.wechatofficialaccount.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.onoff.wechatofficialaccount.entity.*;
import com.onoff.wechatofficialaccount.entity.VO.Connect;
import com.onoff.wechatofficialaccount.service.WeChatService;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import com.onoff.wechatofficialaccount.utils.WeChatUtils;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/9 19:27
 * @VERSION 1.0
 **/
@Slf4j
@Service
public class WeChatServiceImpl implements WeChatService {


    @Override
    public boolean check(Connect connect) {
        // 1）将token、timestamp、nonce三个参数进行字典序排序
        String[] strs = new String[]{WeChatUtils.TOKEN, connect.getTimestamp(), connect.getNonce()};
        Arrays.sort(strs);
        //2）将三个参数字符串拼接成一个字符串进行sha1加密
        String str = strs[0] + strs[1] + strs[2];
        String mysig = WeChatUtils.sha1(str);
        //3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        return mysig.equalsIgnoreCase(connect.getSignature());
    }

    @Override
    public User getUserInfo(String openid) {
        String url="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        url = url.replace("ACCESS_TOKEN", getAccessToken()).replace("OPENID", openid);
        String result = WeChatUtils.get(url);
        JSONObject jsonObject = JSONObject.parseObject(result);
         String openId=jsonObject.getString("openid");
         String nickName=jsonObject.getString("nickname");
         String sex=jsonObject.getString("sex");
         String headImgUrl=jsonObject.getString("headimgurl");
         String subScribeTime=jsonObject.getString("subscribe_time");
         String subscribeScene=jsonObject.getString("subscribe_scene");
         String qrScene=jsonObject.getString("qr_scene");
         String qrSceneStr=jsonObject.getString("qr_scene_str");
        return  new User(openId,nickName,sex,headImgUrl,subScribeTime,subscribeScene,qrScene,qrSceneStr);
    }

    @Override
    public String getAccessToken() {
        if(WeChatUtils.access_token==null||WeChatUtils.expireTime<System.currentTimeMillis()) {
            WeChatUtils.getToken();
        }
        return WeChatUtils.access_token;
    }

    @Override
    public Map<String, String> parseRequest(InputStream is) {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        try {
            //读取输入流，获取文档对象
            Document document = reader.read(is);
            //根据文档对象获取根节点
            Element root = document.getRootElement();
            //获取节点的所有的子节点
            List<Element> elements = root.elements();
            for (Element e : elements) {
                map.put(e.getName(), e.getStringValue());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public String beanToXml(BaseMessage msg) {
        XStream stream = new XStream();
        //设置需要处理XStreamAlias("xml")注释的类
        stream.processAnnotations(TextMessage.class);
        stream.processAnnotations(ImageMessage.class);
        stream.processAnnotations(MusicMessage.class);
        stream.processAnnotations(NewsMessage.class);
        stream.processAnnotations(VideoMessage.class);
        stream.processAnnotations(VoiceMessage.class);
        return stream.toXML(msg);
    }

    @Override
    public String getRespose(Map<String, String> requestMap) {
        BaseMessage msg = null;
        String msgType = requestMap.get("MsgType");
        switch (msgType) {
            //处理文本消息
            case "text":
                msg = new TextMessage(requestMap,requestMap.get("Content"));
                break;
            case "image":
                msg = new TextMessage(requestMap,"您发送的图片，我们已接收");
                break;
            case "voice":
                msg = new TextMessage(requestMap,"您发送的语音消息，我们已接收");
                break;
            case "video":
                msg = new TextMessage(requestMap,"您发送的视频，我们已接收");
                break;
            case "shortvideo":
                msg = new TextMessage(requestMap,"您发送的小视频，我们已接收");
                break;
            case "location":
                msg = new TextMessage(requestMap,"您发送位置信息，我们已接收");
                break;
            case "link":
                msg = new TextMessage(requestMap,"您发送的链接，我们已接收");
                break;
            case "event":
                User User=getUserInfo(requestMap.get("FromUserName"));

                msg = new TextMessage(requestMap,"您发送的语音消息，我们已接收");
                break;
            default:
                break;
        }
        //把消息对象处理为xml数据包
        if (msg != null) {
            return beanToXml(msg);
        }
        return null;
    }

    /**
     * 调用图灵机器人聊天
     * @param msg 	发送的消息
     * @return
     * by 罗召勇 Q群193557337
     */
    public String chat(String msg) {
        String result =null;
        String url ="http://op.juhe.cn/iRobot/index";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",CommonUtils.APPKEY);//您申请到的本接口专用的APPKEY
        params.put("info",msg);//要发送给机器人的内容，不要超过30个字符
        params.put("dtype","");//返回的数据的格式，json或xml，默认为json
        params.put("loc","");//地点，如北京中关村
        params.put("lon","");//经度，东经116.234632（小数点后保留6位），需要写为116234632
        params.put("lat","");//纬度，北纬40.234632（小数点后保留6位），需要写为40234632
        params.put("userid","");//1~32位，此userid针对您自己的每一个用户，用于上下文的关联
        try {
            result =CommonUtils.net(url, params, "GET");
            //解析json
            JSONObject jsonObject = JSONObject.parseObject(result);
            //取出error_code
            int code = jsonObject.getByte("error_code");
            if(code!=0) {
                return null;
            }
            //取出返回的消息的内容
            String resp = jsonObject.getJSONObject("result").getString("text");
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public  String upload(String path,String type) {
        File file = new File(path);
        //地址
        String url="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
        url = url.replace("ACCESS_TOKEN", getAccessToken()).replace("TYPE", type);
        try {
            URL urlObj = new URL(url);
            //强转为案例连接
            HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();
            //设置连接的信息
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            //设置请求头信息
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "utf8");
            //数据的边界
            String boundary = "-----"+System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            //获取输出流
            OutputStream out = conn.getOutputStream();
            //创建文件的输入流
            InputStream is = new FileInputStream(file);
            //第一部分：头部信息
            //准备头部信息
            StringBuilder sb = new StringBuilder();
            sb.append("--");
            sb.append(boundary);
            sb.append("\r\n");
            sb.append("Content-Disposition:form-data;name=\"media\";filename=\""+file.getName()+"\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            out.write(sb.toString().getBytes());
            //第二部分：文件内容
            byte[] b = new byte[1024];
            int len;
            while((len=is.read(b))!=-1) {
                out.write(b, 0, len);
            }
            is.close();
            //第三部分：尾部信息
            String foot = "\r\n--"+boundary+"--\r\n";
            out.write(foot.getBytes());
            out.flush();
            out.close();
            //读取数据
            InputStream is2 = conn.getInputStream();
            StringBuilder resp = new StringBuilder();
            while((len=is2.read(b))!=-1) {
                resp.append(new String(b,0,len));
            }
            return resp.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
