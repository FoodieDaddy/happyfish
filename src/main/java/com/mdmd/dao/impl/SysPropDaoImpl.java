package com.mdmd.dao.impl;

import com.mdmd.dao.SysPropDao;
import com.mdmd.entity.SysPropEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysPropDaoImpl implements SysPropDao {
    @Autowired
    private SessionFactory sessionFactory;

    public SysPropEntity getSysPropWithSysNum(int sysNum) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from SysPropEntity as s where s.sysNum =" + sysNum);
        List list = query.list();
        if(list.size() > 0)
            return (SysPropEntity) list.get(0);
        return null;
    }
}
