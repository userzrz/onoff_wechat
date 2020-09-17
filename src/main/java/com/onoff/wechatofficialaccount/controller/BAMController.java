package com.onoff.wechatofficialaccount.controller;

import com.alibaba.fastjson.JSONObject;
import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.entity.BAM.Integral;
import com.onoff.wechatofficialaccount.entity.BAM.Material;
import com.onoff.wechatofficialaccount.entity.DO.*;
import com.onoff.wechatofficialaccount.entity.Https;
import com.onoff.wechatofficialaccount.entity.User;
import com.onoff.wechatofficialaccount.entity.DO.Command;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
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
     * 异步查询历史榜单
     *
     * @param id
     * @param model
     * m：历史月 w:历史周 h:本月 k:本周
     * @return
     */
    @GetMapping("/past/{id}")
    public String getWeekRanking(@PathVariable("id") String id, Model model) {
        if (id==null||id.length()<5){
            return "remind";
        }
        String type = id.substring(id.length() - 1);
        id=id.substring(0,id.length()-1);
        WeekLeaderboard weekLeaderboard=new WeekLeaderboard();
        if(type.equals("k")){
            if (service.getUser(id) == null) {
                return "remind";
            }
            //获取排行榜
            List<Leaderboard> array = service.getLeaderboard(CommonUtils.period);
            Leaderboard Myleaderboard = CommonUtils.setRanking(array, id);
            //添加数据
            model.addAttribute("Leaderboard", array);
            if (Myleaderboard == null) {
                model.addAttribute("Relation", service.getRanking(id, CommonUtils.period));
            } else {
                model.addAttribute("Relation", Myleaderboard);
            }
            model.addAttribute("count", "本周总参与人数：" +service.getParticipants(CommonUtils.period));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            model.addAttribute("Date", "当前数据统计于：" + formatter.format(new Date()));
            //查询活动起始日期
            Cycle cycle=bamDao.queryCycle();
            model.addAttribute("Cycle","周榜第"+cycle.getWeekPeriod()+"期："+CommonUtils.setCycle(cycle.getWeekStartDate(),0));
            return "list::sup-sup";
        }else if (type.equals("h")){
            if (service.getUser(id) == null) {
                return "remind";
            }
            //获取月榜
            List<Leaderboard> array = service.getLeaderboard(0);
            Leaderboard Myleaderboard = CommonUtils.setRanking(array, id);
            //添加数据
            model.addAttribute("Leaderboard2", array);
            if (Myleaderboard == null) {
                model.addAttribute("Relation2", service.getRanking(id, 0));
            } else {
                model.addAttribute("Relation2", Myleaderboard);
            }
            model.addAttribute("count2", "本月总参与人数：" + service.getParticipants(0));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            model.addAttribute("Date", "当前数据统计于：" + formatter.format(new Date()));
            //查询活动起始日期
            Cycle cycle=bamDao.queryCycle();
            model.addAttribute("Cycle","月榜第"+cycle.getMonthPeriod()+"期："+CommonUtils.setCycle(cycle.getMonthStartDate(),1));
            return "list::sup-sup2";
        }else if (type.equals("w")){
             weekLeaderboard=bamDao.queryLeaderboard(id,CommonUtils.MONGODB_WEEK);
             CommonUtils.delArrayOpenId(weekLeaderboard.getLeaderboards());
            //添加数据
            model.addAttribute("Leaderboard", weekLeaderboard.getLeaderboards());
            model.addAttribute("count", "本周总参与人数：" + weekLeaderboard.getCount());
            model.addAttribute("Date", "当前数据统计于：" + weekLeaderboard.getDate());
            model.addAttribute("Cycle",weekLeaderboard.getCycle());
            return "list::sup-sup";
        }else if (type.equals("m")){
             weekLeaderboard=bamDao.queryLeaderboard(id,CommonUtils.MONGODB_MONTH);
            CommonUtils.delArrayOpenId(weekLeaderboard.getLeaderboards());
            //添加数据
            model.addAttribute("Leaderboard2", weekLeaderboard.getLeaderboards());
            model.addAttribute("count2", "本月总参与人数：" + weekLeaderboard.getCount());
            model.addAttribute("Date", "当前数据统计于：" + weekLeaderboard.getDate());
            model.addAttribute("Cycle",weekLeaderboard.getCycle());
            return "list::sup-sup2";
        }
        if (weekLeaderboard==null){
            return "remind";
        }
        return "remind";
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
        List<WeekData> weekPeriods;
        List<WeekData> monthPeriods;
        User user = service.getUser(openId);
        if (user == null) {
            return "remind";
        }
        //新增用户积分
        weChatService.initializeIntegral(openId);
        //查询用户打卡记录
        List<SignIn> signIns = service.querySignIn(user.getUnionId(), null);
        if (signIns.size() != 0) {
            Integral integral;
            //保存用户打卡记录
            for (SignIn signIn : signIns) {
                if (signIn.getQr_type() == 0) {
                    long time = signIn.getTime() - signIn.getQr_time();
                    if (time >= 86400000) {
                        //给用户添加打卡积分
                        integral = new Integral(user.getOpenId(), 9, 3, signIn.getTime().toString(), signIn.getPeriod());
                    } else {
                        integral = new Integral(user.getOpenId(), 18, 3, signIn.getTime().toString(), signIn.getPeriod());
                    }
                    int result = service.saveIntegral(integral);
                    if (result == 1) {
                        log.info("用户打卡加分成功" + signIn.toString());
                    } else {
                        log.error("用户打卡加分失败文章发表后" + time + "毫秒打卡" + signIn.toString());
                    }
                } else if (signIn.getQr_type() == 1) {
                    PromotionQR promotionQR = bamDao.queryPromotionQR(signIn.getQr_time());
                    integral = new Integral(user.getOpenId(), promotionQR.getIntegral(), 4, signIn.getTime().toString(), signIn.getPeriod());
                    int result = service.saveIntegral(integral);
                    if (result == 1) {
                        log.info("用户扫描推广码加分成功" + signIn.toString());
                    } else {
                        log.error("用户扫描推广码加分失败" + signIn.toString());
                    }
                }
            }
            //修改用户打卡类型
            service.putSignIn(user.getUnionId(), null);
        }
        //获取数据
        List<WeekLeaderboard> weeks = bamDao.queryLeaderboardAll(CommonUtils.MONGODB_WEEK);
        List<WeekLeaderboard> months = bamDao.queryLeaderboardAll(CommonUtils.MONGODB_MONTH);
        if (weeks.size()==0){
            model.addAttribute("week", null);
        }else {
            weekPeriods=CommonUtils.resetArray(weeks);
            model.addAttribute("week", weekPeriods);
        }
        if (months.size()==0){
            model.addAttribute("month", null);
        }else {
            monthPeriods=CommonUtils.resetArray(months);
            model.addAttribute("month", monthPeriods);
        }
        model.addAttribute("Period", CommonUtils.period);
        model.addAttribute("Code", openId);
        //查询奖品图
        model.addAttribute("Material", service.getMaterial("1"));
        //获取本周排行榜
        List<Leaderboard> array = service.getLeaderboard(CommonUtils.period);
        Leaderboard Myleaderboard = CommonUtils.setRanking(array, openId);
        //添加数据
        model.addAttribute("Leaderboard", array);
        //添加个人信息
        if (Myleaderboard == null) {
            model.addAttribute("Relation", service.getRanking(openId, CommonUtils.period));
        } else {
            model.addAttribute("Relation", Myleaderboard);
        }
        //本月排行榜
        List<Leaderboard> array2 = service.getLeaderboard(0);
        Leaderboard Myleaderboard2 = CommonUtils.setRanking(array2, openId);
        //添加数据
        model.addAttribute("Leaderboard2", array2);
        if (Myleaderboard2 == null) {
            model.addAttribute("Relation2", service.getRanking(openId, 0));
        } else {
            model.addAttribute("Relation2", Myleaderboard2);
        }
        //获取本周参与人数
        int count = service.getParticipants(CommonUtils.period);
        //获取本月参与人数
        int count2 = service.getParticipants(0);
        model.addAttribute("count", "本周总参与人数：" + count);
        model.addAttribute("count2", "本月总参与人数：" + count2);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        model.addAttribute("Date", "当前数据统计于：" + formatter.format(new Date()));
        //查询活动起始日期
        Cycle cycle=bamDao.queryCycle();
        model.addAttribute("weekCycle","周榜第"+cycle.getWeekPeriod()+"期："+CommonUtils.setCycle(cycle.getWeekStartDate(),0));
        model.addAttribute("monthCycle","月榜第"+cycle.getMonthPeriod()+"期："+CommonUtils.setCycle(cycle.getMonthStartDate(),1));
        return "list";
    }


    /**
     * 扫码获取积分
     *
     * @return
     */
    @GetMapping("/sign_in")
    public String signIn(@RequestParam(value = "state") String state, @RequestParam(value = "code") String code, Model model) {
        PromotionQR promotionQR = null;
        SignInQR signInQR = bamDao.querySignInQR(state);
        if (signInQR == null) {
            promotionQR = bamDao.queryPromotionQR(state);
        }
        if (signInQR == null && promotionQR == null) {
            model.addAttribute("msg", "无效二维码");
            return "sign_in";
        }
        if (signInQR != null) {
            if (signInQR.getOverTime() < System.currentTimeMillis()) {
                model.addAttribute("msg", "二维码已失效（超过设定时间）");
                return "sign_in";
            }
        }
        if (promotionQR != null) {
            //计算二维码过期时间
            Long validTime = promotionQR.getDays() * 86400000 + promotionQR.getTime();
            if (validTime < System.currentTimeMillis()) {
                model.addAttribute("msg", "二维码已失效（超过设定时间）");
                return "sign_in";
            }
            int count = service.countSignIn(promotionQR.getTime() + "");
            if (count >= promotionQR.getMaxUser()) {
                model.addAttribute("msg", "扫码人数已达上限");
                return "sign_in";
            }
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
        if (signInQR != null) {
            //验证是否已打卡
            List<SignIn> signIns = service.querySignIn(unionid, signInQR.getReleaseTime() + "");
            if (signIns.size() == 0) {
                //存储打卡信息
                SignIn signIn = new SignIn(unionid, System.currentTimeMillis(), signInQR.getReleaseTime(), CommonUtils.period, 0);
                int result = service.saveSignIn(signIn);
                if (result > 0) {
                    long time = signIn.getTime() - signIn.getQr_time();
                    if (time >= 86400000) {
                        log.info("用户打卡成功积分+9；用户unionid=" + unionid);
                        model.addAttribute("msg", "打卡成功积分+9");
                    } else {
                        log.info("用户打卡成功积分+18；用户unionid=" + unionid);
                        model.addAttribute("msg", "打卡成功积分+18");
                    }
                } else {
                    log.error("用户打卡失败;用户unionid=" + unionid);
                }
            } else {
                log.info("用户重复扫描打卡码");
                model.addAttribute("msg", "重复打卡无效哦！");
                return "sign_in";
            }
        } else {
            //验证是否已打卡
            List<SignIn> signIns = service.querySignIn(unionid, promotionQR.getTime() + "");
            if (signIns.size() == 0) {
                //存储打卡信息
                SignIn signIn = new SignIn(unionid, System.currentTimeMillis(), promotionQR.getTime(), CommonUtils.period, 1);
                int result = service.saveSignIn(signIn);
                if (result > 0) {
                    log.info("用户扫描推广码成功积分+" + promotionQR.getIntegral() + "；用户unionid=" + unionid);
                    model.addAttribute("msg", "扫描推广码积分+"+ promotionQR.getIntegral());
                } else {
                    log.error("用户推广sign_in表积分建立失败;用户unionid=" + unionid + "积分应加" + promotionQR.getIntegral());
                }
            } else {
                log.info("用户重复扫描推广码");
                model.addAttribute("msg", "重复扫码无效哦！");
                return "sign_in";
            }
        }
        return "sign_in";
    }

    /**
     * 生成文章打卡二维码
     *
     * @param releaseTime 发表文章时间
     * @param days        发表文章后有效天数
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/maker")
    public void makerQR(@RequestParam("releaseTime") String releaseTime, @RequestParam("days") Long days, HttpServletRequest request, HttpServletResponse response) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        releaseTime = releaseTime + " 21:30:00";
        Date date = null;
        try {
            date = simpleDateFormat.parse(releaseTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //发表文章时间
        releaseTime = date.getTime() + "";
        //二维码过期时间
        Long overTime = Long.parseLong(releaseTime) + days * 86400000;
        String id = MD5Utils.MD5Encode(System.currentTimeMillis() + "", "utf8");
        bamDao.saveSignInQR(new SignInQR(id, Long.parseLong(releaseTime), overTime));
        String http = service.generateHttp(id, Https.signIn_uri, "snsapi_userinfo");
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

    /**
     * 生成推广二维码
     *
     * @param maxUser  上限人数
     * @param integral 设置的积分
     * @param days2    有效天数
     * @param response
     * @throws IOException
     */
    @PostMapping("/promotion")
    public void promotionQR(@RequestParam("maxUser") int maxUser, @RequestParam("integral") int integral, @RequestParam("days2") Long days2, HttpServletResponse response) throws IOException {
        String id = MD5Utils.MD5Encode(System.currentTimeMillis() + "", "utf8");
        bamDao.savePromotionQR(new PromotionQR(id, maxUser, integral, days2, System.currentTimeMillis()));
        String http = service.generateHttp(id, Https.signIn_uri, "snsapi_userinfo");
        //生成二维码
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

    /**
     *
     */
    @ResponseBody
    @PostMapping("/command")
    public String command(@RequestParam("maxUser") int maxUser, @RequestParam("integral") int integral, @RequestParam("days2") Long days2,@RequestParam("remark") String remark){
        String code=CommonUtils.createCommand();
        bamDao.saveCommand(new Command(code, maxUser, integral, days2, System.currentTimeMillis(),remark));
        return "口令为："+code;
    }

    @GetMapping({"/onoff_login.html"})
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
//        //新用户来源渠道
//        List<UserScene> userScenes = service.queryScene();
//        model.addAttribute("userScenes", userScenes);
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
            String err = result.substring(2, 9);
            if (err.equals("errcode")) {
                model.addAttribute("msg", result);
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
     * @param model
     * @return
     */
    @PostMapping(value = "/login/verify")
    public String loginVerify(@ModelAttribute Admin admin, HttpSession session,Model model) {
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


    @GetMapping(value = "/rule.html")
    public String  points(){
        return "rule";
    }

    /**
     *如何获取更多积分
     * @return
     */
    @GetMapping(value = "/explain.html")
    public String  explain(){
        return "explain";
    }

    /**
     * 联系我们
     * @return
     */
    @GetMapping(value = "/contact_us.html")
    public String  contact_us(){
        return "contact_us";
    }
}
