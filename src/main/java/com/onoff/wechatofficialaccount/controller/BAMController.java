package com.onoff.wechatofficialaccount.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.service.BAMService;
import com.onoff.wechatofficialaccount.service.WeChatService;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import com.onoff.wechatofficialaccount.utils.MD5Utils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/7/16 15:44
 * @VERSION 1.0
 **/
@Slf4j
@Controller
public class BAMController {

    @Autowired
    BAMService service;

    @Autowired
    WeChatService weChatService;

    @GetMapping({"/","/login.html"})
    public String login(HttpSession session){
        if (session.getAttribute(CommonUtils.ADMIN_SESSION) != null) {
            return "redirect:/index";
        }
        return "login";
    }

    @GetMapping("/index.html")
    public String index(){
        return "index";
    }


    /**
     * 上传海报素材
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam(value = "myfile",required = true) MultipartFile file,Model model){
        //设置0为临时素材，设置1为永久素材
         int typeCode=1;
         int result2=0;
        try {
            String result=weChatService.uploadMaterial(typeCode,CommonUtils.multipartFileToFile(file),"image");
            Material material= JSONObject.parseObject(result, Material.class);
            if(material.getMediaId()==null){
                    model.addAttribute("msg","添加失败"+result);
                    return "index";
            }
                 result2=service.saveMaterial(material);
                if (result2>0){
                    model.addAttribute("msg", "添加成功");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    @ResponseBody
    @PostMapping(("/delmaterial"))
    public int delMaterial(@RequestParam(value = "mediaId",required = true) String mediaId, Model model){
        int result=weChatService.delMaterial(mediaId);
        if (result==0){
            int result2=service.delMaterial(mediaId);
            if(result2>0){
                model.addAttribute("msg","删除成功");
                return 0;
            }else {
                model.addAttribute("msg","数据库删除素材失败");
            }
        }else {
            model.addAttribute("msg","微信服务器素材删除失败");
        }
        return 1;
    }

    /**
     * 登录
     * @param admin
     * @param session
     * @param response
     * @param model
     * @return
     */
    @PostMapping(value = "/login/verify")
    public String loginVerify(@ModelAttribute Admin admin, HttpSession session, HttpServletResponse response, Model model) {
        String pwd = MD5Utils.MD5Encode(admin.getPassWord(), "utf8");
        admin = service.getAdmin(admin.getAccount(), pwd);
        if (admin != null) {
            session.setAttribute(CommonUtils.ADMIN_SESSION, admin);
            return "redirect:/index.html";
        } else {
            model.addAttribute("msg", "账号或密码错误");
            return "login";
        }
    }
}
