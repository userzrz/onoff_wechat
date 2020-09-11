package com.onoff.wechatofficialaccount.entity;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/8/3 10:28
 * @VERSION 1.0
 **/
public class Https {

    //onoffmedia.top
    //3281p3c855.zicp.vip
    public static String domain="3281p3c855.zicp.vip";

    //http%3a%2f%2fonoffmedia.top%2fmiddle
    //http%3a%2f%2f3281p3c855.zicp.vip%2fmiddle
    //绑定邀请关系回调地址(urlEncode)
    public static String redirect_uri="http%3a%2f%2f3281p3c855.zicp.vip%2fmiddle";

    //http%3a%2f%2fonoffmedia.top%2fsign_in
    //http%3a%2f%2f3281p3c855.zicp.vip%2fsign_in
    //签到地址(urlEncode)
    public static String signIn_uri="http%3a%2f%2f3281p3c855.zicp.vip%2fsign_in";

    //更新菜单
    public static String menuHttps="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    //用户同意授权，获取code
    public static String codeHttps="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    //通过code换取网页授权access_token
    public static String tokenHttps="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    //网页拉取用户信息(需scope为 snsapi_userinfo)
    public static String webUserInfoHttps="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    //获取用户基本信息
    public static String userInfoHttps="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    //客服发送消息
    public static String kfHttps="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    //新增临时素材地址
    public static String mediaHttps="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    //新增永久素材地址
    public static String materialHttps="https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";

    //删除永久素材地址
    public static String delmaterialHttps="https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=ACCESS_TOKEN";

    //添加客服账号
    public static String addkfHttps="https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN";

    public static String userListHttps="https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
}
