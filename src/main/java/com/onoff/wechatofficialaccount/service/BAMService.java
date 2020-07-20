package com.onoff.wechatofficialaccount.service;

import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Material;

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
     * @param account
     * @param passWord
     * @return
     */
    Admin getAdmin(String account, String passWord);

    /**
     * 保存海报素材信息
     * @param material
     * @return
     */
    int saveMaterial(Material material);

    /**
     * 获取海报素材信息
     */
    List<Material> getMaterial();

    /**
     * 删除素材信息
     * @return
     */
    int delMaterial(String mediaId);

}
