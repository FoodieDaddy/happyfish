package com.mdmd.constant;

public class RedisConstant {
    //考虑到应该使用price和targetValue双判断才能定位获取fishrule  所以这里redis中map的key用 money + f + targetValue
    public static final String REDIS_KEY_fishRules = "fishRules";
    public static final String REDIS_KEY_fishRules_itemkey = "f";//map的key money + f + targetValue
}
