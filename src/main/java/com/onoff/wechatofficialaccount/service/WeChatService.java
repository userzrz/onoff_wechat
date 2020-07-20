package com.onoff.wechatofficialaccount.service;

import com.onoff.wechatofficialaccount.entity.BaseMessage;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.entity.VO.Connect;

import java.io.File;
import java.io.InputStream;
import java.util.Map;


public interface WeChatService {
    /**
     * 验证消息是否来自微信服务器
     *
     * @param connect
     * @return
     */
    boolean check(Connect connect);

    /**
     * 获取用户信息
     * @param openid
     * @return
     */
     User getUserInfo(String openid);

    /**
     * 暴露获取access_token
     * @return
     */
    String getAccessToken();

    /**
     * 解析xml数据包
     *
     * @param is
     * @return
     */
    Map<String, String> parseRequest(InputStream is);

    /**
     * 将对象转为xml
     * @param msg
     * @return
     */
    String beanToXml(BaseMessage msg);

    /**
     * 用于处理所有的事件和消息的回复
     * @param requestMap
     * @return 返回的是xml数据包
     */
    String getRespose(Map<String, String> requestMap);

    /**
     * 调用聊天机器人
     * @param msg
     * @return
     */
    String chat(String msg);


    /**
     * 添加客服号
     * @return
     */
    String addKf(String data);

    /**
     * 客服发送消息
     * @param data 消息内容
     * @return 0：成功，其他为错误
     */
    int kfSendMsg(String data);

    /**
     *上传素材
     * @param typeCode 0为临时素材，1为永久素材
     * @param file
     * @param type
     * @return
     */
    String uploadMaterial(int typeCode,File file,String type);


    /**
     * 删除永久素材
     * @param data
     * @return
     */
    int delMaterial(String data);

    /**
     * 生成带参数二维码
     * @param openid 用户openid为蚕食
     * @return
     */
    String getQrCodeTicket(String openid);

}
