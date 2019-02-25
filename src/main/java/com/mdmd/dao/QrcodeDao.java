package com.mdmd.dao;

import com.mdmd.entity.QrcodeEntity;

public interface QrcodeDao {

    QrcodeEntity getQrcodeEntityFromUserId(int userId);

    void addQrcodeEntity(QrcodeEntity qrcodeEntity);

    void updateQrcodeEntity(QrcodeEntity qrcodeEntity);

    QrcodeEntity getQrcodeEntityFromSceneId(int sceneId);
}
