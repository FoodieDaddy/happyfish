package com.mdmd.entity;


public class AccountDatasEntity {
    private int id;
    private int timeInt;
    private String jsonValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeInt() {
        return timeInt;
    }

    public void setTimeInt(int timeInt) {
        this.timeInt = timeInt;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountDatasEntity that = (AccountDatasEntity) o;

        if (id != that.id) return false;
        if (timeInt != that.timeInt) return false;
        if (jsonValue != null ? !jsonValue.equals(that.jsonValue) : that.jsonValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + timeInt;
        result = 31 * result + (jsonValue != null ? jsonValue.hashCode() : 0);
        return result;
    }
}
