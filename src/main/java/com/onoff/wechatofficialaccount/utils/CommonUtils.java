package com.onoff.wechatofficialaccount.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/6/10 19:15
 * @VERSION 1.0
 **/
public class CommonUtils {

    //聊天机器人Appkey
    public static final String APPKEY = "c88a5156428825ddbb0e78fca4ca1e32";
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //后台管理员信息
    public final static String ADMIN_SESSION = "adminSession";


    /**
     * 拼图
     * @param bigURL    背景图url
     * @param headImgURL 头像url
     * @param QR_URL    二维码url
     * @param nickname  昵称
     */
    public static File overlapImage(String bigURL, String headImgURL, String QR_URL,String nickname) {
        try {
            URL bigUrl = new URL(bigURL);
            URL imgUrl = new URL(headImgURL);
            URL qrUrl = new URL(QR_URL);
            BufferedImage big = ImageIO.read(bigUrl.openStream());
            BufferedImage img = ImageIO.read(imgUrl.openStream());
            BufferedImage qr = ImageIO.read(qrUrl.openStream());
            int width = 600;
            int height = 900;
            Image image = big.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedImage2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            Graphics2D g = bufferedImage2.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.drawImage(img, 20, 720, 150, 150, null);
            g.drawImage(qr, 330, 630, 250, 250, null);
            Font font = new Font("宋体", Font.BOLD, 30);
            g.setFont(font);
            g.setPaint(Color.DARK_GRAY);
            g.drawString(nickname, 20, 700);
            g.dispose();
            File outputfile = new File("image.jpg");
            ImageIO.write(bufferedImage2, "jpg", outputfile);
            //ImageIO.write(bufferedImage2, "jpg", new File("C:\\Users\\lev\\Pictures\\7.8\\hehe.jpg"));
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

    /**
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    // 将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
