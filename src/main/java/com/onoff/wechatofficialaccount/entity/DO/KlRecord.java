package com.onoff.wechatofficialaccount.entity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/9/21 16:16
 * @VERSION 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KlRecord implements Serializable {

    private String id;

    private String openId;

    private String klId;

    public KlRecord(String openId, String klId) {
        this.openId = openId;
        this.klId = klId;
    }
}
