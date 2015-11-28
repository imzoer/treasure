package com.example.masonqwli.treasurestest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cookies.treasure.Treasures;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1.初始化Treasures
        Treasures.init(this);

        // 2.使用无注解版本
        Log.d("mason", "userName without annotation:" + Treasures.of(ConfigWithoutAnnotations.class).getUserName());
        Treasures.of(ConfigWithoutAnnotations.class).setUserName("masonqwli");
        Log.d("mason", "userName without annotation:" + Treasures.of(ConfigWithoutAnnotations.class).getUserName());

        // 3.使用注解版本
        Log.d("mason", "userName with annotation:" + Treasures.of(ConfigWithAnnotations.class).getUserName());
        Treasures.of(ConfigWithAnnotations.class).setUserName("naughty");
        Log.d("mason", "userName with annotation:" + Treasures.of(ConfigWithAnnotations.class).getUserName());

        Log.d("mason", "rootUserName:" + Treasures.of(ConfigWithAnnotations.class).getRootUserName("root", "fuck", "name"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
