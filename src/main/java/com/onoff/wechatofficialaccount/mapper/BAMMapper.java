package com.onoff.wechatofficialaccount.mapper;

import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
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
     * 验证用户信息
     *
     * @return
     */
    Admin getAdmin(@Param("a") String account, @Param("p") String passWord);

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
}
