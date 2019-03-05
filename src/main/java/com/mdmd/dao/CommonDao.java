package com.mdmd.dao;

import java.util.List;

/**
 * 通用dao方法
 */
public interface CommonDao {

    List listAllEntity(Class clazz);
    void addEntity(Object obj);

    void updateEntity(Object obj);

    Object getEntity(Class clazz,int id);
}
