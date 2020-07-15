package com.onoff.wechatofficialaccount.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信请求参数
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/10 13:29
 * @VERSION 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Connect {

    //
    private String signature;

    private String timestamp;

    private String nonce;

    private String echostr;
}
