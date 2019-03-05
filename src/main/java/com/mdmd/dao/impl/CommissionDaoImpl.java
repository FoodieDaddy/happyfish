package com.mdmd.dao.impl;

import com.mdmd.dao.CommissionDao;
import com.mdmd.entity.CommissionEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.mdmd.constant.SystemConstant.DATEFORMAT__yyMMdd;

@Component
public class CommissionDaoImpl implements CommissionDao {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Object[]> listTopCommissionFromCommission_limit(int count) {
        Session session = sessionFactory.getCurrentSession();
        int today = Integer.valueOf(new SimpleDateFormat(DATEFORMAT__yyMMdd).format(new Date()));
        String hql = "select u.userid,c.todayCommission from  UserEntity as u , CommissionEntity as c " +
                "where  c in elements(u.commissionEntitySet) and c.calcuDate = :today  order by c.todayCommission desc";
        Query query = session.createQuery(hql);
        query.setParameter("today", today);
        query.setMaxResults(count);
        return query.list();
    }


}
