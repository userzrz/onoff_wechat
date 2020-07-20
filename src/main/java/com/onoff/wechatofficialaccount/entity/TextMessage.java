package com.onoff.wechatofficialaccount.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/10 19:45
 * @VERSION 1.0
 **/
@Data
@XStreamAlias("xml")
public class TextMessage extends BaseMessage  implements Serializable {

    @XStreamAlias("Content")
    private String content;

    public TextMessage(Map<String, String> requestMap, String content) {
        super(requestMap);
        this.setMsgType("text");
        this.content = content;
    }
}
