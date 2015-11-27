package com.example.masonqwli.treasurestest;

import com.cookies.treasure.Treasure;

/**
 * 不适用注解的例子
 */
public interface ConfigWithoutAnnotations extends Treasure {
    String getUserName();

    void setUserName(String name);
}
