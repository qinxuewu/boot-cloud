package com.im.utils;

/**
 * 〈一句话功能简述〉<br>
 * 〈常量类〉
 *
 * @author qinxuewu
 * @create 18/10/14上午10:30
 * @since 1.0.0
 */


public enum  ConstEnum{
    MYID("myid"),
    FRIENDID("friendid"),
    MESSAGE("message");

    private final String name;

    private ConstEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
