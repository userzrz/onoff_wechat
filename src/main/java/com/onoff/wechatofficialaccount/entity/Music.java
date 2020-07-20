package com.onoff.wechatofficialaccount.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.security.DenyAll;
import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/11 21:50
 * @VERSION 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Music  implements Serializable {

    private String title;

    private String description;

    private String musicURL;

    private String hQMusicUrl;

    private String thumbMediaId;

}
