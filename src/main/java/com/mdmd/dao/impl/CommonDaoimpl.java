package com.mdmd.dao.impl;

import com.mdmd.dao.CommonDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonDaoimpl implements CommonDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addEntity(Object obj){
        Session session = sessionFactory.getCurrentSession();
        session.save(obj);
    }

    public void updateEntity(Object obj) {
        Session session = sessionFactory.getCurrentSession();
        session.update(obj);
    }

    public Object getEntity(Class clazz, int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(clazz, id);
    }

    public void removeEntity(Object obj) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(obj);
    }

    public void removeEntity(Class clazz, int id) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "delete from " + clazz.getSimpleName() +" as c where c.id = "+id;
        Query query = session.createQuery(hql);
        query.executeUpdate();
    }

    public List listAllEntity(Class clazz) {
        String name = clazz.getSimpleName();
        Session session = sessionFactory.getCurrentSession();
        String hql= "from  " + name ;
        Query query = session.createQuery(hql);
        return query.list();
    }
}
