package com.mdmd.dao.impl;

import com.mdmd.dao.TakeoutDao;
import com.mdmd.entity.UserTakeoutEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class TakeoutDaoImpl implements TakeoutDao {
    @Autowired
    private SessionFactory sessionFactory;

    public int getTakeoutAllCount_today(int userId){
        //今日时间
        String today = new SimpleDateFormat("yy-MM-dd").format(new Date())+" 00:00:00";
        Session session = sessionFactory.getCurrentSession();
        String hql = "select sum(t.takeoutQuantity) from UserTakeoutEntity as t " +
                "where t.userEntity.userid = :userId " +
                "and unix_timestamp(t.time) >= unix_timestamp(:today)";
        Query query = session.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("today", today);
        Number o = (Number) query.list().get(0);
        return o.intValue();
    }

    public int getTakeoutCount_today_0gold_1commission(int userId,int type) {
        type = type <= 0 ? 0 : 1;
        String today = new SimpleDateFormat("yy-MM-dd").format(new Date())+" 00:00:00";
        Session session = sessionFactory.getCurrentSession();
        String hql = "select sum(t.takeoutQuantity) from UserTakeoutEntity as t " +
                "where t.userEntity.userid = :userId " +
                "and unix_timestamp(t.time) >= unix_timestamp(:today)" +
                "and t.takeoutType = :type and t.takeoutResult = 1";
        Query query = session.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("today", today);
        query.setParameter("type", type);
        Number o = (Number) query.list().get(0);
        return o.intValue();
    }

    public List<UserTakeoutEntity> listUserTakeoutDatas(int userId, int recordNum) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from UserTakeoutEntity as u where u.userEntity.userid = :u order by u.id desc ";
        Query query = session.createQuery(hql);
        query.setParameter("u",userId);
        query.setMaxResults(recordNum);
        return query.list();

    }
}
