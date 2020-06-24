# MenuBox Instruction

## 引入（Android Studios）

在项目根目录 `build.gradle` 下添加仓库地址

```sh
allprojects {
    repositories {
        ...
        maven { url 'coming soon' }
        ...
    }
}
```

在应用Module `app` 根目录 `build.gradle` 添加如下配置

```sh
implementation 'com.zndroid:MenuBox:1.0.4'//（建议指定版本）
//或者
implementation 'com.zndroid:MenuBox:latest.release'//保持最新版
```

然后点击同步按钮进行代码同步

<img src=".\doc\imgs\image-20200604170920064.png" alt="image-20200604170920064" style="zoom:67%;" />

## 使用

目前支持链式调用，方便快捷。样例中 `this` 指的是上下文 `Activity` ，`view` 指的是依附显示的 `View`。

### 菜单行为相关

目前可以设置：整体菜单栏出现方向（上、下、左、右）、菜单栏大小（默认内容充满自适应）、背景色、背景透明度、菜单栏进出动画类型（默认内部支持，可通过 `R.style.anim_menu_box_xxx` 指定，也可以自定义）、菜单标题文字相关定制、支持点击透传、设置菜单项个数以及点击行为等等。以上均通过 `MenuBox.Builder`设置之后调用  `create()` 方法进行创建，最终调用 `showFromXXX()` 方法展示。

#### 初始化

```java
MenuBox menuBox = new MenuBox.Builder(this)
```

#### 设置菜单栏标题

默认为空，并不显示

```java
build.setBoxTitle(String title)
```

#### 设置菜单栏标题字体大小

默认： 14f 

```java
build.setBoxTitleSize(float titleSize)
```

#### 设置菜单栏标题字体颜色

默认： 黑色

```java
build.setBoxTitleColor(@ColorInt int titleColor)
```

#### 设置菜单栏关闭按钮

默认：图标 `X` 实现

```java
build.setBoxCloseImage(@DrawableRes int resId)
```

#### 设置菜单栏背景颜色

默认： 白色

```java
build.setBoxBgColor(@ColorInt int colorInt)
```

#### 设置菜单栏背景透明度

```java
build.setBackGroundLevel(float level)//0.0f-1.0f
```

#### 设置菜单栏宽高

建议不用设置，默认自适应

```java
build.setWidthAndHeight(int width, int height)
```

#### 设置动画类型

默认实现了一套动画，通过 `R.style.anim_menu_box_` 可以使用自带动画，

包括常见的 `滑动` 、 `下拉` 、 `弹出` 、`抖动` 动画。

```java
build.setAnimationStyle(int animationStyle)
```

#### 设置数据源

```java
build.setMenuItemList(List<MenuItem> menuItemList)
```

#### 设置菜单项监听

```java
build.setOnItemClick(IMenuItemClick itemClick)
```

#### 设置菜单行/列数

可根据具体需要设置子菜单项布局，左右显示时，建议设置为 `1` ，系统会进行默认自适应居中布局，改参数受数据源影响

```java
build.setRow(int row)
```

#### 设置菜单布局形式

默认子菜单项横向布局，如果是左右弹出菜单项请设置为 `true` 竖向布局，比较美观

```java
build.setVertical(boolean vertical)
```

#### 设置点击透传

默认点击菜单栏以外的部分会消失菜单栏，如果下层元素可以处理点击事件并不会触发，如果需要触发请设置为 `true` 。

```java
build.setOutsideTouchable(boolean touchable)
```

#### 设置菜单项点击时是否有缩放动画

默认无缩放动画

```java
build.withButtonAnimation(boolean showAnimation)
```

#### 创建菜单栏

最终通过该方法创建菜单栏的实体

```java
build.create()
```

#### 显示菜单栏

目前支持 `上、下、左、右` 弹出菜单栏，默认贴边自适应显示，如果不需要贴边显示，可以调用其带有偏移量的构造方法

```java
menuBox.showFromXXX(xxx)
```

### 菜单项属性相关

目前菜单项包含：

`菜单标题 setTitle(String title)`，

`标题字体颜色 setFrontColor(@ColorInt int frontColor)` , 

`标题字体大小 setFrontSize(float frontSize)`，

`图标 setIconId(@DrawableRes int iconId)`， 

`红点显示 setShowDot(boolean showDot)` （默认不显示，用于提示新功能等场景），

其中 `position 索引位置`属性为回调属性，初始化时无需设置，方便开发者处理红点状态。

### 范例（包含红点处理举例）

```java
package com.app.menubox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arcvideo.component.menubox.MenuBox;
import com.arcvideo.component.menubox.core.IMenuItemClick;
import com.arcvideo.component.menubox.core.MenuItem;

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
        
        //红点处理 start
        if (sharedPreferences.getBoolean("item3", true))
            item3.setShowDot(true);
        else
            item3.setShowDot(false);
        //红点处理逻辑 end
        
        item3.setTitle("标题3");

        list.add(item1);
        list.add(item2);
        list.add(item3);

        menuBox = new MenuBox.Builder(this)
                .setMenuItemList(list)
                .setRow(3)
                .setBoxTitle("工具箱")
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
                .setOutsideTouchable(false)
                .setAnimationStyle(R.style.anim_menu_box_top_over_shoot_style)
                .create();

        menuBox.showFromTop(view, this);

    }

    public void test(View view) {
        Toast.makeText(MainActivity.this, "im clicked", Toast.LENGTH_SHORT).show();
    }
}
```

### 效果

<img src=".\doc\imgs\image-20200605145709460.png" alt="image-20200605145709460" style="zoom: 67%;" />

