package com.mdmd.enums;

public enum SysSearchEnum {
    search_user("user"),//用户
    search_topup("topup"),//充值
    search_takeout("takeout"),//提现
    search_gamerecord("game"),//游戏记录
    search_ordertopup("ortopup"),//充值订单
    search_adminuser("admin");//管理员列表
    private String search;
    SysSearchEnum(String search){
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public static SysSearchEnum getEnum(String value){
        SysSearchEnum[] values = SysSearchEnum.values();
        for (int i = 0; i < values.length; i++) {
            if(value.equals(values[i].getSearch()))
                return values[i];
        }
        return null;
    }

}
