package com.mdmd.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mdmd.controller.GameAction;
import com.mdmd.entity.QrcodeEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;

import static com.mdmd.constant.ActionConstant.QRCODE_URL;

public class QrcodeUtil {
//    private static final String QRCODE_URL = "http://www.miaojieshan.com/wx/routerToMyPage.do?token=";
    private static Properties prop = null;
    private static final Logger LOGGER = LogManager.getLogger(QrcodeUtil.class);

    //给了个默认配置，在读取配置文件失败时使用
    private static String qr_format = "jpg";
    private static String qr_path = "/usr/local/happyfish/stc/qrcode";
    private static int qr_whitelength = 3;//边框
    private static int qr_length = 155;//长度
    private static String modeImg_path = "/usr/local/happyfish/qrcode_mode.jpg";
    static {
        prop = new Properties();
        InputStream in = QrcodeUtil.class.getClassLoader().getResourceAsStream( "qrcode.properties" );

        try {
            prop.load(in);
            qr_format = prop.getProperty("qr_format");
            qr_path = prop.getProperty("qr_path");
            qr_whitelength = Integer.valueOf(prop.getProperty("qr_whitelength"));
            qr_length = Integer.valueOf(prop.getProperty("qr_length"));
            modeImg_path = prop.getProperty("modeImg_path");
        } catch (Exception e) {
           LOGGER.info("初始化二维码配置失败" + e.getMessage());
           System.exit(0);
        }
    }

    /**
     * 将生成的推广二维码植入另一模板图片中
     * @param fileName
     * @param data
     */
    public static void mergeImageAndDrawQrcode(String fileName, String data) {
        LOGGER.info("为用户"+data+"创建推广码");
        //二维码嵌入的定位
        int x = 140,y=418;
        try {
            ErrorCorrectionLevel level = ErrorCorrectionLevel.H;
            System.out.println("进来了--"+qr_format+qr_path+"" + qr_whitelength +"" +qr_length+"--");
            String qrName = fileName + "." + qr_format; //生成二维码图片名称
            File target = new File(qr_path, qrName);
            if(!target.exists()){
                target.mkdirs();
            }
            //生成二维码中的设置
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); //编码
            hints.put(EncodeHintType.ERROR_CORRECTION, level); //容错率
            hints.put(EncodeHintType.MARGIN, 0); //二维码边框宽度，这里文档说设置0-4，但是设置后没有效果，不知原因，

            String content = QRCODE_URL+data;

            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qr_length, qr_length,hints); //生成bitMatrix

            bitMatrix = updateBit(bitMatrix, qr_whitelength); //生成新的bitMatrix

            //因为二维码生成时，白边无法控制，去掉原有的白边，再添加自定义白边后，二维码大小与size大小就存在差异了，为了让新生成的二维码大小还是size大小，根据size重新生成图片

            BufferedImage bi = MatrixToImageWriter.toBufferedImage(bitMatrix);

            BufferedImage small = zoomInImage(bi,qr_length,qr_length);//根据size放大、缩小生成的二维码

            BufferedImage big = ImageIO.read(new File(modeImg_path));

            Graphics2D g = big.createGraphics();
            g.setColor(new Color(220,197,232));
            g.setFont(new Font("fantasy", Font.BOLD, 23));
            g.drawString(data,90,46);
            g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
            g.dispose();
            ImageIO.write(big, "jpg", target);
        } catch (Exception e) {
            LOGGER.warn("为用户"+data+"创建推广码失败!!" + e.getMessage());
        }

    }


    /**
     * 将生成的推广二维码植入另一模板图片中 返回输出流中
     * @param fileName
     * @param data
     * @param outputStream
     */
    public static void mergeImageAndDrawQrcode_outputStream(String fileName,String data, OutputStream outputStream) {
        LOGGER.info("为用户"+data+"创建推广码");
        //二维码嵌入的定位
        int x = 140,y=418;
        try {
            ErrorCorrectionLevel level = ErrorCorrectionLevel.H;
            System.out.println("进来了--"+qr_format+qr_path+"" + qr_whitelength +"" +qr_length+"--");

            //生成二维码中的设置
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); //编码
            hints.put(EncodeHintType.ERROR_CORRECTION, level); //容错率
            hints.put(EncodeHintType.MARGIN, 0); //二维码边框宽度，这里文档说设置0-4，但是设置后没有效果，不知原因，

            String content = QRCODE_URL+data;

            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qr_length, qr_length,hints); //生成bitMatrix

            bitMatrix = updateBit(bitMatrix, qr_whitelength); //生成新的bitMatrix

            //因为二维码生成时，白边无法控制，去掉原有的白边，再添加自定义白边后，二维码大小与size大小就存在差异了，为了让新生成的二维码大小还是size大小，根据size重新生成图片

            BufferedImage bi = MatrixToImageWriter.toBufferedImage(bitMatrix);

            BufferedImage small = zoomInImage(bi,qr_length,qr_length);//根据size放大、缩小生成的二维码

            BufferedImage big = ImageIO.read(new File(modeImg_path));

            Graphics2D g = big.createGraphics();
            g.setColor(new Color(220,197,232));
            g.setFont(new Font("fantasy", Font.BOLD, 23));
            g.drawString(data,90,46);
            g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
            g.dispose();
            ImageIO.write(big, "jpg", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("为用户"+data+"创建推广码失败!!" + e.getMessage());
        }

    }
    /**
     * 生成二维码
     * @param fileName
     * @param data
     * @throws IOException
     * @throws WriterException
     */
    public static void createQrcode(String fileName,String data) throws IOException, WriterException {
        ErrorCorrectionLevel level = ErrorCorrectionLevel.H;
         String qrName = fileName + "." + qr_format; //生成二维码图片名称
         File target = new File(qr_path, qrName);
         if(!target.exists()){
            target.mkdirs();
         }
        //生成二维码中的设置
         Hashtable hints = new Hashtable();
         hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); //编码
         hints.put(EncodeHintType.ERROR_CORRECTION, level); //容错率
         hints.put(EncodeHintType.MARGIN, 0); //二维码边框宽度，这里文档说设置0-4，但是设置后没有效果，不知原因，

         String content = QRCODE_URL+data;


         BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qr_length, qr_length,hints); //生成bitMatrix


         bitMatrix = updateBit(bitMatrix, qr_whitelength); //生成新的bitMatrix

         //因为二维码生成时，白边无法控制，去掉原有的白边，再添加自定义白边后，二维码大小与size大小就存在差异了，为了让新生成的二维码大小还是size大小，根据size重新生成图片

         BufferedImage bi = MatrixToImageWriter.toBufferedImage(bitMatrix);
        bi = zoomInImage(bi,qr_length,qr_length);//根据size放大、缩小生成的二维码
        ImageIO.write(bi, qr_format, target); //生成二维码图片

    }
    private static BitMatrix updateBit(BitMatrix matrix, int margin){
         int tempM = margin*2;
         int[] rec = matrix.getEnclosingRectangle(); //获取二维码图案的属性
         int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
         BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
         resMatrix.clear();
        for(int i= margin; i < resWidth- margin; i++){ //循环，将二维码图案绘制到新的bitMatrix中
         for(int j=margin; j < resHeight-margin; j++){
        if(matrix.get(i-margin + rec[0], j-margin + rec[1])){
         resMatrix.set(i,j);
         }
         }
        }
        return resMatrix;
    }
    private static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height){
        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
         Graphics g = newImage.getGraphics();
         g.drawImage(originalImage, 0,0,width,height,null);
         g.dispose();

            return newImage;

    }


}
