package com.mdmd.entity;

public class QrcodeEntity {
    private int id;
    private int userId;
    private String ticket;
    private int sceneId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QrcodeEntity that = (QrcodeEntity) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (ticket != null ? !ticket.equals(that.ticket) : that.ticket != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + userId;
        result = 31 * result + (ticket != null ? ticket.hashCode() : 0);
        return result;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }
}
