import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mdmd.constant.WeiXinPublicContant;
import com.mdmd.util.Base64Utl;
import com.mdmd.util.MD5Util;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;



public class Test {
    public static void main(String[] args) {


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
