package com.app.menubox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zndroid.menubox.MenuBox;
import com.zndroid.menubox.core.IMenuItemClick;
import com.zndroid.menubox.core.MenuItem;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity {
    private boolean isShow = true;
    private SharedPreferences sharedPreferences;
    private MenuBox menuBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("sp_test", Context.MODE_PRIVATE);
    }

    public void show_menu(View view) {
        List<MenuItem> list = new CopyOnWriteArrayList<>();

        MenuItem item1 = new MenuItem();
        item1.setIconId(R.mipmap.icon_camera_phone);
        item1.setFrontSize(14.0f);
        item1.setTitle("标题1");

        MenuItem item2 = new MenuItem();
        item2.setIconId(R.mipmap.icon_camera_phone);
        item2.setTitle("标题2");

        MenuItem item3 = new MenuItem();
        item3.setIconId(R.mipmap.icon_camera_phone);
        if (sharedPreferences.getBoolean("item3", true))
            item3.setShowDot(true);
        else
            item3.setShowDot(false);
        item3.setTitle("标题3");

        list.add(item1);
        list.add(item2);
        list.add(item3);

        menuBox = new MenuBox.Builder(this)
                .setFullScreen(true)
                .setMenuItemList(list)
                .setRow(6)
                .setBoxTitle("Menu Box")
                .setBoxTitleSize(20.0f)
                .setBoxCloseImage(R.mipmap.icon_control_camera_up_pressed)
                .setOnItemClick(new IMenuItemClick() {
                    @Override
                    public void onItemClick(MenuItem item) {
                        if (item.getPosition() == 2)
                            sharedPreferences.edit().putBoolean("item3", false).apply();

                        Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setAnimationStyle(R.style.anim_box_top_drop_style)
                .setOutsideTouchable(false)
                .create();

        menuBox.showFromTop(view, this);

    }

    public void test(View view) {
        Toast.makeText(MainActivity.this, "im clicked", Toast.LENGTH_SHORT).show();
    }
}
