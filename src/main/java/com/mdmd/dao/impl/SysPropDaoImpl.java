package com.mdmd.dao.impl;

import com.mdmd.dao.SysPropDao;
import com.mdmd.entity.AccountDatasEntity;
import com.mdmd.entity.SysPropEntity;
import com.mdmd.util.CommonUtil;
import com.mdmd.util.DateFormatUtil;
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

    public AccountDatasEntity getAccountDatasEntity_today() {
        Session session = sessionFactory.getCurrentSession();
        int today = DateFormatUtil.now_yyyyMMdd_intVal();
        Query query = session.createQuery("from AccountDatasEntity as s where s.timeInt =:t");
        query.setParameter("t",today);
        List list = query.list();
        if(list.size() == 0)
            return null;
        return (AccountDatasEntity) list.get(0);
    }

    public List<AccountDatasEntity> getAccountDatasEntity(int startDay, int endDay) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from AccountDatasEntity as s where s.timeInt >=:st and s.timeInt<=:e");
        query.setParameter("st",startDay);
        query.setParameter("e",endDay);
        return query.list();
    }
}
