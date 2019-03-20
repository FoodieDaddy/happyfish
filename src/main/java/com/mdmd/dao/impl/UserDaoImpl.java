package com.mdmd.dao.impl;

import com.mdmd.dao.UserDao;
import com.mdmd.entity.*;
import com.mdmd.entity.JO.PageJO;
import com.mdmd.entity.JO.UserResultJO;
import com.mdmd.entity.bean.PageBean;
import com.mdmd.util.DateFormatUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
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
        String today = DateFormatUtil.today_yyyyMMddHHmmss();
        String hql = "select sum(u.commissionResult) from UserCommissionEntity as u  where u.userEntity.userid = :userId and u.childNodeIndex = :level and unix_timestamp(u.time) > unix_timestamp(:today)";
        Query query = session.createQuery(hql);
        query.setParameter("level",level);
        query.setParameter("userId",userId);
        query.setParameter("today",today);
        Object o = query.list().get(0);
        return  o == null ? 0 : (double)o;
    }


    public List<UserResultJO> listUserEntity_limit(PageBean pageBean, Map<String, Object> filter, Map<String,Object> sort) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("select s.userid as userId\n" +
                "       ,s.userOpenid as openId\n" +
                "       ,s.gold as gold\n" +
                "       ,s.goldTake + s.gold as allGold\n" +
                "       ,s.commission as commission\n" +
                "       ,s.takeCom + s.commission as allCommission\n" +
                "       ,s.superUserId as superUserId\n"+
                "       ,s.a+s.b+s.c+s.d+s.e as childs\n" +
                "       ,s.a as childs_1,s.b as childs_2\n" +
                "       ,s.c as childs_3\n" +
                "       ,s.d as childs_4\n" +
                "       ,s.e as childs_5\n" +
                "       ,s.loginBan as ban\n" +
                "from(select  u.userid as userid,u.userOpenid as userOpenid,u.gold as gold,g.takeoutGold as goldTake\n" +
                "       ,u.commission as commission,com.takeoutCommission as takeCom,u.superUserId_a as superUserId,u.loginBan as loginBan,\n" +
                "       (select count(u1.userid) from user as u1 where u1.superUserId_a = u.userid) as a,\n" +
                "       (select count(u1.userid) from user as u1 where u1.superUserId_b = u.userid) as b,\n" +
                "       (select count(u1.userid) from user as u1 where u1.superUserId_c = u.userid) as c,\n" +
                "       (select count(u1.userid) from user as u1 where u1.superUserId_d = u.userid) as d,\n" +
                "       (select count(u1.userid) from user as u1 where u1.superUserId_e = u.userid) as e\n" +
                "from user as u\n" +
                "left join commission as com on u.userid = com.userId\n" +
                "left join gold g on u.userid = g.userId)as s");
        if(filter.size() > 0)
        {
            Iterator<Map.Entry<String, Object>> iterator = filter.entrySet().iterator();
            boolean flag = true;
            while (iterator.hasNext()){
                Map.Entry<String, Object> next = iterator.next();
                if(flag)
                {
                    sql.append(" where " + next.getKey() + "="+ next.getValue());
                    flag = false;
                }
                else
                {
                    sql.append(" and " + next.getKey() + "="+ next.getValue());
                }
            }
        }
        if(sort.size() > 0)
        {
            Object field = sort.get("field");
            Object type = sort.get("type");
            sql.append(" order by " + field +" " + type);
        }
        NativeQuery sqlQuery = session.createSQLQuery(sql.toString());
        sqlQuery.setFirstResult(pageBean.getIndex());
        sqlQuery.setMaxResults(pageBean.getCount());
        List list = sqlQuery.list();
        List<UserResultJO> userResultJOS = new LinkedList<>();
        //结果赋值 非通用方法
        if(list != null)
        {
            Field[] fields = UserResultJO.class.getDeclaredFields();
            for (int i = 0; i < list.size(); i++) {
                UserResultJO userResultJO = new UserResultJO();
                Object[] o = (Object[]) list.get(i);
                for (int j = 0; j < o.length; j++) {
                    Object val = o[j];
                    if(val instanceof BigInteger)
                        val = ((BigInteger) val).intValue();
                    fields[j].set(userResultJO,val);
                }
                userResultJOS.add(userResultJO);
            }
        }
        return userResultJOS;
    }



}
