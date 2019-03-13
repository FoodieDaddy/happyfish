package com.mdmd.dao;

import com.mdmd.entity.AdminUserEntity;

public interface AdminUserDao {
    AdminUserEntity getAdminUserFromOpenid(String openid);
}
