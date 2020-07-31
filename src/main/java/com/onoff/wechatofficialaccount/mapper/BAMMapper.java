package com.onoff.wechatofficialaccount.mapper;

import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.entity.BAM.UserRelation;
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
    int saveUserRelation(@Param("a")String openId,@Param("b")String unionId);

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
