package com.example.masonqwli.treasurestest;

import com.cookies.treasure.Default;
import com.cookies.treasure.FileName;
import com.cookies.treasure.Key;
import com.cookies.treasure.Treasure;

/**
 * 使用注解的例子
 */
@FileName(filename = "config")
public interface ConfigWithAnnotations extends Treasure {
    @Key(key = "user_name")
    @Default(stringValue = "mason")
    String getUserName();

    @Key(key = "user_name")
    void setUserName(String name);
}
