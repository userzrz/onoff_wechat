package com.onoff.wechatofficialaccount.mapper;


import com.onoff.wechatofficialaccount.entity.User;

public interface WechatMapper {

    /**
     * 新增用户
     * @param user
     * @return
     */
    int addUser(User user);
}
