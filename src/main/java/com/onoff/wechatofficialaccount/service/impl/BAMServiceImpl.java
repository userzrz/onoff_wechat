package com.onoff.wechatofficialaccount.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Integral;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.entity.BAM.Relation;
import com.onoff.wechatofficialaccount.entity.DO.SignIn;
import com.onoff.wechatofficialaccount.entity.Https;
import com.onoff.wechatofficialaccount.entity.Menus.Menus;
import com.onoff.wechatofficialaccount.entity.Menus.OneClick;
import com.onoff.wechatofficialaccount.entity.Menus.OneMenus;
import com.onoff.wechatofficialaccount.entity.Menus.TwoMenus;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.entity.UserScene;
import com.onoff.wechatofficialaccount.entity.VO.Leaderboard;
import com.onoff.wechatofficialaccount.mapper.BAMMapper;
import com.onoff.wechatofficialaccount.service.BAMService;
import com.onoff.wechatofficialaccount.service.WeChatService;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import com.onoff.wechatofficialaccount.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/16 15:54
 * @VERSION 1.0
 **/
@Slf4j
@Service
public class BAMServiceImpl implements BAMService {

    @Autowired
    BAMMapper mapper;
    @Autowired
    WeChatService service;

    @Override
    public int countQL(String remark) {
        return mapper.countQL(remark);
    }

    @Override
    public int verifyKL(String openId, String remark) {
        return mapper.verifyKL(openId,remark);
    }

    @Override
    public List<SignIn> querySignIn(String unionId, String time) {
        return mapper.querySignIn(unionId, time);
    }

    @Override
    public int countSignIn(String time) {
        return mapper.countSignIn(time);
    }

    @Override
    public int putSignIn(String unionId, Long time) {
        return mapper.putSignIn(unionId, time);
    }

    @Override
    public int saveSignIn(SignIn signIn) {
        return mapper.saveSignIn(signIn);
    }

    @Override
    public int queryPeriod() {
        return mapper.queryPeriod();
    }

    @Override
    public Admin getAdmin(String account, String passWord) {
        return mapper.getAdmin(account, passWord);
    }

    @Override
    public int middlePage(String state, String code) {
        //第一步：使用code换取access_token及用户openId
        String url = Https.tokenHttps;
        url = url.replace("APPID", WeChatUtils.FWAPPID)
                .replace("SECRET", WeChatUtils.FWAPPSECRET)
                .replace("CODE", code);
        String data = WeChatUtils.get(url);
        JSONObject jsonObject = JSONObject.parseObject(data);
        //第二步：拉取用户信息
        url = Https.webUserInfoHttps;
        url = url.replace("ACCESS_TOKEN", jsonObject.getString("access_token"))
                .replace("OPENID", jsonObject.getString("openid"));
        data = WeChatUtils.get(url);
        log.info("网页授权获取到的用户信息-->" + data);
        jsonObject = JSONObject.parseObject(data);
        //取出unionid
        String unionid = jsonObject.getString("unionid");
        if (unionid == null || unionid.length() < 2) {
            return 4;
        }
        //获取邀请人信息
        User user = mapper.getUser(state);
        if (user != null) {
            if (user.getUnionId().equals(unionid)) {
                return 4;
            }
        } else {
            return 4;
        }
        //查询邀请人关系
        Relation relation = mapper.getRelation(unionid);
        if (relation == null) {
            //建立用户关系
            return mapper.saveRelation(state, unionid, System.currentTimeMillis() + "");
        } else {
            return 1;
        }
    }

    @Override
    public int saveMaterial(Material material) {
        return mapper.saveMaterial(material);
    }

    @Override
    public User getUser(String openId) {
        return mapper.getUser(openId);
    }

    @Override
    public List<Material> getMaterial(String type) {
        List<Material> materialList = mapper.getMaterial(type);
        Material material;
        String url;
        for (int i = 0; i < materialList.size(); i++) {
            material = materialList.get(i);
            url = material.getUrl();
            material.setUrl(url.replace("http", "https"));
        }
        return materialList;
    }

    @Override
    public int delMaterial(String mediaId) {
        return mapper.delMaterial(mediaId);
    }

