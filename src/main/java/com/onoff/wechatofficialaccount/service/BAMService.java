package com.onoff.wechatofficialaccount.service;

import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Integral;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.entity.BAM.Relation;
import com.onoff.wechatofficialaccount.entity.DO.SignIn;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.entity.UserScene;
import com.onoff.wechatofficialaccount.entity.VO.Leaderboard;

import java.util.List;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/16 15:54
 * @VERSION 1.0
 **/
public interface BAMService {

    /**
     * 查询用户是否打卡
     * @param unionId
     * @param time
     * @return
     */
    List<SignIn> querySignIn(String unionId, String time);

    /**
     * 返回指定二维码打卡的总数
     * @param time
     * @return
     */
    int countSignIn(String time);

    /**
     * 清除用户打卡记录
     * @param unionId
     * @return
     */
    int putSignIn(String unionId,Long time);

    /**
     * 保存打卡信息
     * @param signIn
     * @return
     */
    int saveSignIn(SignIn signIn);
    /**
     * 查询最新期
     * @return
     */
    int queryPeriod();

    /**
     * 查询管理员
     *
     * @param account
     * @param passWord
     * @return
     */
    Admin getAdmin(String account, String passWord);

    /**
     * 处理中间页信息(建立用户关系)
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
     * 获取素材信息
     */
    List<Material> getMaterial(String type);

    /**
     * 删除素材信息
     *
     * @return
     */
    int delMaterial(String mediaId);

    /**
     * 拼接用户请求
     *
     * @param state
     * @return
     */
    String generateHttp(String state,String url,String scope);

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
    Relation getRelation(String unionId);

    /**
     * 删除用户关联关系
     * @param unionId
     * @return
     */
    int delRelation(String unionId);

    /**
     * 修改关系类型
     * @param unionId
     * @return
     */
    int putRelation(int type,String unionId,String time);


    /**
     * 新增用户积分
     * @return
     */
    int saveIntegral(Integral Integral);

    /**
     * 查询用户是否存在积分表中
     * @param openId
     * @return
     */
    int getIntegralUser(String openId);

    /**
     * 查询用户积分总和
     *
     * @return
     */
    int getIntegral(String openId,int period);

    /**
     * 获取积分排行榜
     * @return
     */
    List<Leaderboard> getLeaderboard(int period);

    /**
     * 获取指定用户的积分记录
     * @param openId
     * @return
     */
    List<Integral> getIntegralrecord(String openId);

    /**
     * 查询指定用户的名次
     * @param openId
     * @return
     */
    Leaderboard getRanking(String openId,int period);

    /**
     * 查询用户助力时的期数
     * @param relationId
     * @return
     */
    int queryRelationidPeriod(String relationId);

    /**
     * 统计用户来源
     * @return
     */
    List<UserScene> queryScene();

    /**
     * 获取指定期的参与人数
     * @param period 0表示统计所有期
     * @return
     */
    int getParticipants(int period);

}
