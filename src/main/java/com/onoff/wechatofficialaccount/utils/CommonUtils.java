package com.onoff.wechatofficialaccount.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.onoff.wechatofficialaccount.entity.DO.WeekData;
import com.onoff.wechatofficialaccount.entity.DO.WeekLeaderboard;
import com.onoff.wechatofficialaccount.entity.VO.Leaderboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/6/10 19:15
 * @VERSION 1.0
 **/
@Slf4j
public class CommonUtils {
    //后台管理员信息
    public final static String ADMIN_SESSION = "adminSession";
    public final static String MONGODB_WEEK = "weekleaderboard";
    public final static String MONGODB_MONTH = "monthleaderboard";
    public final static String MONGODB_SIGINQR = "siginqr";
    public final static String MONGODB_PROMOTIONQR = "promotionqr";
    public final static String MONGODB_CYCLE = "cycle";
    //活动期数
    public static int period;

    public static void setPeriod(int period) {
        CommonUtils.period = period;
    }


    /**
     * 计算截止日期并返回周期字符串
     *
     * @param startDate 当前活动（周/月）起始日期
     * @param type      0：周/1：月
     * @return
     */
    public static String setCycle(String startDate, int type) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime();
        if (type == 0) {
            time += 86400000 * (long) 6;
        }
        if (type == 1) {
            time += 86400000 * (long) 27;
        }
        String endingDate = formatter.format(time);
        String[] array = new String[]{startDate, endingDate};
        String month;
        String day;
        String cycle = "";
        for (int i = 0; i < array.length; i++) {
            //截取月份
            if (array[i].substring(5, 6).equals("0")) {
                month = array[i].substring(6, 7);
            } else {
                month = array[i].substring(5, 7);
            }
            //截取日期
            if (array[i].substring(8, 9).equals("0")) {
                day = array[i].substring(9, 10);
            } else {
                day = array[i].substring(8, 10);
            }
            if (i == 0) {
                cycle = month + "月" + day + "日-";
            } else {
                cycle += month + "月" + day + "日";
            }
        }
        return cycle;
    }

    /**
     * @param startDate 当前活动起始日期
     * @return 下期起始时间
     */
    public static String setStartDate(String startDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime();
        time += 86400000 * (long) 7;
        return formatter.format(time);
    }

    /**
     * 生成二维码
     *
     * @param content 源内容
     * @return 返回二维码图片
     * @throws Exception
     */
    public static BufferedImage createImage(String content) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 256, 256,
                    hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //去白边
        int[] rec = bitMatrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (bitMatrix.get(i + rec[0], j + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    public static List<WeekData> resetArray(List<WeekLeaderboard> array) {
        if (array == null) {
            return null;
        }
        List<WeekData> list = new ArrayList<>();
        if (array.size() > 0) {
            for (WeekLeaderboard w : array) {
                WeekData weekData;
                weekData = new WeekData(w.getId(), w.getPeriod() + "");
                list.add(weekData);
            }
            Collections.reverse(list);
        }
        return list;
    }

    /**
     * 设置名次并返回本人名次同时清空除本人之外用户的openid
     *
     * @param array
     * @param openId
     * @return
     */
    public static Leaderboard setRanking(List<Leaderboard> array, String openId) {
        Leaderboard Myleaderboard = null;
        for (int i = 0; i <= array.size() - 1; i++) {
            Leaderboard leader1 = array.get(i);
            leader1.setRanking(i + 1);
            if (leader1.getOpenId().equals(openId)) {
                Myleaderboard = leader1;
                array.set(i, leader1);
                continue;
            }
            leader1.setOpenId("");
            array.set(i, leader1);
        }
        return Myleaderboard;
    }

    /**
     * 设置名次
     *
     * @param array
     * @return
     */
    public static void setRanking(List<Leaderboard> array) {
        for (int i = 0; i <= array.size() - 1; i++) {
            Leaderboard leader1 = array.get(i);
            leader1.setRanking(i + 1);
            array.set(i, leader1);
        }
    }

    public static void delArrayOpenId(List<Leaderboard> array) {
        for (int i = 0; i <= array.size() - 1; i++) {
            Leaderboard leader1 = array.get(i);
            leader1.setOpenId("");
            array.set(i, leader1);
        }
    }

    /**
     * 排序(两个第一名没有第二名直接显示第三名)
     *
     * @param array
     * @param openId
     * @return
     */
    public static Leaderboard sort(List<Leaderboard> array, String openId) {
        Leaderboard Myleaderboard = null;
        if (array.size() == 1) {
            Leaderboard l = array.get(0);
            l.setRanking(1);
            array.set(0, l);
        }
        for (int i = 0; i < array.size() - 1; i++) {
            Leaderboard leader1 = array.get(i);
            Leaderboard leader2 = array.get(i + 1);
            if (i == 0) {
                leader1.setRanking(1);
            }
            if (leader1.getRecord() == leader2.getRecord()) {
                leader2.setRanking(leader1.getRanking());
            } else {
                leader2.setRanking((i + 1) + 1);
            }
            array.set(i, leader1);
            array.set(i + 1, leader2);
            if (leader1.getOpenId().equals(openId)) {
                Myleaderboard = leader1;
            }
            if (leader2.getOpenId().equals(openId)) {
                Myleaderboard = leader2;
            }
        }
        return Myleaderboard;
    }

    public static void sortArray(List<Leaderboard> array) {
        for (int i = 0; i < array.size() - 1; i++) {
            Leaderboard leader1 = array.get(i);
            Leaderboard leader2 = array.get(i + 1);
            if (i == 0) {
                leader1.setRanking(1);
            }
            if (leader1.getRecord() == leader2.getRecord()) {
                leader2.setRanking(leader1.getRanking());
            } else {
                leader2.setRanking((i + 1) + 1);
            }
            array.set(i, leader1);
            array.set(i + 1, leader2);
        }
    }

    /**
     * 拼图
     *
     * @param bigURL     背景图url
     * @param headImgURL 头像url
     * @param qr         二维码url
     * @param nickname   昵称
     */
    public static File overlapImage(String bigURL, String headImgURL, BufferedImage qr, String nickname) {
        try {
            URL bigUrl = new URL(bigURL);
            URL headUrl = new URL(headImgURL);
            //URL qrUrl = new URL(QR_URL);
            BufferedImage bigImg = ImageIO.read(bigUrl.openStream());
            BufferedImage headImg = ImageIO.read(headUrl.openStream());
            //BufferedImage qr = ImageIO.read(qrUrl.openStream());
            int width = bigImg.getWidth();
            int height = bigImg.getHeight();
            Image bigImage = bigImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedImage2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            Graphics2D g = bufferedImage2.createGraphics();
            g.drawImage(bigImage, 0, 0, null);
            //等比宽度
            int pWidth = width / 6;
            g.drawImage(headImg, width - (pWidth + pWidth / 5), height - (pWidth * 3 + pWidth / 20), pWidth, pWidth, null);
            g.drawImage(qr, width - (pWidth * 2 + (pWidth / 5) * 2), height - (pWidth * 3 + pWidth / 20), pWidth, pWidth, null);
            Font font = new Font("微软雅黑", Font.BOLD, 35);
            g.setFont(font);
            g.setPaint(Color.DARK_GRAY);
            if (nickname != null && nickname.length() > 10) {
                nickname = nickname.substring(0, 8);
                nickname = nickname + "..";
            }
            g.drawString(nickname, width - (pWidth * 2 + (pWidth / 5) * 2), height - (pWidth * 3 + pWidth / 5));
            g.dispose();
            File outputfile = File.createTempFile("img", ".jpg");
            ImageIO.write(bufferedImage2, "jpg", outputfile);
            return outputfile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断字符串是否为int
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 将MultipartFile转为File类型
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //            //查询邀请人总积分
//            int sum = bamService.getIntegral(relation.getOpenId(),0);
//            //查询邀请人本周积分
//            int periodSum = bamService.getIntegral(relation.getOpenId(),CommonUtils.period);
//            data = "{\n" +
//                    "    \"touser\":\"" + relation.getOpenId() + "\",\n" +
//                    "    \"msgtype\":\"text\",\n" +
//                    "    \"text\":\n" +
//                    "    {\n" +
//                    "         \"content\":\"你的好友 " + user.getNickName() + " 刚刚为你助力\n积分+10\n本周积分:" + periodSum + "季度积分:"+sum+"\"\n" +
//                    "    }\n" +
//                    "}";
//            errcode = kfSendMsg(data);
//            if (errcode == 0) {
//                log.info("客服成功发送消息给邀请人" + data);
//            } else {
//                log.error("客服发送消息给邀请人失败！邀请人openId：" + relation.getOpenId() + "发送数据：" + data + errcode);
//            }
//            data = "{\n" +
//                    "    \"touser\":\"" + user.getOpenId() + "\",\n" +
//                    "    \"msgtype\":\"text\",\n" +
//                    "    \"text\":\n" +
//                    "    {\n" +
//                    "         \"content\":\"你已帮助好友助力\"\n" +
//                    "    }\n" +
//                    "}";
//            errcode = kfSendMsg(data);
//            if (errcode == 0) {
//                log.info("----------------->客服成功发送消息给助力好友" + data);
//            } else {
//                log.error("---------------->客服发送消息给给助力好友失败！助力好友openId：" + user.getOpenId() + "发送数据：" + data + errcode);
//            }

    //            //查询邀请人总积分
//            int sum = bamService.getIntegral(relation.getOpenId(),0);
//            //查询邀请人本周积分
//            int periodSum = bamService.getIntegral(relation.getOpenId(),CommonUtils.period);
//            if (period == CommonUtils.period) {
//                data = "{\n" +
//                        "    \"touser\":\"" + inviterOpenId + "\",\n" +
//                        "    \"msgtype\":\"text\",\n" +
//                        "    \"text\":\n" +
//                        "    {\n" +
//                        "         \"content\":\"你的好友 " + user.getNickName() + " 取消了关注\n积分-10\n本周积分:" + periodSum + "季度积分:"+sum+ "\"\n" +
//                        "    }\n" +
//                        "}";
//            }else {
//                data = "{\n" +
//                        "    \"touser\":\"" + inviterOpenId + "\",\n" +
//                        "    \"msgtype\":\"text\",\n" +
//                        "    \"text\":\n" +
//                        "    {\n" +
//                        "         \"content\":\"你的好友 " + user.getNickName() + " 取消了关注\n季度积分-10，本周积分不变\n本周积分:" + periodSum + "季度积分:"+sum+ "\"\n" +
//                        "    }\n" +
//                        "}";
//            }
//
//            int errcode = kfSendMsg(data);
//            if (errcode == 0) {
//                log.info("----------------->通过邀请进入的用户首次取消关注，客服成功发送消息给邀请人" + data);
//            } else {
//                log.error("---------------->通过邀请进入的用户取消了关注，客服发送消息给邀请人失败！" + data + errcode);
//            }

}
