import com.google.zxing.WriterException;
import com.mdmd.constant.WeiXinPublicContant;
import com.mdmd.entity.bean.Transfers;
import com.mdmd.util.MD5Util;
import com.mdmd.util.QrcodeUtil;
import com.mdmd.util.WeixinConfigUtils;
import com.sun.prism.Graphics;
import org.jdom2.JDOMException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Test {

    public static void main(String[] args) throws ParseException {
        String s = "<xml>\n" +
                "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "<return_msg><![CDATA[]]></return_msg>\n" +
                "<mch_appid><![CDATA[wxf16e677138970e0a]]></mch_appid>\n" +
                "<mchid><![CDATA[1495156732]]></mchid>\n" +
                "<nonce_str><![CDATA[IQ0kbZB5evY5ZPIa]]></nonce_str>\n" +
                "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
                "<partner_trade_no><![CDATA[wx3108033453254747733133]]></partner_trade_no>\n" +
                "<payment_no><![CDATA[1495156732201902262714829855]]></payment_no>\n" +
                "<payment_time><![CDATA[2019-02-26 23:06:35]]></payment_time>\n" +
                "</xml>";
        try {
            Map<String, String> stringStringMap = WeixinConfigUtils.parseRefundXml(s);
            System.out.println(stringStringMap);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mergeImage(String bigPath,String smallPath,int x,int y) throws IOException {

        try {
            BufferedImage small ;
            BufferedImage big = ImageIO.read(new File(bigPath));
            if(smallPath.contains("http")){

                URL url = new URL(smallPath);
                small = ImageIO.read(url);
            }else{
                small = ImageIO.read(new File(smallPath));
            }

            Graphics2D g = big.createGraphics();

//            float fx = Float.parseFloat(x);
//            float fy = Float.parseFloat(y);
//            int x_i = (int)fx;
//            int y_i = (int)fy;
            g.drawImage(small, x, y,small.getWidth(),small.getHeight(), null);
            g.dispose();
            ImageIO.write(big, "png", new File("d:/sss.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
