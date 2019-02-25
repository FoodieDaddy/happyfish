package com.mdmd.dao.impl;

import com.mdmd.dao.QrcodeDao;
import com.mdmd.entity.QrcodeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QrcodeDaoImpl implements QrcodeDao {
    @Autowired
    private SessionFactory sessionFactory;

    public QrcodeEntity getQrcodeEntityFromUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from QrcodeEntity as q where q.userId = :u");
        query.setParameter("u",userId);
        List list = query.list();
        if(list.size() == 0)
            return null;
        return (QrcodeEntity) list.get(0);
    }

    public void addQrcodeEntity(QrcodeEntity qrcodeEntity) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(qrcodeEntity);
        } catch (Exception e) {
            System.out.println(qrcodeEntity.getUserId()+"的qrcode存储失败");
        }
    }

    public void updateQrcodeEntity(QrcodeEntity qrcodeEntity) {
        Session session = sessionFactory.getCurrentSession();
        session.update(qrcodeEntity);

    }

    @Override
    public QrcodeEntity getQrcodeEntityFromSceneId(int sceneId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from QrcodeEntity where sceneId = :s");
        query.setParameter("s",sceneId);
        List list = query.list();
        if(list.size() == 0)
            return null;
        return (QrcodeEntity) list.get(0);

    }
}
