package com.mdmd.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


public class CommonUtil {
    private static  DecimalFormat DF_TWO = new DecimalFormat("0.00");
    private static  DecimalFormat DF_THREE = new DecimalFormat("0.000");

    public static double formatDouble_two(double d){
        return Double.valueOf(DF_TWO.format(d));
    }

    public static double formatDouble_three(double d){
        return Double.valueOf(DF_THREE.format(d));
    }
}
