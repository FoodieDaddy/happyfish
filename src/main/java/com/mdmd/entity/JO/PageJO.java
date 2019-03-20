package com.mdmd.entity.JO;

import java.io.Serializable;

public class PageJO implements Serializable {

    private int pageNo;//当前页面
    private int allPage;//页面总数
    private int pageSize;//页面数据量
    private int allCount;//总数据量

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getAllPage() {
        return allPage;
    }

    public void setAllPage(int allPage) {
        this.allPage = allPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }
}
