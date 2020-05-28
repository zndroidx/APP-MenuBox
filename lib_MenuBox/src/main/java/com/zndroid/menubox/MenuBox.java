package com.zndroid.menubox;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.zndroid.menubox.core.IMenuItemClick;
import com.zndroid.menubox.core.MenuController;
import com.zndroid.menubox.core.MenuItem;
import com.zndroid.menubox.util.ScreenUtil;

import java.util.List;

/**
 * Created by lzy on 2020/5/27.
 */
public class MenuBox extends PopupWindow {
    private MenuController menuController;

    private MenuBox(Context context) {
        menuController = new MenuController(context, this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        menuController.setBackGroundLevel(1.0f);
    }

    public static class Builder {
        private MenuController.PopupParams params;
        private IMenuItemClick itemClick;
        private List<MenuItem> menuItemList;

        public Builder(Context context) {
            params = new MenuController.PopupParams(context);
            params.mView = null;
            params.layoutResId = R.layout.layout_menu_box;
        }

        /**
         * 设置点击回调
         * */
        public Builder setOnItemClick(IMenuItemClick itemClick) {
            this.itemClick = itemClick;
            return this;
        }

        public Builder setMenuItemList(List<MenuItem> menuItemList) {
            this.menuItemList = menuItemList;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param isWidthFull
         * @return Builder
         */
        public Builder setFullScreen(boolean isWidthFull) {
            if (isWidthFull)
                params.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            params.mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
            return this;
        }

        /**
         * 设置背景灰色程度
         *
         * @param level 0.0f-1.0f
         * @return Builder
         */
        public Builder setBackGroundLevel(float level) {
            params.isShowBg = true;
            params.bg_level = level;
            return this;
        }

        /**
         * 是否可点击Outside消失
         *
         * @param touchable 是否可点击外部
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean touchable) {
            params.isTouchable = touchable;
            return this;
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            params.isShowAnim = true;
            params.animationStyle = animationStyle;
            return this;
        }

        /**
         * 设置列数，如果数据源少于设置的列数，则显示一行，列数为数据源的数目
         *
         * @param row
         * return Builder
         * */
        public Builder setRow(int row) {
            params.row = row;
            return this;
        }

        /**
         * 设置整体菜单栏标题，不设置则不展示
         *
         * @param title
         * return Builder
         * */
        public Builder setBoxTitle(String title) {
            params.box_title = title;
            return this;
        }

        /**
         * 设置整体菜单栏标题颜色，默认白色
         *
         * @param titleColor
         * return Builder
         * */
        public Builder setBoxTitleColor(@ColorInt int titleColor) {
            params.box_title_color = titleColor;
            return this;
        }

        /**
         * 设置整体菜单栏标题字体大小，默认 14.0f
         *
         * @param size
         * return Builder
         * */
        public Builder setBoxTitleSize(float size) {
            params.box_title_front_size = size;
            return this;
        }

        /**
         * 设置整体菜单栏关闭图标，默认系统关闭图标
         *
         * @param resId
         * return Builder
         * */
        public Builder setBoxCloseImage(@DrawableRes int resId) {
            params.box_close_image_id = resId;
            return this;
        }

        /**
         * 设置整体菜单栏背景颜色，默认白色
         *
         * @param colorInt
         * return Builder
         * */
        public Builder setBoxBgColor(@ColorInt int colorInt) {
            params.box_bg_color_id = colorInt;
            return this;
        }

        public MenuBox create() {
            final MenuBox menuBox = new MenuBox(params.mContext);
            if (null !=itemClick && params.layoutResId != 0)
                menuBox.menuController.setOnItemClick(new IMenuItemClick() {
                    @Override
                    public void onItemClick(MenuItem item) {
                        menuBox.dismiss();
                        itemClick.onItemClick(item);
                    }
                });
            if (null != menuItemList)
                menuBox.menuController.addMenuItems(menuItemList);

            params.apply(menuBox.menuController);
            //测量View的宽高
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            menuBox.menuController.mPopupView.measure(w, h);
            return menuBox;
        }
    }

    public void showFromTop(View anchorView, Activity activity) {
        if (isShowing()) return;
        showAtLocation(anchorView, Gravity.START | Gravity.TOP, 0, getStatusBarHeight(activity));
    }

    public void showFromTop(View anchorView, int offset) {
        if (isShowing()) return;
        showAtLocation(anchorView, Gravity.START | Gravity.TOP, 0, offset);
    }

    public void showFromBottom(View anchorView, Activity activity) {
        if (isShowing()) return;
        showAtLocation(anchorView, Gravity.START | Gravity.BOTTOM, 0, getNavigationBarHeight(activity));
    }

    private int getStatusBarHeight(Activity activity) {
        if (ScreenUtil.isStatusBarExist(activity))
            return ScreenUtil.getInstance(activity.getApplicationContext()).getStatusBarHeight();
        else
            return 0;
    }

    private int getNavigationBarHeight(Activity activity) {
        if (ScreenUtil.isNavigationBarExist(activity))
            return ScreenUtil.getInstance(activity.getApplicationContext()).getNavigationHeight();
        else
            return 0;
    }
}
