package com.onoff.wechatofficialaccount.service;

import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.entity.BAM.UserRelation;
import com.onoff.wechatofficialaccount.entity.User;

import java.util.List;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/16 15:54
 * @VERSION 1.0
 **/
public interface BAMService {

    /**
     * 查询管理员
     *
     * @param account
     * @param passWord
     * @return
     */
    Admin getAdmin(String account, String passWord);

    /**
     * 处理中间页信息
     * @param state
     * @param code
     */
    int middlePage(String state,String code);

    /**
     * 保存海报素材信息
     *
     * @param material
     * @return
     */
    int saveMaterial(Material material);

    /**
     * 获取用户信息
     *
     * @param openId
     * @return
     */
    User getUser(String openId);

    /**
     * 获取海报素材信息
     */
    List<Material> getMaterial();

    /**
     * 删除素材信息
     *
     * @return
     */
    int delMaterial(String mediaId);

    /**
     * 拼接用户请求
     *
     * @param openId
     * @return
     */
    String generateHttp(String openId);

    /**
     * 设置菜单
     * @return
     */
    String setMenu();

    /**
     * 查询关注者的邀请人openId
     * @param unionId
     * @return
     */
    UserRelation getUserRelation(String unionId);

    /**
     * 删除用户关联关系
     * @param unionId
     * @return
     */
    int delUserRelation(String unionId);

}
