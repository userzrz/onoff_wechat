package com.onoff.wechatofficialaccount.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/11 21:17
 * @VERSION 1.0
 **/
@Data
@XStreamAlias("xml")
public class ImageMessage extends BaseMessage  implements Serializable {

    @XStreamAlias("MediaId")
    private String mediaId;

    public ImageMessage(Map<String, String> requestMap, String mediaId) {
        super(requestMap);
        this.setMsgType("image");
        this.mediaId=mediaId;
    }
}
