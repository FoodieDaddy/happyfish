package com.mdmd.dao.impl;

import com.mdmd.dao.UserCommissionDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class UserCommissionDaoImpl implements UserCommissionDao {
    @Autowired
    private SessionFactory sessionFactory;

    public List listTopCommissionFromUserCommission_limit(int count) {
        Date date=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,-1);
        date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
        String dateStr = simpleDateFormat.format(date);
        //获取昨日起止时间
        String timeStampBegin = dateStr + " 00:00:00";
        String timeStampEnd =  dateStr + " 23:59:59";
        Session session = sessionFactory.getCurrentSession();
        String hql = "select uc.userEntity.userid,sum(uc.commissionResult) as cr " +
                "from UserCommissionEntity as uc " +
                "where unix_timestamp(uc.time) between unix_timestamp(:beginTime) and unix_timestamp(:endTime) " +
                "group by uc.userEntity.userid order by cr desc";
        Query query = session.createQuery(hql);
        query.setMaxResults(count);
        query.setParameter("beginTime", timeStampBegin);
        query.setParameter("endTime", timeStampEnd);
        return query.list();
    }
}
