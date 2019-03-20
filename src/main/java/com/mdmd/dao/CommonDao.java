package com.mdmd.dao;

import com.mdmd.entity.bean.PageBean;

import java.util.List;
import java.util.Map;

/**
 * 通用dao方法
 */
public interface CommonDao {

    <T> List<T> listAllEntity(Class<T> clazz);

    void addEntity(Object obj);

    void updateEntity(Object obj);

    <T> T  getEntity(Class<T> clazz, int id);

    void removeEntity(Object obj);

    void removeEntity(Class clazz, int id);

    int getCount(Class clazz);

    <T> List<T> listAllEntity_limit_desctime(Class<T> tClass,PageBean pageBean, Map<String,Object> filter) throws Exception;
    int  countAllEntity_filter(Class tClass, Map<String,Object> filter) throws Exception;

}