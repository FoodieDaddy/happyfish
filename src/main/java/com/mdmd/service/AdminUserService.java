package com.mdmd.service;

import com.mdmd.entity.AdminUserEntity;

public interface AdminUserService {
    AdminUserEntity getAdminUserEntityFromOpenid(String openid);
}
