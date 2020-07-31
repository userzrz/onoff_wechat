package com.onoff.wechatofficialaccount.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/27 12:28
 * @VERSION 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Access {

    /**
     *网页授权接口调用凭证
     */
    private String access_token;

    /**
     * 有效时间(秒)
     */
    private String expires_in;

    /**
     *用户刷新access_token
     */
    private String refresh_token;

    /**
     *用户唯一标识
     */
    private String openid;

    /**
     *用户授权的作用域
     */
    private String scope;
}
