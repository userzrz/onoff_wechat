package com.onoff.wechatofficialaccount.controller;

import com.alibaba.fastjson.JSONObject;
import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.service.BAMService;
import com.onoff.wechatofficialaccount.service.WeChatService;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import com.onoff.wechatofficialaccount.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @ResponseBody
    @RequestMapping("/MP_verify_5z40QMC3xdtOZ81T.txt")
    public String text() {
        return "5z40QMC3xdtOZ81T";
    }

    /**
     * 建立关系
     *
     * @param state
     * @param code
     * @return
     */
    @GetMapping("/middle")
    public String middlePage(@RequestParam(value = "state") String state, @RequestParam(value = "code") String code) {
        int result = service.middlePage(state, code);
        if (result == 1) {
            log.info("--------->用户关系存在了");
        } else {
            log.info("--------->用户关系建立成功");
        }
        return "middle";
    }

    @GetMapping("/list.html")
    public String Leaderboard(@RequestParam(value = "code") String openId, Model model) {
        model.addAttribute("Relation", service.getRanking(openId));
        model.addAttribute("Leaderboard", service.getLeaderboard());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        model.addAttribute("Date", "当前数据统计于：" + formatter.format(new Date()));
        return "list";
    }


    @GetMapping({"/", "/login.html"})
    public String login(HttpSession session) {
        if (session.getAttribute(CommonUtils.ADMIN_SESSION) != null) {
            return "redirect:/index";
        }
        return "login";
    }

    @GetMapping(value = "/exit")
    public String exit(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @GetMapping("/index.html")
    public String index(Model model) {
        model.addAttribute("Material", service.getMaterial());
        return "index";
    }

    @ResponseBody
    @GetMapping("/setMenu")
    public String setMenu() {
        String result = service.setMenu();
        log.info("设置自定义菜单------->" + result);
        return result;
    }

    /**
     * 上传海报素材
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam(value = "inputGroupFile02", required = true) MultipartFile file, Model model) {
        //设置0为临时素材，设置1为永久素材
        int typeCode = 1;
        int result2 = 0;
        try {
            String result = weChatService.uploadMaterial(typeCode, CommonUtils.multipartFileToFile(file), "image");
            JSONObject jsonObject = JSONObject.parseObject(result);
            String err = result.substring(2, 9);
            if (err.equals("errcode")) {
                model.addAttribute("msg", jsonObject.getString("errmsg"));
                return "index";
            }
            Material material = JSONObject.parseObject(result, Material.class);
            if (material.getMediaId() == null) {
                model.addAttribute("msg", "本地数据库新增失败");
                return "index";
            }
            result2 = service.saveMaterial(material);
            if (result2 == 1) {
                return "redirect:/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }

    @ResponseBody
    @GetMapping(("/delmaterial"))
    public int delMaterial(@RequestParam(value = "mediaId", required = true) String mediaId) {
        int result2 = service.delMaterial(mediaId);
        String result = weChatService.delMaterial(mediaId);
        JSONObject jsonObject = JSONObject.parseObject(result);
        int errcode = jsonObject.getInteger("errcode");
        if (result2 == 1 && errcode == 0) {
            return 0;
        } else if (errcode != 0 && result2 == 1) {
            log.error("------------->Local material was removed successfully;The WeChat interface invokes an exception!" + errcode);
            return 0;
        } else if (result2 <= 0 && errcode == 0) {
            log.error("------------->Local material deletion failed!The WeChat interface call is normal");
            return 1;
        } else {
            log.error("------------->Local material deletion failed;The WeChat interface invokes an exception!" + errcode);
            return 1;
        }
    }

    /**
     * 登录
     *
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
