package com.mdmd.dao;

import com.mdmd.entity.JO.AccountDatasJO;

import java.util.List;

public interface SqlDao {

    /**
     * 获取某日的统计数据 time必须为 xxxx-xx-xx日期格式
     * @param date
     * @return
     */
    AccountDatasJO getAccountDatas(String date);

    /**
     * 所有统计数据
     * @return
     */
    AccountDatasJO getAccountDatas_all();

}
