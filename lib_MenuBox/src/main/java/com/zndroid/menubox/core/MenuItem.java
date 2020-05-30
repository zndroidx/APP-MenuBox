package com.zndroid.menubox.core;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

/**
 * Created by lzy on 2020/5/27.
 */
public class MenuItem {
    private boolean isShowDot = false;
    private String title;
    @DrawableRes
    private int iconId;
    private int position;
    private float frontSize;
    @ColorInt
    private int frontColor;

    public float getFrontSize() {
        return frontSize;
    }

    public void setFrontSize(float frontSize) {
        this.frontSize = frontSize;
    }

    public int getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(@ColorInt int frontColor) {
        this.frontColor = frontColor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isShowDot() {
        return isShowDot;
    }

    public void setShowDot(boolean showDot) {
        isShowDot = showDot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(@DrawableRes int iconId) {
        this.iconId = iconId;
    }
}
