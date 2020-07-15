package com.onoff.wechatofficialaccount.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/11 21:54
 * @VERSION 1.0
 **/
@XStreamAlias("item")
@AllArgsConstructor
@Data
public class Article {

    private String title;

    private String description;

    private String picUrl;

    private String url;
}
