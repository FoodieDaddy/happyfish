package com.mdmd.dao.impl;

import com.mdmd.dao.CommonDao;
import com.mdmd.entity.bean.PageBean;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public <T> T  getEntity(Class<T> clazz, int id) {
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

    public int getCount(Class clazz) {
        String name = clazz.getSimpleName();
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select count(n) from " + name + " as n");
        List list = query.list();
        if(list == null)
            return 0;
        return Long.valueOf(list.get(0).toString()).intValue();
    }

    public <T> List<T> listAllEntity_limit_desctime(Class<T> tClass,PageBean pageBean,  Map<String,Object> filter) throws Exception {
        String name = tClass.getSimpleName();
        Session session = sessionFactory.getCurrentSession();
        String hql = "";
        if(filter.size() > 0 && filter.containsKey("userid"))
        {
            Integer userid = Integer.valueOf(filter.get("userid").toString());
            hql = "from "+ name +" as n where n.userEntity.userid = "+ userid  +" order by n.id desc";

        }
        else
        {
            hql = "from "+ name +" as n order by n.id desc";

        }
        Query<T> query = session.createQuery(hql, tClass);
        query.setMaxResults(pageBean.getCount());
        query.setFirstResult(pageBean.getIndex());
        return query.list();
    }

    public int countAllEntity_filter(Class tClass, Map<String, Object> filter) throws Exception {
        String name = tClass.getSimpleName();
        Session session = sessionFactory.getCurrentSession();
        String hql = "";
        if(filter.size() > 0 && filter.containsKey("userid"))
        {
            Integer userid = Integer.valueOf(filter.get("userid").toString());
            hql = "select count(n.id) from "+ name +" as n where n.userEntity.userid = "+ userid  +" order by n.id desc";

        }
        else
        {
            hql = "select count(n.id) from "+ name +" as n ";

        }
        Query<Long> query = session.createQuery(hql, Long.class);
        return query.list().get(0).intValue();
    }


    public <T> List<T> listAllEntity(Class<T> clazz){
        String name = clazz.getSimpleName();
        Session session = sessionFactory.getCurrentSession();
        String hql= "from  " + name ;
        Query<T> query = session.createQuery(hql, clazz);
        return query.list();
    }
}
