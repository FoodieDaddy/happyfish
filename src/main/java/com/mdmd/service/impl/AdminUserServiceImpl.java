package com.mdmd.service.impl;

import com.mdmd.controller.SysPropController;
import com.mdmd.dao.AdminUserDao;
import com.mdmd.entity.AdminUserEntity;
import com.mdmd.service.AdminUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserDao adminUserDao;


    public AdminUserEntity getAdminUserEntityFromOpenid(String openid){
        AdminUserEntity adminUser = adminUserDao.getAdminUserFromOpenid(openid);
        return adminUser;
    }
}
