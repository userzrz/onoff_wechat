package com.onoff.wechatofficialaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.security.DenyAll;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/11 21:50
 * @VERSION 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Music {

    private String title;

    private String description;

    private String musicURL;

    private String hQMusicUrl;

    private String thumbMediaId;

}
