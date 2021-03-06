package com.onoff.wechatofficialaccount.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/11 21:54
 * @VERSION 1.0
 **/
@XStreamAlias("item")
@AllArgsConstructor
@Data
public class Article  implements Serializable {

    @XStreamAlias("Title")
    private String title;

    @XStreamAlias("Description")
    private String description;

    @XStreamAlias("PicUrl")
    private String picUrl;

    @XStreamAlias("Url")
    private String url;
}
