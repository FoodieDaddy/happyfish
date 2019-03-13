package com.mdmd.dao.impl;

import com.mdmd.dao.TopupDao;
import com.mdmd.entity.UserOrderTopupEntity;
import com.mdmd.entity.UserTopupEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TopupDaoImpl implements TopupDao {

    @Autowired
    private SessionFactory sessionFactory;


    public UserOrderTopupEntity getUserOrderTopupEntityFromOrderNum(String orderNum, int userId){
        Session session = sessionFactory.getCurrentSession();
        String hql = "from UserOrderTopupEntity as ut " +
                "where ut.orderNumber = :orderNum and ut.userEntity.userid = :userId";
        Query query = session.createQuery(hql);
        query.setParameter("userId",userId);
        query.setParameter("orderNum",orderNum);
        List list = query.list();
        if(list == null)
            return null;
        return (UserOrderTopupEntity) list.get(0);
    }
}
