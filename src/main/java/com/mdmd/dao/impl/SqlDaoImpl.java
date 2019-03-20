package com.mdmd.dao.impl;

import com.mdmd.dao.SqlDao;
import com.mdmd.entity.JO.AccountDatasJO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class SqlDaoImpl implements SqlDao {
    @Autowired
    private SessionFactory sessionFactory;

    public AccountDatasJO getAccountDatas(String date){
        Session session = sessionFactory.getCurrentSession();
        String sql = "select\n" +
                "(select  COALESCE(count(g.gameCost),0)  from game_record as g where g.gameOrder is not null and cast(g.time as date) = date(:t) ) as gameTime,\n" +
                "(select COALESCE(ROUND(sum(tp.realQuantity),2),0)  from user_topup as tp where tp.topupResult > 0 and cast(tp.time as date) = date(:t)) as topup,\n" +
                "(select COALESCE(sum(tk.takeoutQuantity),0)  from user_takeout as tk where tk.takeoutResult > 0 and tk.takeoutType = 0 and cast(tk.time as date) = date(:t)) as goldTake,\n" +
                "(select COALESCE(sum(tk.takeoutQuantity),0)  from user_takeout as tk where tk.takeoutResult > 0 and tk.takeoutType = 1 and cast(tk.time as date) = date(:t)) as commissionTake ;";
        NativeQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.setParameter("t",date);
        List list = sqlQuery.list();
        Object[] o = (Object[]) list.get(0);
        int gameTime =  Integer.parseInt(o[0].toString());
        double topups = Double.valueOf(o[1].toString());
        int takeoutGold = Integer.parseInt(o[2].toString());
        int takeoutComm =  Integer.parseInt(o[3].toString()) ;
        AccountDatasJO accountDatasJO = new AccountDatasJO(date, gameTime, topups, takeoutGold, takeoutComm);
        return accountDatasJO;
    }


    public AccountDatasJO getAccountDatas_all(){
        Session session = sessionFactory.getCurrentSession();
        String sql = "select\n" +
                "(select  COALESCE(count(g.gameCost),0)  from game_record as g where g.gameOrder is not null  ) as gameTime,\n" +
                "(select COALESCE(ROUND(sum(tp.realQuantity),2),0)  from user_topup as tp where tp.topupResult > 0 ) as topup,\n" +
                "(select COALESCE(sum(tk.takeoutQuantity),0)  from user_takeout as tk where tk.takeoutResult > 0 and tk.takeoutType = 0) as goldTake,\n" +
                "(select COALESCE(sum(tk.takeoutQuantity),0)  from user_takeout as tk where tk.takeoutResult > 0 and tk.takeoutType = 1 ) as commissionTake ;";
        NativeQuery sqlQuery = session.createSQLQuery(sql);
        List list = sqlQuery.list();
        Object[] o = (Object[]) list.get(0);
        int gameTime =  Integer.parseInt(o[0].toString());
        double topups = Double.valueOf(o[1].toString());
        int takeoutGold = Integer.parseInt(o[2].toString());
        int takeoutComm =  Integer.parseInt(o[3].toString()) ;
        AccountDatasJO accountDatasJO = new AccountDatasJO("0000-00-00", gameTime, topups, takeoutGold, takeoutComm);
        return accountDatasJO;
    }

}
