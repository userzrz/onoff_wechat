package com.onoff.wechatofficialaccount.controller;

import com.alibaba.fastjson.JSONObject;
import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Integral;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.entity.DO.SignIn;
import com.onoff.wechatofficialaccount.entity.DO.WeekData;
import com.onoff.wechatofficialaccount.entity.DO.WeekLeaderboard;
import com.onoff.wechatofficialaccount.entity.Https;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.entity.UserScene;
import com.onoff.wechatofficialaccount.entity.VO.Leaderboard;
import com.onoff.wechatofficialaccount.mapper.DAO.BAMDao;
import com.onoff.wechatofficialaccount.service.BAMService;
import com.onoff.wechatofficialaccount.service.WeChatService;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import com.onoff.wechatofficialaccount.utils.MD5Utils;
import com.onoff.wechatofficialaccount.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    BAMDao bamDao;

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
        } else if (result == 4) {
            log.info("--------->本人扫描无需建立关系---");
        } else {
            log.info("--------->用户关系建立成功");
        }
        return "middle";
    }

    /**
     * 查询积分
     *
     * @param code
     * @param model
     * @return
     */
    @GetMapping("/record/{code}")
    public String record(@PathVariable("code") String code, Model model) {
        if (service.getUser(code) == null) {
            return "remind";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        List<Integral> integrals = service.getIntegralrecord(code);
        List<Integral> weekIntegrals = new ArrayList<>();
        int sum = 0;
        int weeksum = 0;
        for (int i = 0; i < integrals.size(); i++) {
            Integral integral = integrals.get(i);
            String time = formatter.format(new Date(Long.parseLong(integral.getTime())));
            integral.setTime(time);
            if (integral.getPeriod() == CommonUtils.period) {
                weekIntegrals.add(integral);
                weeksum += integral.getRecord();
            }
            sum += integral.getRecord();
            integrals.set(i, integral);
        }
        model.addAttribute("Record", integrals);
        model.addAttribute("weekRecord", weekIntegrals);
        model.addAttribute("sum", sum);
        model.addAttribute("weeksum", weeksum);
        return "record";
    }

    @GetMapping("/month/{code}")
    public String month(@PathVariable("code") String openId, Model model) {
        User user = service.getUser(openId);
        if (user == null) {
            return "remind";
        }
        return "";
    }

    /**
     * 异步查询历史周榜
     *
     * @param period
     * @param model
     * @return
     */
    @GetMapping("/week/{period}")
    public String week(@PathVariable("period") String period, Model model) {
        String code = "";
        if (period.length() > 10) {
            code = period.substring(period.length() - 5);
        }
        if (code.equals("month")) {
            String openId = period.substring(0, period.length() - 5);
            //获取月榜
            List<Leaderboard> array = service.getLeaderboard(0);
            Leaderboard Myleaderboard = CommonUtils.setRanking(array, openId);
            //添加数据
            model.addAttribute("Leaderboard", array);
            if (Myleaderboard == null) {
                model.addAttribute("Relation", service.getRanking(openId, 0));
            } else {
                model.addAttribute("Relation", Myleaderboard);
            }
            model.addAttribute("historyPeriod", "本月");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            model.addAttribute("Date", "当前数据统计于：" + formatter.format(new Date()));
            return "list::sup-sup";
        }
        if(code.equals("weeks")) {
            String id = period.substring(0, period.length() - 5);
            //获取排行榜
            WeekLeaderboard weekLeaderboard = bamDao.queryLeaderboard(id);
            if (weekLeaderboard == null) {
                return "remind";
            }
            //添加数据
            model.addAttribute("Leaderboard", weekLeaderboard.getLeaderboards());
            model.addAttribute("Date", "当前数据统计于：" + weekLeaderboard.getDate());
            model.addAttribute("historyPeriod", "历史");
            return "list::sup-sup";
        }
        if (period.equals("month")) {
            //获取排行榜
            WeekLeaderboard weekLeaderboard = bamDao.queryLeaderboardAll(CommonUtils.mongodb_month).get(0);
            if (weekLeaderboard == null) {
                return "remind";
            }
            //添加数据
            model.addAttribute("Leaderboard", weekLeaderboard.getLeaderboards());
            model.addAttribute("Date", "当前数据统计于：" + weekLeaderboard.getDate());
            model.addAttribute("historyPeriod", "上月");
            return "list::sup-sup";
        }

        if (period.length() > 10) {
            String openId = period;
            if (service.getUser(openId) == null) {
                return "remind";
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            model.addAttribute("Date", "当前数据统计于：" + formatter.format(new Date()));
            //获取排行榜
            List<Leaderboard> array = service.getLeaderboard(CommonUtils.period);
            Leaderboard Myleaderboard = CommonUtils.setRanking(array, openId);
            //添加数据
            model.addAttribute("Leaderboard", array);
            if (Myleaderboard == null) {
                model.addAttribute("Relation", service.getRanking(openId, CommonUtils.period));
            } else {
                model.addAttribute("Relation", Myleaderboard);
            }
        }
        return "list::sup-sup";
    }

    /**
     * list主页信息
     *
     * @param openId
     * @param model
     * @return
     */
    @GetMapping("/list.html/{code}")
    public String Leaderboard(@PathVariable("code") String openId, Model model) {
        User user = service.getUser(openId);
        if (user == null) {
            return "remind";
        }
        //新增用户积分
        weChatService.initializeIntegral(openId);
        //查询用户打卡记录
        List<SignIn> signIns = service.querySignIn(user.getUnionId(), null);
        if (signIns != null) {
            //保存用户打卡记录
            for (SignIn signIn : signIns) {
                //给用户添加打卡积分
                Integral integral = new Integral(user.getOpenId(), 10, 3, signIn.getTime().toString(), signIn.getPeriod());
                int result = service.saveIntegral(integral);
                if (result == 1) {
                    log.info("用户打卡加分成功");
                } else {
                    log.error("用户打卡加分成功失败用户openId:" + openId + "应加10分");
                }
            }
            //修改用户打卡类型
            service.putSignIn(user.getUnionId());
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        model.addAttribute("Date", "当前数据统计于：" + formatter.format(new Date()));
        //获取数据
        List<WeekLeaderboard> weeks = bamDao.queryLeaderboardAll(CommonUtils.mongodb_week);
        int month_siez = bamDao.queryLeaderboardAll(CommonUtils.mongodb_month).size();
        if (weeks.size() > 0) {
            List<WeekData> list = new ArrayList<>();
            for (WeekLeaderboard w : weeks) {
                WeekData weekData=new WeekData(w.getId(),w.getDate().substring(5,9));
                list.add(weekData);
            }
            model.addAttribute("week", list);
        }
        if (month_siez > 0) {
            model.addAttribute("month", 1);
        }
        model.addAttribute("Period", CommonUtils.period);
        model.addAttribute("Code", openId);
        //查询奖品图
        model.addAttribute("Material", service.getMaterial("1"));
        //获取排行榜
        List<Leaderboard> array = service.getLeaderboard(CommonUtils.period);
        Leaderboard Myleaderboard = CommonUtils.setRanking(array, openId);
        //添加数据
        model.addAttribute("Leaderboard", array);
        if (Myleaderboard == null) {
            model.addAttribute("Relation", service.getRanking(openId, CommonUtils.period));
        } else {
            model.addAttribute("Relation", Myleaderboard);
        }
        return "list";
    }


    /**
     * 打卡
     *
     * @return
     */
    @GetMapping("/sign_in")
    public String signIn(@RequestParam(value = "state") String state, @RequestParam(value = "code") String code, Model model) {
        String limit = state.substring(14);
        String qr_time = state.substring(0, 13);

        if (Long.parseLong(qr_time) < System.currentTimeMillis()) {
            model.addAttribute("msg", "二维码已失效（超过设定时间）");
            return "sign_in";
        }
        int count = service.countSignIn(qr_time);
        if (count >= Integer.parseInt(limit)) {
            model.addAttribute("msg", "二维码已失效（超出打卡上限人数)");
            return "sign_in";
        }
        //第一步：使用code换取access_token及用户openId
        String url = Https.tokenHttps;
        url = url.replace("APPID", WeChatUtils.FWAPPID)
                .replace("SECRET", WeChatUtils.FWAPPSECRET)
                .replace("CODE", code);
        String data = WeChatUtils.get(url);
        JSONObject jsonObject = JSONObject.parseObject(data);
        //第二步：拉取用户信息
        url = Https.webUserInfoHttps;
        url = url.replace("ACCESS_TOKEN", jsonObject.getString("access_token"))
                .replace("OPENID", jsonObject.getString("openid"));
        data = WeChatUtils.get(url);
        log.info("网页授权获取到的用户信息-->" + data);
        jsonObject = JSONObject.parseObject(data);
        //取出unionid
        String unionid = jsonObject.getString("unionid");
        //验证是否已打卡
        List<SignIn> signIns = service.querySignIn(unionid, qr_time);
        if (signIns.size() == 0) {
            //存储打卡信息
            SignIn signIn = new SignIn(unionid, System.currentTimeMillis(), Long.parseLong(qr_time), CommonUtils.period);
            int result = service.saveSignIn(signIn);
            if (result > 0) {
                log.info("----->用户打卡成功");
                model.addAttribute("msg", "打卡成功积分+10！");
            } else {
                log.error("---->用户打卡失败;用户unionid=" + unionid);
            }
        } else {
            log.info("----------用户重复打卡");
            model.addAttribute("msg", "重复打卡无效哦！");
            return "sign_in";
        }
        return "sign_in";
    }

    /**
     * 生成打卡二维码
     *
     * @param date
     * @return
     */
    @GetMapping("/maker")
    @ResponseBody
    public void makerQR(@RequestParam("date") int date, @RequestParam("limit") int limit, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long time = System.currentTimeMillis();
        time = time + (date * 86400000);
        String http = service.generateHttp(time + "_" + limit, Https.signIn_uri, "snsapi_userinfo");
        //生成打卡二维码
        BufferedImage qrImg = CommonUtils.createImage(http);
        //BufferedImage 转 InputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
        ImageIO.write(qrImg, "png", imageOutput);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        long length = imageOutput.length();

        //设置response
        response.setContentType("application/x-msdownload");
        response.setContentLength((int) length);
        response.setHeader("Content-Disposition", "attachment;filename=" + new String("clock.png".getBytes("gbk"), "iso-8859-1"));

        //输出流
        byte[] bytes = new byte[1024];
        OutputStream outputStream = response.getOutputStream();
        long count = 0;
        while (count < length) {
            int len = inputStream.read(bytes, 0, 1024);
            count += len;
            outputStream.write(bytes, 0, len);
        }
        outputStream.flush();
    }

    @GetMapping({"/", "/login.html"})
    public String login(HttpSession session) {
        if (session.getAttribute(CommonUtils.ADMIN_SESSION) != null) {
            return "redirect:/index.html";
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
        //新用户来源渠道
        List<UserScene> userScenes = service.queryScene();
        model.addAttribute("userScenes", userScenes);
        model.addAttribute("Material", service.getMaterial(null));
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
     * 上传素材
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam(value = "inputGroupFile02", required = true) MultipartFile file, @RequestParam("imageType") int imageType, Model model) {
        //设置0为临时素材，设置1为永久素材
        int typeCode = 1;
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
            material.setType(imageType);
            int result2 = service.saveMaterial(material);
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
