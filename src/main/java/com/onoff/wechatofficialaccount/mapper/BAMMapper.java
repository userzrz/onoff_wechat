package com.onoff.wechatofficialaccount.mapper;

import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Integral;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.entity.DO.SignIn;
import com.onoff.wechatofficialaccount.entity.DO.SignInQR;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.entity.BAM.Relation;
import com.onoff.wechatofficialaccount.entity.UserScene;
import com.onoff.wechatofficialaccount.entity.VO.Leaderboard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Mapper
@Repository()
public interface BAMMapper {

    /**
     * 查询用户是否打卡
     * @param unionId
     * @param time 二维码保存时间
     * @return
     */
    List<SignIn> querySignIn(@Param("param1") String unionId, @Param("param2")String time);

    /**
     * 查询所有未激活的数据
     * @return
     */
    List<SignIn> querySignInNonactivated();

    /**
     * 返回指定二维码打卡的总数
     * @param time
     * @return
     */
    int countSignIn(@Param("time")String time);


    /**
     * 修改用户未激活记录
     * @param unionId
     * @return
     */
    int putSignIn(@Param("unionId")String unionId,@Param("time")Long time);


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
     * 验证管理员信息
     *
     * @return
     */
    Admin getAdmin(@Param("a") String account, @Param("b") String passWord);

    /**
     * 获取用户信息 使用openId
     * @param openId
     * @return
     */
    User getUser(String openId);

    /**
     * 获取用户信息使用unionId
     * @param unionId
     * @return
     */
    User getUser_Unionid(String unionId);

    /**
     * 保存海报素材信息
     * @param material
     * @return
     */
    int saveMaterial(Material material);

    /**
     * 获取素材信息
     */
    List<Material> getMaterial(String type);

    /**
     * 删除素材信息
     * @return
     */
    int delMaterial(String mediaId);

    /**
     * 新增用户关系
     * @return
     */
    int saveRelation(@Param("a")String openId,@Param("b")String unionId,@Param("c")String time);

    /**
     * 查询关系是否存在
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
    int putRelation(@Param("a") int type,@Param("b") String unionId,@Param("c")String time);

    /**
     * 查询用户是否存在积分表中
     * @param openId
     * @return
     */
    int getIntegralUser(String openId,int period);

    /**
     * 新增用户积分
     *
     * @return
     */
    int saveIntegral(Integral integral);

    /**
     * 查询用户积分总和
     *
     * @return
     */
    int getIntegral(@Param("openId") String openId,@Param("period") int period);

    /**
     * 获取积分排行榜前20
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
    Leaderboard getRanking(@Param("a") String openId,@Param("b") int period);

    /**
     * 查询用户助力时的期数
     * @param relationId
     * @return
     */
    int queryRelationidPeriod(String relationId);

    /**
     * 清空积分表
     * @return
     */
    int delIntegral();

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
