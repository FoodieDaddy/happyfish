package com.mdmd.dao.impl;

import com.mdmd.dao.GameRuleDao;
import com.mdmd.entity.FishRuleEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameRuleDaoImpl implements GameRuleDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public FishRuleEntity getFishRuleWithPriceAndTargetValue(int price, int targetValue) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from FishRuleEntity f where f.price = :price and f.targetValue = :targetValue");
        query.setParameter("price",price);
        query.setParameter("targetValue",targetValue);
        List list = query.list();
        if(list.size() == 0)
            return null;
        return (FishRuleEntity) list.get(0);
    }
}
