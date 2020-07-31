package com.onoff.wechatofficialaccount.entity.BAM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/16 18:04
 * @VERSION 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Material implements Serializable {

    /**
     * 素材id
     */
    private String mediaId;

    /**
     * 素材ulr地址
     */
    private String url;
}
