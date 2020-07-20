package com.onoff.wechatofficialaccount.service.impl;

import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.mapper.BAMMapper;
import com.onoff.wechatofficialaccount.service.BAMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/16 15:54
 * @VERSION 1.0
 **/
@Service
public class BAMServiceImpl implements BAMService {

    @Autowired
    BAMMapper mapper;

    @Override
    public Admin getAdmin(String account, String passWord) {
        return mapper.getAdmin(account, passWord);
    }

    @Override
    public int saveMaterial(Material material) {
        return mapper.saveMaterial(material);
    }

    @Override
    public List<Material> getMaterial() {
        return mapper.getMaterial();
    }

    @Override
    public int delMaterial(String mediaId) {
        return mapper.delMaterial(mediaId);
    }

}
