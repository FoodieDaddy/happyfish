package com.mdmd.dao;

import com.mdmd.entity.AccountDatasEntity;
import com.mdmd.entity.SysPropEntity;

import java.util.List;

public interface SysPropDao {

    SysPropEntity getSysPropWithSysNum(int sysNum);

    AccountDatasEntity getAccountDatasEntity_today();

    List<AccountDatasEntity> getAccountDatasEntity(int startDay, int endDay);
}
