package com.onoff.wechatofficialaccount.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.entity.BAM.UserRelation;
import com.onoff.wechatofficialaccount.entity.Menus.ImageMenus;
import com.onoff.wechatofficialaccount.entity.Menus.Menus;
import com.onoff.wechatofficialaccount.entity.Menus.OneMenus;
import com.onoff.wechatofficialaccount.entity.Menus.TwoMenus;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.mapper.BAMMapper;
import com.onoff.wechatofficialaccount.service.BAMService;
import com.onoff.wechatofficialaccount.service.WeChatService;
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
    public Admin getAdmin(String account, String passWord) {
        return mapper.getAdmin(account, passWord);
    }

    @Override
    public int middlePage(String state, String code) {
        //返回static+code
        log.info("state-------------->" + state);
        log.info("code-------------->" + code);
        //第一步：使用code换取access_token及用户openId
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        url = url.replace("APPID", WeChatUtils.FWAPPID)
                .replace("SECRET", "2ebd982fc738d5ed36a04e9764cd0092")
                .replace("CODE", code);
        String data = WeChatUtils.get(url);
        log.info("使用code获取到的数据-------------->" + data);
        JSONObject jsonObject = JSONObject.parseObject(data);
        //第二步：拉取用户信息
        url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        url = url.replace("ACCESS_TOKEN", jsonObject.getString("access_token"))
                .replace("OPENID", jsonObject.getString("openid"));
        data = WeChatUtils.get(url);
        log.info("拉取到的用户信息--------------------->" + data);
        jsonObject = JSONObject.parseObject(data);
        //取出unionid，测试时使用openid
        String unionid=jsonObject.getString("openid");
        UserRelation userRelation=mapper.getUserRelation(unionid);
        if(userRelation==null){
            //建立用户关系
            int result=mapper.saveUserRelation(state,unionid);
            return result;
        }else {
            return 3;
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
    public List<Material> getMaterial() {
        return mapper.getMaterial();
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
        TwoMenus twoMenus1 = new TwoMenus("view", "FASHION", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=2&sn=ee519a6ddb733583d0c10c87902a31c6&scene=18");
        TwoMenus twoMenus2 = new TwoMenus("view", "BEAUTY", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=3&sn=d2770eccc2a4734b94d15f1da747d868&scene=18");
        TwoMenus twoMenus3 = new TwoMenus("view", "LIFE", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=4&sn=a97599b157b369b9e290d9a9dbf82680&scene=18");
        TwoMenus twoMenus4 = new TwoMenus("view", "EDITOR PICK", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=5&sn=cf84a9714959316e207fa1ae570233c3&scene=18");
        TwoMenus twoMenus5 = new TwoMenus("view", "OOTD4M", "http://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=8&sn=8999e1ceabe960dd055c487bb6142712&scene=18#wechat_redirect");
        twoMenusList.add(twoMenus1);
        twoMenusList.add(twoMenus2);
        twoMenusList.add(twoMenus3);
        twoMenusList.add(twoMenus4);
        twoMenusList.add(twoMenus5);
        //第一个一级菜单
        OneMenus oneMenus1 = new OneMenus("click", "\uD83D\uDD19", "001", twoMenusList);
        //二级菜单集合(第二个一级菜单子菜单)
        List<TwoMenus> twoMenusLIst2 = new ArrayList<>();
        TwoMenus twoMenus2_1 = new TwoMenus("view", "CELEBRITY", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=6&sn=3196f57e492c52931c4813cc135826be&scene=18");
        TwoMenus twoMenus2_2 = new TwoMenus("view", "TO签福利", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU0MjcxODUzMA==&hid=7&sn=3f409adc90882687c5d9b3aa2d18b92a&scene=18");
        twoMenusLIst2.add(twoMenus2_1);
        twoMenusLIst2.add(twoMenus2_2);
        //第二个一级菜单
        OneMenus oneMenus2 = new OneMenus("click", "\uD83D\uDD1D", "002", twoMenusLIst2);
        //第三个一级菜单
        ImageMenus imageMenus = new ImageMenus("media_id", "\uD83D\uDD1C", "vShrcmjVz7pud19EDyuE5RISBGGcRhoYiUKeeaj3J_M");
        //添加一级菜单
        oneMenus.add(oneMenus1);
        oneMenus.add(oneMenus2);
        oneMenus.add(imageMenus);
        Menus menus = new Menus();
        menus.setButton(oneMenus);
        // 转为json
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(menus);
        log.info("设置菜单-------->", jsonObject);
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", service.getAccessToken());
        String result = WeChatUtils.post(url, jsonObject.toJSONString());
        return result;
    }

    @Override
    public UserRelation getUserRelation(String unionId) {
        return mapper.getUserRelation(unionId);
    }

    @Override
    public int delUserRelation(String unionId) {
        return mapper.delUserRelation(unionId);
    }

    @Override
    public String generateHttp(String openId) {
        String http = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redire";
        //这里写服务号APPID
        http = http.replace("APPID", WeChatUtils.FWAPPID)
                .replace("REDIRECT_URI", "http%3a%2f%2f3281p3c855.zicp.vip%2fmiddle")
                .replace("SCOPE", "snsapi_userinfo")//snsapi_userinfo snsapi_base
                .replace("STATE", openId);
        return http;
    }

}
