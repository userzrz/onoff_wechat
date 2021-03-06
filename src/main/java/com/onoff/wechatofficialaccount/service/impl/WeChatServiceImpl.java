package com.onoff.wechatofficialaccount.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.onoff.wechatofficialaccount.entity.*;
import com.onoff.wechatofficialaccount.entity.BAM.Integral;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.entity.BAM.Relation;
import com.onoff.wechatofficialaccount.entity.DO.Command;
import com.onoff.wechatofficialaccount.entity.DO.KlRecord;
import com.onoff.wechatofficialaccount.entity.DO.Poster;
import com.onoff.wechatofficialaccount.entity.VO.Connect;
import com.onoff.wechatofficialaccount.mapper.DAO.BAMDao;
import com.onoff.wechatofficialaccount.mapper.WechatMapper;
import com.onoff.wechatofficialaccount.service.BAMService;
import com.onoff.wechatofficialaccount.service.WeChatService;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import com.onoff.wechatofficialaccount.utils.WeChatUtils;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/9 19:27
 * @VERSION 1.0
 **/
@Slf4j
@Service
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    WechatMapper wechatMapper;

    @Autowired
    BAMService bamService;

    @Autowired
    BAMDao dao;

    @Override
    public User getUserInfo(String openid) {
        String url = Https.userInfoHttps;
        url = url.replace("ACCESS_TOKEN", getAccessToken()).replace("OPENID", openid);
        String result = WeChatUtils.get(url);
        log.info(result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String openId = jsonObject.getString("openid");
        String nickName = jsonObject.getString("nickname");
        String sex = jsonObject.getString("sex");
        String headImgUrl = jsonObject.getString("headimgurl");
        String city = jsonObject.getString("city");
        String province = jsonObject.getString("province");
        String country = jsonObject.getString("country");
        String language = jsonObject.getString("language");
        String subScribeTime = jsonObject.getString("subscribe_time");
        String subscribeScene = jsonObject.getString("subscribe_scene");
        String unionId = jsonObject.getString("unionid");
        String groupId = jsonObject.getString("groupid");
        String tagid_list = jsonObject.getString("tagid_list");
        String remark = jsonObject.getString("remark");
        String subscribe = jsonObject.getString("subscribe");
        log.info(result);
        return new User(openId, nickName, sex, headImgUrl, city, province, country, language, subScribeTime,
                subscribeScene, unionId, groupId, tagid_list, remark, subscribe);
    }

    @Override
    public String getAccessToken() {
        if (WeChatUtils.access_token == null || WeChatUtils.expireTime < System.currentTimeMillis()) {
            WeChatUtils.getToken();
        }
        return WeChatUtils.access_token;
    }

    @Override
    public String getRespose(Map<String, String> requestMap) {
        BaseMessage msg = null;
        String msgType = requestMap.get("MsgType");
        switch (msgType) {
            //处理文本消息
            case "text":
                msg = this.disposeText(requestMap);
                break;
            case "image":
                msg = new TextMessage(requestMap, "已收到您的图片信息");
                break;
            case "voice":
                msg = new TextMessage(requestMap, "已收到您的语音信息");
                break;
            case "video":
                msg = new TextMessage(requestMap, "已收到您的视频信息");
                break;
            case "shortvideo":
                msg = new TextMessage(requestMap, "已收到您的小视频信息");
                break;
            case "location":
                msg = new TextMessage(requestMap, "已收到您的地理位置");
                break;
            case "link":
                msg = new TextMessage(requestMap, "已收到您的链接信息");
                break;
            //关注取消关注事件
            case "event":
                //用户openid
                String openId = requestMap.get("FromUserName");
                String eventType = requestMap.get("Event");
                switch (eventType) {
                    //关注事件
                    case "subscribe":
                        msg = new TextMessage(requestMap, "新潮灵感，开关即现\nYOUR FASHION RADAR");
                        subscribeEvent(openId);
                        break;
                    //取关事件
                    case "unsubscribe":
                        String r = unsubscribeEvent(openId);
                        if (r != null && r.equals("success")) {
                            return "success";
                        }
                        break;
                    case "CLICK":
                        if (requestMap.get("EventKey").equals("WELFARE")) {
                            String id = requestMap.get("FromUserName");
                            Poster poster = dao.queryPoster(id);
                            if (poster == null) {
                                dao.savePoster(new Poster(id, System.currentTimeMillis(), 0));
                            } else {
                                if ((poster.getTime() + 10000) > System.currentTimeMillis() && poster.getTriesLimit() == 0) {
                                    String data = "{\n" +
                                            "    \"touser\":\"" + openId + "\",\n" +
                                            "    \"msgtype\":\"text\",\n" +
                                            "    \"text\":\n" +
                                            "    {\n" +
                                            "         \"content\":\"" + "如未接收到海报，请" + (10 - (System.currentTimeMillis() - poster.getTime()) / 1000) + "秒后重试" + "\"\n" +
                                            "    }\n" +
                                            "}";
                                    log.info(data);
                                    dao.putTriesLimit(id);
                                    kfSendMsg(data);
                                    return "success";
                                } else {
                                    if((poster.getTime() + 10000) < System.currentTimeMillis()){
                                        dao.putPoster(id);
                                    }else {
                                        return "success";
                                    }
                                }
                            }
                            sendmail(id);
                        }
                        break;
                }
                break;
            default:
                break;
        }
        //把消息对象处理为xml数据包
        if (msg != null) {
            return beanToXml(msg);
        }
        return "success";
    }

    @Override
    public String addKf(String data) {
        String url = Https.addkfHttps;
        url = url.replace("ACCESS_TOKEN", getAccessToken());
        String result = WeChatUtils.post(url, data);
        return result;
    }

    @Override
    public int kfSendMsg(String data) {
        String url = Https.kfHttps;
        url = url.replace("ACCESS_TOKEN", getAccessToken());
        String result = WeChatUtils.post(url, data);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getByte("errcode");
    }


    public String uploadMaterial(int typeCode, File file, String type) {
        String url = "";
        if (typeCode == 0) {
            //临时素材地址
            url = Https.mediaHttps;
            url = url.replace("ACCESS_TOKEN", getAccessToken()).replace("TYPE", type);
        } else if (typeCode == 1) {
            //永久素材地址
            url = Https.materialHttps;
            url = url.replace("ACCESS_TOKEN", getAccessToken()).replace("TYPE", type);
        }
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
            String boundary = "-----" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
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
            sb.append("Content-Disposition:form-data;name=\"media\";filename=\"" + file.getName() + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            out.write(sb.toString().getBytes());
            //第二部分：文件内容
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) != -1) {
                out.write(b, 0, len);
            }
            is.close();
            //第三部分：尾部信息
            String foot = "\r\n--" + boundary + "--\r\n";
            out.write(foot.getBytes());
            out.flush();
            out.close();
            //读取数据
            InputStream is2 = conn.getInputStream();
            StringBuilder result = new StringBuilder();
            while ((len = is2.read(b)) != -1) {
                result.append(new String(b, 0, len));
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String delMaterial(String data) {
        String url = Https.delmaterialHttps;
        url = url.replace("ACCESS_TOKEN", getAccessToken());
        String data1 = "{\n" +
                "  \"media_id\":\"" + data + "\"\n" +
                "}";
        log.info(data1);
        String result = WeChatUtils.post(url, data1);
        log.info("Delete permanent material--------------------------------->" + result);
        return result;
    }

    @Override
    public String getQrCodeTicket(String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", getAccessToken());
        String data = "{\n" +
                "\t\"expire_seconds\": 2592000,\n" +
                "\t\"action_name\": \"QR_STR_SCENE\",\n" +
                "\t\"action_info\": {\n" +
                "\t\t\"scene\": {\n" +
                "\t\t\t\"scene_str\": \"" + openid + "\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        String result = WeChatUtils.post(url, data);
        log.info("-->" + result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String ticket = jsonObject.getString("ticket");
        return ticket;
    }

    /**
     * 处理文本消息
     *
     * @param requestMap
     * @return
     */
    private BaseMessage disposeText(Map<String, String> requestMap) {
        BaseMessage msg;
        //用户openid
        String openId = requestMap.get("FromUserName");
        //消息内容
        String content = requestMap.get("Content");
        //判断是否为地址
        String result = checkCellphone(content);
        if (!result.equals("success")) {
            msg = new TextMessage(requestMap, result);
            return msg;
        }
        switch (content) {
            case "排行榜":
                //新增用户积分
                initializeIntegral(openId);
                msg = new TextMessage(requestMap, "<a href='http://" + Https.domain + "/list.html/" + openId + "'>积分排行榜</a>");
                break;
            default:
                Command command = dao.queryCommand(content);
                if (command == null) {
                    return null;
                } else {
                    //计算二维码过期时间
                    Long validTime = command.getDays() * 86400000 + command.getTime();
                    if (validTime < System.currentTimeMillis()) {
                        msg = new TextMessage(requestMap, "该口令已过期，无法使用");
                        return msg;
                    }
                    int count = bamService.countQL(command.getId());
                    if (count >= command.getMaxUser()) {
                        msg = new TextMessage(requestMap, "该口令已达人数上限,无法使用");
                        return msg;
                    }
                    int c = bamService.verifyKL(openId, command.getId());
                    if (c==0) {
                        Integral integral = new Integral(openId, command.getIntegral(), 4, System.currentTimeMillis() + "", CommonUtils.period, command.getTime() + "");
                        msg = new TextMessage(requestMap, "您通过口令新增了 " + command.getIntegral() + " 积分\n前往" + "<a href='http://" + Https.domain + "/list.html/" + openId + "'>积分排行榜</a>" + "查询您的积分吧");
                        //新增一条口令记录
                        bamService.saveKL(new KlRecord(openId,command.getId()));
                        int res = bamService.saveIntegral(integral);
                        if (res != 1) {
                            log.error("用户："+openId+",通过口令:" + command.getId() + "加分失败,该口令设置积分为：" + command.getIntegral());
                        }
                    }else{
                        msg = new TextMessage(requestMap, "重复使用口令无效");
                        return msg;
                    }
                }
                break;
        }
        return msg;
    }


    /**
     * 处理关注事件
     *
     * @param openId
     * @return
     */
    private String subscribeEvent(String openId) {
        User user = this.verifyUser(openId);
        if (user == null) {
            return null;
        }
        String data = "{\n" +
                "    \"touser\":\"" + user.getOpenId() + "\",\n" +
                "    \"msgtype\":\"text\",\n" +
                "    \"text\":\n" +
                "    {\n" +
                "         \"content\":\"你好 " + user.getNickName() + " 欢迎关注我们\"\n" +
                "    }\n" +
                "}";
        int errcode = kfSendMsg(data);
        //查询用户是否有关联关系
        Relation relation = bamService.getRelation(user.getUnionId());
        if (relation == null) {
            if (errcode == 0) {
                log.info("客服成功发送欢迎消息给普通关注用户" + data);
            } else {
                log.error("客服发送消息给普通关注用户失败！！" + data + errcode);
            }
            return "success";
        } else if (relation != null && relation.getRType() == 0) {
            //修改关注类型
            int result = bamService.putRelation(1, relation.getUnionId(), System.currentTimeMillis() + "");
            if (result == 1) {
                log.info("用户关系确立成功");
            } else {
                log.error("用户关系确立失败！");
                return "success";
            }
            //给邀请人添加积分
            initializeIntegral(relation.getOpenId());
            Integral integral = new Integral(relation.getOpenId(), 10, 1, System.currentTimeMillis() + "", CommonUtils.period, user.getOpenId());
            result = bamService.saveIntegral(integral);
            if (result == 1) {
                log.info("给邀请人加分成功");
            } else {
                log.error("给邀请人加分失败邀请人openId:" + relation.getOpenId() + "应加10分" + "邀请好友关注成功");
            }
        }
        return null;
    }

    /**
     * 处理取关事件
     *
     * @param openId
     * @return
     */
    private String unsubscribeEvent(String openId) {
        String data = null;
        User user = bamService.getUser(openId);
        if (user == null) {
            return "success";
        }
        //查询用户是否有关联关系
        Relation relation = bamService.getRelation(user.getUnionId());
        if (relation == null) {
            log.info("------------------>非邀请关注但与公众号产生过交互的用户取消了关注");
            wechatMapper.delUser(openId);
            return "success";
        } else if (relation != null && relation.getRType() == 1) {
            //邀请人openId
            String inviterOpenId = relation.getOpenId();
            //查询用户助力时的期数
            int period = bamService.queryRelationidPeriod(user.getOpenId());
            Integral integral;
            if (period == CommonUtils.period) {
                //给邀请人减分
                integral = new Integral(inviterOpenId, -10, 2, System.currentTimeMillis() + "", CommonUtils.period, user.getOpenId());
                bamService.saveIntegral(integral);
            } else if (period > 0 && period != CommonUtils.period) {
                //给邀请人减分
                integral = new Integral(inviterOpenId, -10, 2, System.currentTimeMillis() + "", period, user.getOpenId());
                bamService.saveIntegral(integral);
            } else {
                log.info("---------->用户隔月取关不减分");
                return null;
            }
            //修改关系类型为2
            bamService.putRelation(2, relation.getUnionId(), System.currentTimeMillis() + "");
            wechatMapper.delUser(openId);
        } else if (relation != null && relation.getRType() == 2) {
            wechatMapper.delUser(openId);
            log.info("---------->通过邀请进入的用户再次取消关注");
        }
        return null;
    }

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
    public User verifyUser(String openId) throws NullPointerException {
        User user = bamService.getUser(openId);
        if (user != null && user.getSubscribe() != null && user.getSubscribe().equals("0")) {
            wechatMapper.delUser(user.getOpenId());
            log.info("-------->发现一条空的用户数据并删除掉了");
        }
        if (user != null && user.getSubscribe() != null && !user.getSubscribe().equals("0")) {
            log.info("-------->该用户数据存在了不需要保存！");
        }
        if (user == null) {
            //调用微信接口获取用户信息，每日可调用50万次
            user = getUserInfo(openId);
            if (user != null) {
                int res = wechatMapper.addUser(user);
                if (res > 0) {
                    log.info("--------->新增用户信息成功");
                }
            } else {
                log.error("------------>出现异常：调用微信接口获取用户数据为空");
                return null;
            }
        }
        return user;
    }

    @Override
    public void initializeIntegral(String openId) {
        //查询本周用户是否存在积分表
        int count = bamService.getIntegralUser(openId);
        if (count <= 0) {
            //初始化用户积分
            Integral integral = new Integral(openId, 0, 0, System.currentTimeMillis() + "", CommonUtils.period);
            bamService.saveIntegral(integral);
        }
    }

    public String sendmail(String openId) {
        //新增用户信息
        User user = verifyUser(openId);
        if (user == null) {
            return null;
        }
        //查询所有海报素材
        List<Material> list = bamService.getMaterial("2");
        int size = list.size();
        //随机数：Math.random()*(n+1-m)+m
        int ran2 = (int) (Math.random() * (size));
        Material material = list.get(ran2);
        //拼接请求
        String http = bamService.generateHttp(openId, Https.redirect_uri, "snsapi_userinfo");
        //生成邀请二维码
        BufferedImage qrImg = CommonUtils.createImage(http);
        //生成海报  1、海报地址  2、用户头像地址  3、邀请二维码  4、用户昵称
        File file = CommonUtils.overlapImage(material.getUrl(), user.getHeadImgUrl(), qrImg, user.getNickName());
        String re = this.uploadMaterial(0, file, "image");
        JSONObject jsonObject = JSONObject.parseObject(re);
        String mediaId = jsonObject.getString("media_id");
        String data = "{\n" +
                "    \"touser\":\"" + openId + "\",\n" +
                "    \"msgtype\":\"image\",\n" +
                "    \"image\":\n" +
                "    {\n" +
                "         \"media_id\":\"" + mediaId + "\"\n" +
                "    }\n" +
                "}";
        log.info(data);
        kfSendMsg(data);
        data = "{\n" +
                "    \"touser\":\"" + openId + "\",\n" +
                "    \"msgtype\":\"text\",\n" +
                "    \"text\":\n" +
                "    {\n" +
                "         \"content\":\"" + "<a href='http://" + Https.domain + "/list.html/" + openId + "'>积分排行榜</a>" + "\"\n" +
                "    }\n" +
                "}";
        log.info(data);
        kfSendMsg(data);
        return "succeed";
    }

    /**
     * 查询符合的手机号码
     *
     * @param str      
     */
    public String checkCellphone(String str) {
        //将给定的正则表达式编译到模式中
        Pattern pattern = Pattern.compile("((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}");
        //创建匹配给定输入与此模式的匹配器。
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return "信息接收成功,稍后客服会与您联系！";
        } else {
            return "success";
        }
    }
}
