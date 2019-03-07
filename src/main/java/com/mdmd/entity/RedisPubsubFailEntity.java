package com.mdmd.entity;

import java.sql.Timestamp;

public class RedisPubsubFailEntity {
    private int id;
    private String channelName;
    private String jsonVal;
    private String failMsg;
    private Timestamp time;
    private int processed;

    public RedisPubsubFailEntity() {
    }

    public RedisPubsubFailEntity(String channelName, String jsonVal,String failMsg) {
        this.channelName = channelName;
        this.jsonVal = jsonVal;
        this.failMsg = failMsg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getJsonVal() {
        return jsonVal;
    }

    public void setJsonVal(String jsonVal) {
        this.jsonVal = jsonVal;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedisPubsubFailEntity that = (RedisPubsubFailEntity) o;

        if (id != that.id) return false;
        if (processed != that.processed) return false;
        if (channelName != null ? !channelName.equals(that.channelName) : that.channelName != null) return false;
        if (jsonVal != null ? !jsonVal.equals(that.jsonVal) : that.jsonVal != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (channelName != null ? channelName.hashCode() : 0);
        result = 31 * result + (jsonVal != null ? jsonVal.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + processed;
        return result;
    }
}
