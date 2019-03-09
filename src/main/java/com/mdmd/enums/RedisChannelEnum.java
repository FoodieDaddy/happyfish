package com.mdmd.enums;

/**
 * 已设置的频道
 */
public enum RedisChannelEnum {
    channel_test("test"),
    channel_superComm("superComm");//用于异步计算佣金
    private String channel;

    RedisChannelEnum(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }


}