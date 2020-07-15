package com.onoff.wechatofficialaccount.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/11 21:53
 * @VERSION 1.0
 **/
@Data
@XStreamAlias("xml")
public class NewsMessage extends  BaseMessage{

    @XStreamAlias("ArticleCount")
    private String articleCount;

    @XStreamAlias("Articles")
    private List<Article> articles = new ArrayList<>();

    public NewsMessage(Map<String, String> requestMap, List<Article> articles) {
        super(requestMap);
        setMsgType("news");
        this.articleCount=articles.size()+"";
        this.articles = articles;
    }
}
