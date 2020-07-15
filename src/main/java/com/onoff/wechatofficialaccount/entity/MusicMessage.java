package com.onoff.wechatofficialaccount.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.Map;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/11 21:49
 * @VERSION 1.0
 **/
@Data
@XStreamAlias("xml")
public class MusicMessage extends BaseMessage {

    @XStreamAlias("Music")
    private Music music;

    public MusicMessage(Map<String, String> requestMap, Music music) {
        super(requestMap);
        setMsgType("music");
        this.music = music;
    }
}
