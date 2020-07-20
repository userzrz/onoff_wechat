package com.onoff.wechatofficialaccount.mapper;


import com.onoff.wechatofficialaccount.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository()
public interface WechatMapper {

    /**
     * 新增用户
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 删除用户
     * @param openId
     * @return
     */
    int delUser(String openId);
}
