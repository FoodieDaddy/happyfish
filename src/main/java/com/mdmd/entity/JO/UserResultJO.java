package com.mdmd.entity.JO;

import java.io.Serializable;
import java.lang.reflect.Field;

public class UserResultJO  implements Serializable {
    public int userId;
    public String openId;
    public double gold;//金币
    public double allGold;//总金币
    public double commission;//佣金
    public double allCommission;//总佣金
    public int superUserId;//上级id
    public int childs;//子类总数
    public int childs_1;
    public int childs_2;
    public int childs_3;
    public int childs_4;
    public int childs_5;
    public boolean ban;//是否被封

}
