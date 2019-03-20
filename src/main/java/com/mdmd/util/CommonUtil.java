package com.mdmd.util;

import com.mdmd.entity.JO.PageJO;
import com.mdmd.entity.bean.PageBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


public class CommonUtil {
    private static  DecimalFormat DF_TWO = new DecimalFormat("0.00");

    public static double formatDouble_two(double d){
        return Double.valueOf(DF_TWO.format(d));
    }
    public static double formatDouble_three(double d){
        return Double.valueOf(DF_TWO.format(d));
    }

    public static PageBean getPageData(PageJO pageJO) throws Exception {
        int allCount = pageJO.getAllCount();
        int pageSize = pageJO.getPageSize();
        int page = pageJO.getPageNo();
        int allPage = allCount%pageSize == 0 ? allCount/pageSize : allCount/pageSize + 1;
        if( !(page > 0 || page <= allPage) )
        {
            throw new Exception("页码错误");
        }
        PageBean pageBean = new PageBean();
        pageBean.setIndex(-- page * pageSize);
        pageBean.setCount(pageSize);
        pageJO.setAllPage(allPage);
        return pageBean;
    }

}
