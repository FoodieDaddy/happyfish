package com.mdmd.dao.impl;

import com.mdmd.dao.UserDao;
import com.mdmd.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class UserDaoImpl  implements UserDao {


    @Autowired
    private SessionFactory sessionFactory;

    public List listForeignWithUserEntity_desc_limit(Class clazz, int number,int userId) {
        String name = clazz.getSimpleName();
        Session session = sessionFactory.getCurrentSession();
        String hql= "from  " + name + " as s where "+ "s.userEntity.userid =" + userId +" order by s.id desc";
        Query query = session.createQuery(hql);
        query.setMaxResults(number);
        return query.list();
    }

    public UserEntity getUserFromOpenId(String openId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from UserEntity as u where u.userOpenid = :openId" );
        query.setParameter("openId",openId);
        List list = query.list();
        if(list.size() > 0)
            return (UserEntity) list.get(0);
        return null;
    }

    public UserDetailEntity addUserDetail(UserEntity user) throws RuntimeException {
        Session session = sessionFactory.getCurrentSession();
        UserDetailEntity userDetailEntity = new UserDetailEntity(user);
        session.save(userDetailEntity);
        return userDetailEntity;
    }

    public UserEntity getUserFromUserId_only(int userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select new UserEntity(" +
                "u.userid,u.userOpenid,u.gold,u.commission," +
                "u.superUserId_a,u.superUserId_b,u.superUserId_c," +
                "u.superUserId_d,u.superUserId_e,u.loginBan,u.takeoutBan," +
                "u.commissionGiveBan,u.commissionGetBan," +
                "u.topupBan,u.takeoutTime,u.calcuDay" +
                ") from UserEntity as u where userid=:u");
        query.setParameter("u",userId);
        List<UserEntity> list = query.list();
        return list.get(0);
    }


    public int getChildsLevelNumberFromUser(int userId, int level) {
        Session session = sessionFactory.getCurrentSession();
        String hql = null;
        if(level == 1)
        {
            hql = "select count(u.id) from UserEntity as u where u.superUserId_a = :userId  ";
        }
        else if(level == 2)
        {
            hql = "select count(u.id) from UserEntity as u where u.superUserId_b =:userId ";
        }
        else if(level == 3)
        {
            hql = "select count(u.id) from UserEntity as u where u.superUserId_c =:userId ";
        }
        else if(level == 4)
        {
            hql = "select count(u.id) from UserEntity as u where u.superUserId_d =:userId ";
        }
        else if(level == 5)
        {
            hql = "select count(u.id) from UserEntity as u where u.superUserId_e =:userId ";
        }
        if(hql != null)
        {
            Query query = session.createQuery(hql);
            query.setParameter("userId",userId);
            Number o = (Number) query.list().get(0);
            return o.intValue();
        }
        return 0;
    }

    public double getChildsLevelCommissionFormUser(int userId, int level) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select sum(u.commissionResult) from UserCommissionEntity as u  where u.userEntity.userid = :userId and u.childNodeIndex = :level";
        Query query = session.createQuery(hql);
        query.setParameter("level",level);
        query.setParameter("userId",userId);
        Object o = query.list().get(0);
        return  o == null ? 0 : (double)o;
    }


    public double getGameRecord_costs_betweenTime(int userId, String beginTime, String endTime) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(" select sum(g.gameCost) - sum(g.principal)  " +
                "from GameRecordEntity as g where g.userEntity.userid = :u " +
                "and unix_timestamp(g.time) " +
                "between unix_timestamp(:beginTime) and  unix_timestamp(:endTime)");
        query.setParameter("u",userId);
        query.setParameter("beginTime",beginTime);
        query.setParameter("endTime",endTime);
        List list = query.list();
        Object o = list.get(0);
        if(o == null)
            return 0;
        return (double) o;

    }

    public double getTopup_sum_betweenTime(int userId, String beginTime, String endTime) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(" select sum(tu.topupQuantity) " +
                "from UserTopupEntity as tu where tu.userEntity.userid = :u " +
                "and unix_timestamp(tu.time) " +
                "between unix_timestamp(:beginTime) and  unix_timestamp(:endTime)");
        query.setParameter("u",userId);
        query.setParameter("beginTime",beginTime);
        query.setParameter("endTime",endTime);
        List list = query.list();
        Object o = list.get(0);
        if(o == null)
            return 0;
        return (double) o;
    }

    public double getTakeout_sum_betweenTime(int userId, String beginTime, String endTime) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(" select sum(to.takeoutQuantity) " +
                "from UserTakeoutEntity as to where to.userEntity.userid = :u " +
                "and unix_timestamp(to.time) " +
                "between unix_timestamp(:beginTime) and  unix_timestamp(:endTime)");
        query.setParameter("u",userId);
        query.setParameter("beginTime",beginTime);
        query.setParameter("endTime",endTime);
        List list = query.list();
        Object o = list.get(0);
        if(o == null)
            return 0;
        return (double) o;
    }





}
