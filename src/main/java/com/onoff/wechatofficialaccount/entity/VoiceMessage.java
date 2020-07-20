package com.onoff.wechatofficialaccount.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/11 21:46
 * @VERSION 1.0
 **/
@Data
@XStreamAlias("xml")
public class VoiceMessage extends BaseMessage  implements Serializable {

    @XStreamAlias("MediaId")
    private String mediaId;

    public VoiceMessage(Map<String, String> requestMap, String mediaId) {
        super(requestMap);
        this.setMsgType("voice");
        this.mediaId = mediaId;
    }
}
