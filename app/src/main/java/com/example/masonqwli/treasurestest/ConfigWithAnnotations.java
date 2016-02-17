package com.example.masonqwli.treasurestest;

import com.cookies.treasure.Default;
import com.cookies.treasure.DefaultValue;
import com.cookies.treasure.FileName;
import com.cookies.treasure.Get;
import com.cookies.treasure.GetMode;
import com.cookies.treasure.Key;
import com.cookies.treasure.SaveMode;
import com.cookies.treasure.Treasure;
import com.cookies.treasure.Write;

/**
 * 使用注解的例子
 */
@FileName(filename = "config")
@GetMode(mode = Get.MODE_PRIVATE)
public interface ConfigWithAnnotations extends Treasure {
    @Key(key = "user_name")
    @Default(stringValue = "mason")
    String getUserName();

    @Key(key = "user_name")
    @SaveMode(mode = Write.COMMIT)
    @GetMode(mode = Get.MODE_MULTI_PROCESS)
    void setUserName(String name);

    String getRootUserName(@DefaultValue String name, String uid);

    // 记录uid的用户名
    void setRootUserName(String uid, String name);
}
