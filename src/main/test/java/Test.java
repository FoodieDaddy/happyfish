import com.mdmd.constant.WeiXinPublicContant;
import com.mdmd.entity.bean.SearchCompanyPayBean;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import static com.mdmd.constant.WeiXinPublicContant.WEIXIN_SEARCH_PAY_URL;
import static com.mdmd.util.WeixinConfigUtils.transferXml;
import static com.mdmd.util.WeixinConfigUtils.weiXinPost;

public class Test {

    public static void main(String[] args) throws ParseException {

        SearchCompanyPayBean searchPay = new SearchCompanyPayBean();
        searchPay.setAppid( WeiXinPublicContant.WEIXIN_APPID);
        searchPay.setMch_id(WeiXinPublicContant.WEIXIN_mch_id);
        searchPay.setNonce_str("shCEJ4xSOmBsCWM5");
        searchPay.setPartner_trade_no("20190304134836x33xeKq8Xi");
        searchPay.setSign("9EA092037632B07C9AEA6D007C974F11");
        String xmlInfo = transferXml(searchPay);
        try {
            String s = weiXinPost(xmlInfo, WEIXIN_SEARCH_PAY_URL);
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(xmlInfo);


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
