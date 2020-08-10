package com.onoff.wechatofficialaccount.mapper;

import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Integral;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.entity.BAM.Relation;
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
     * 验证管理员信息
     *
     * @return
     */
    Admin getAdmin(@Param("a") String account, @Param("b") String passWord);

    /**
     * 获取用户信息
     * @param openId
     * @return
     */
    User getUser(String openId);

    /**
     * 保存海报素材信息
     * @param material
     * @return
     */
    int saveMaterial(Material material);

    /**
     * 获取海报信息
     */
    List<Material> getMaterial();

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
    int getIntegralUser(String openId);

    /**
     * 新增用户积分
     *
     * @return
     */
    int saveIntegral(@Param("a") String openId,@Param("b") int record,@Param("c") int source,@Param("d")String time);

    /**
     * 查询用户积分总和
     *
     * @return
     */
    int getIntegral(String openId);

    /**
     * 获取积分排行榜前100
     * @return
     */
    List<Leaderboard> getLeaderboard();

    /**
     * 查询指定用户的名次
     * @param openId
     * @return
     */
    Leaderboard getRanking(String openId);
}
