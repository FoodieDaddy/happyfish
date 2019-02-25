package com.mdmd.entity;

import java.sql.Timestamp;

public class UserDetailEntity {
    private int id;
    private Timestamp createTime;
    private Timestamp firstGameTime;
    private UserEntity userEntity;

    public UserDetailEntity() {
    }

    public UserDetailEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }



    public Timestamp getFirstGameTime() {
        return firstGameTime;
    }

    public void setFirstGameTime(Timestamp firstGameTime) {

        this.firstGameTime = firstGameTime;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDetailEntity that = (UserDetailEntity) o;

        if (id != that.id) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (firstGameTime != null ? !firstGameTime.equals(that.firstGameTime) : that.firstGameTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (firstGameTime != null ? firstGameTime.hashCode() : 0);
        return result;
    }
}
