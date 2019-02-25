import com.google.zxing.WriterException;
import com.mdmd.util.QrcodeUtil;
import com.sun.prism.Graphics;

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
import java.util.Date;
import java.util.Random;

public class Test {

    public static void main(String[] args) throws ParseException {
        for (int i = 0; i < 200; i++) {
            if(i == 100){
                System.exit(0);
            }
            System.out.println(i);
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
