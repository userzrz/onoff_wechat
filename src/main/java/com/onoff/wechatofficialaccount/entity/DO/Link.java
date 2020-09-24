package com.onoff.wechatofficialaccount.entity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/9/21 13:46
 * @VERSION 1.0
 **/
@AllArgsConstructor
@Data
public class Link implements Serializable {

    private String short_url;

    private String url;
}
