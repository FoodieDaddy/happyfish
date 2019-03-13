package com.mdmd.dao.impl;

import com.mdmd.dao.AdminUserDao;
import com.mdmd.entity.AdminUserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminUserDaoImpl implements AdminUserDao {

    @Autowired
    private SessionFactory sessionFactory;
    public AdminUserEntity getAdminUserFromOpenid(String openid) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from AdminUserEntity as a where a.openid = :o");
        query.setParameter("o",openid);
        List list = query.list();
        if(list == null)
            return null;
        return (AdminUserEntity) list.get(0);
    }
}
