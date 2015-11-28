package com.cookies.treasure;

import android.content.Context;

/**
 * Created by masonqwli on 15/11/28.
 */
public enum Get {
    MODE_PRIVATE(Context.MODE_PRIVATE), MODE_MULTI_PROCESS(Context.MODE_MULTI_PROCESS);

    private int mode;

    Get(int val) {
        this.mode = val;
    }

    public int get() {
        return mode;
    }
}
