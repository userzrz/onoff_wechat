package com.onoff.wechatofficialaccount.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Hashtable;
/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/6/10 19:15
 * @VERSION 1.0
 **/
public class CommonUtils {
    //后台管理员信息
    public final static String ADMIN_SESSION = "adminSession";
    //海报活动介绍内容
    public static String intro = "";



    /**
     * 生成二维码
     *
     * @param content 源内容
     * @return 返回二维码图片
     * @throws Exception
     */
    public static BufferedImage createImage(String content) {
        Hashtable<EncodeHintType,Object> hints = new Hashtable<EncodeHintType,Object>();
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
            int pWidth = width / 5;
            int pWidthQR = width / 3;
            //等比高度
            int pHeight = height / 5;
            g.drawImage(headImg, pWidth / 2, height - (pWidth + pWidth / 2), pWidth, pWidth, null);
            g.drawImage(qr, width - (pWidthQR + pWidth / 2), height - (pWidthQR + pWidth / 2), pWidthQR, pWidthQR, null);
            Font font = new Font("宋体", Font.PLAIN, 30);
            g.setFont(font);
            g.setPaint(Color.DARK_GRAY);
            g.drawString(nickname, pWidth / 2, height - (pWidth + pWidth / 2 + pWidth / 4));
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

}
