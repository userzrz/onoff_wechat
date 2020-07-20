package com.onoff.wechatofficialaccount.entity.BAM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 管理员
 * @Author ZHENG
 * @Data 2020/7/16 15:52
 * @VERSION 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin implements Serializable {

    private String id;

    private String account;

    private String passWord;
}
