package com.onoff.wechatofficialaccount.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.Map;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/11 21:48
 * @VERSION 1.0
 **/
@Data
@XStreamAlias("xml")
public class VideoMessage extends BaseMessage{

    @XStreamAlias("MediaId")
    private String mediaId;

    @XStreamAlias("Title")
    private String title;

    @XStreamAlias("Description")
    private String description;

    public VideoMessage(Map<String, String> requestMap, String mediaId, String title, String description) {
        super(requestMap);
        setMsgType("video");
        this.mediaId = mediaId;
        this.title = title;
        this.description = description;
    }
}