    public String setMenu() {
        //一级菜单集合
        List oneMenus = new ArrayList<>();
        //二级菜单集合(第一个一级菜单子菜单)
        List<TwoMenus> twoMenusList = new ArrayList<>();
        TwoMenus twoMenus0 = new TwoMenus("view", "LIFE", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=4&sn=a97599b157b369b9e290d9a9dbf82680&scene=18");
        TwoMenus twoMenus1 = new TwoMenus("view", "FASHION", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=2&sn=ee519a6ddb733583d0c10c87902a31c6&scene=18");
        TwoMenus twoMenus2 = new TwoMenus("view", "BEAUTY", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=3&sn=d2770eccc2a4734b94d15f1da747d868&scene=18");
        TwoMenus twoMenus3 = new TwoMenus("view", "CELEBRITY", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=6&sn=3196f57e492c52931c4813cc135826be&scene=18");
        twoMenusList.add(twoMenus0);
        twoMenusList.add(twoMenus1);
        twoMenusList.add(twoMenus2);
        twoMenusList.add(twoMenus3);
        //第一个一级菜单
        OneMenus oneMenus1 = new OneMenus("click", "精选内容", "001", twoMenusList);
        //第二个一级菜单
        OneClick oneClick = new OneClick("click", "海报福利", "WELFARE");
        //第三个一级菜单
        TwoMenus contact = new TwoMenus("view", "联系我们", "http://onoffmedia.top/contact_us.html");
        //添加一级菜单
        oneMenus.add(oneMenus1);
        oneMenus.add(oneClick);
        oneMenus.add(contact);
        Menus menus = new Menus();
        menus.setButton(oneMenus);
        // 转为json
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(menus);
        log.info("设置菜单-------->", jsonObject);
        String url = Https.menuHttps;
        url = url.replace("ACCESS_TOKEN", service.getAccessToken());
        String result = WeChatUtils.post(url, jsonObject.toJSONString());
        return result;
    }

    @Override
    public Relation getRelation(String unionId) {
        return mapper.getRelation(unionId);
    }

    @Override
    public int delRelation(String unionId) {
        return mapper.delRelation(unionId);
    }

    @Override
    public int putRelation(int type, String unionId, String time) {
        return mapper.putRelation(type, unionId, time);
    }

    @Override
    public int saveIntegral(Integral integral) {
        return mapper.saveIntegral(integral);
    }

    @Override
    public int getIntegralUser(String openId) {
        return mapper.getIntegralUser(openId, CommonUtils.period);
    }

    @Override
    public int getIntegral(String openId, int period) {
        return mapper.getIntegral(openId, period);
    }

    @Override
    public List<Leaderboard> getLeaderboard(int period) {
        return mapper.getLeaderboard(period);
    }

    @Override
    public List<Integral> getIntegralrecord(String openId) {
        return mapper.getIntegralrecord(openId);
    }

    @Override
    public Leaderboard getRanking(String openId, int period) {
        return mapper.getRanking(openId, period);
    }

    @Override
    public int queryRelationidPeriod(String relationId) {
        return mapper.queryRelationidPeriod(relationId);
    }

    @Override
    public List<UserScene> queryScene() {
        List<UserScene> userScenes = mapper.queryScene();
        for (UserScene u : userScenes) {
            if (u.getScene() == null) {
                u.setScene("无数据用户");
                continue;
            }
            switch (u.getScene()) {
                case "ADD_SCENE_SEARCH":
                    u.setScene("公众号搜索");
                    break;
                case "ADD_SCENE_ACCOUNT_MIGRATION":
                    u.setScene("公众号迁移");
                    break;
                case "ADD_SCENE_PROFILE_CARD":
                    u.setScene("名片分享");
                    break;
                case "ADD_SCENE_QR_CODE":
                    u.setScene("扫描二维码");
                    break;
                case "ADD_SCENE_PROFILE_LINK":
                    u.setScene("图文页内名称点击");
                    break;
                case "ADD_SCENE_PROFILE_ITEM":
                    u.setScene("图文页右上角菜单");
                    break;
                case "ADD_SCENE_PAID":
                    u.setScene("支付后关注");
                    break;
                case "ADD_SCENE_WECHAT_ADVERTISEMENT":
                    u.setScene("微信广告");
                    break;
                case "ADD_SCENE_OTHERS":
                    u.setScene("其他");
                    break;
                default:
                    break;
            }
        }
        return userScenes;
    }

    @Override
    public int getParticipants(int period) {
        return mapper.getParticipants(period);
    }

    @Override
    public String generateHttp(String state, String url, String scope) {
        String http = Https.codeHttps;
        //这里写服务号APPID
        http = http.replace("APPID", WeChatUtils.FWAPPID)
                .replace("REDIRECT_URI", url)
                .replace("SCOPE", scope)
                .replace("STATE", state);
        return http;
    }

}
