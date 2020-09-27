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
    public final static String MONGODB_COMMAND = "command";
    public final static String MONGODB_POSTER = "poster";
    public final static String MONGODB_CYCLE = "cycle";
    //活动期数
    public static int period;

    public static void setPeriod(int period) {
        CommonUtils.period = period;
    }


    /**
     * 返回一个短连接
     * @param url
     * @return
     */
    public static String shortUrl(String url) {
        // 可以自定义生成 MD5 加密字符传前的混合 KEY
        String key = "";
        // 要使用生成 URL 的字符
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z" };

        // 对传入网址进行 MD5 加密
        String sMD5EncryptResult = MD5Utils.MD5Encode(key + url,"utf8");
        String hex = sMD5EncryptResult;
        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);
            // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用
            // long ，则会越界
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars += chars[(int) index];
                // 每次循环按位右移 5 位
                lHexLong = lHexLong >> 5;
            }

            // 把字符串存入对应索引的输出数组
            resUrl[i] = outChars;
        }
        return resUrl[0]+resUrl[1];
    }

        /**
         * 生成口令代码
         * @return
         */
    public static String createCommand(){
        String val = "";
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
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
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 400,400,
                    hints);
        } catch (WriterException e) {
            e.printStackTrace();
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
            //y轴位置
            int y=height - (pWidth * 3-pWidth/10);
            g.drawImage(headImg, pWidth / 4, y, pWidth, pWidth, null);
            g.drawImage(qr, (pWidth + (pWidth / 3)), y, pWidth, pWidth, null);
            Font font = new Font("微软雅黑", Font.BOLD, 30);
            g.setFont(font);
            g.setPaint(Color.DARK_GRAY);
            if (nickname != null && nickname.length() > 10) {
                nickname = nickname.substring(0, 10);
                nickname = nickname + "..";
            }
            g.drawString(nickname, pWidth / 4, y+pWidth+pWidth/5);
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
