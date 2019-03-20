package com.mdmd.dao;

import com.mdmd.entity.AdminUserEntity;
import com.mdmd.entity.bean.PageBean;

import java.util.List;

public interface AdminUserDao {
    AdminUserEntity getAdminUserFromOpenid(String openid);

}
