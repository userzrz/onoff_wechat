package com.onoff.wechatofficialaccount.entity.Menus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/29 18:42
 * @VERSION 1.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OneMenus {

    private String type;

    private String name;

    private String key;

    private List<TwoMenus> sub_button;
}
