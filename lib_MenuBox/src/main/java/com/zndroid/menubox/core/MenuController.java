package com.zndroid.menubox.core;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zndroid.menubox.MenuBox;
import com.zndroid.menubox.R;
import com.zndroid.menubox.model.MenuAdapter;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lzy on 2020/5/27.
 */
public class MenuController {
    private WeakReference<Context> mContextWeakReference;
    private MenuBox popupWindow;

    private boolean isLeftOrRight = false;//判断是否是左右菜单
    private boolean isButtonAnim = false;//按钮动画
    private int layoutResId;    //布局id
    private View mView;         //临时变量
    public View mPopupView;     //弹窗布局View
    private Window mWindow;
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItemList = new CopyOnWriteArrayList<>();
    private IMenuItemClick click;

    @ColorInt
    private int box_title_color;
    private float box_title_front_size;
    private String box_title;
    @DrawableRes
    private int box_close_image;
    private int box_bg_color;

    public MenuController(Context context, MenuBox popupWindow) {
        mContextWeakReference = new WeakReference<>(context);
        this.popupWindow = popupWindow;
    }

    public void addMenuItems(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    public void setView(int layoutResId, int row) {
        mView = null;
        this.layoutResId = layoutResId;
        installContent(row);
    }

    public void setLeftOrRight(boolean isLeftOrRight) {
        this.isLeftOrRight = isLeftOrRight;
    }

    public void setView(View view, int row) {
        mView = view;
        this.layoutResId = 0;
        installContent(row);
    }

    public void setBoxTitle(String title) {
        if (TextUtils.isEmpty(title)) return;
        this.box_title = title;
    }

    public void setBoxTitleColor(@ColorInt int titleColor) {
        if (titleColor <= 0) this.box_title_color = mContextWeakReference.get().getResources().getColor(android.R.color.black);
        this.box_title_color = titleColor;
    }

    public void setBoxTitleSize(float size) {
        if (size <= 0)
            this.box_title_front_size = 14.0f;
        else
            this.box_title_front_size = size;
    }

    public void setBoxCloseImage(@DrawableRes int resId) {
        if (resId == 0)
            this.box_close_image = R.id.im_box_close;
        else
            this.box_close_image = resId;
    }

    public void setBoxBgColor(@ColorInt int color) {
        if (color == 0)
            this.box_bg_color = android.R.color.white;
        else
            this.box_bg_color = color;
    }

    public void setButtonAnim(boolean showAnim) {
        this.isButtonAnim = showAnim;
    }

    private void installContent(int row) {
        if (layoutResId != 0) {
            mPopupView = LayoutInflater.from(mContextWeakReference.get()).inflate(layoutResId, null);
        } else if (mView != null) {
            mPopupView = mView;
        }

        recyclerView = mPopupView.findViewById(R.id.recycler_view);
        menuAdapter = new MenuAdapter();
        menuAdapter.setMenuItems(menuItemList);
        menuAdapter.setItemClick(click);
        menuAdapter.setButtonAnimation(isButtonAnim);

        if (menuItemList.size() < row)
            row = menuItemList.size();

        if (isLeftOrRight) {
            GridLayoutManager layoutManager = new GridLayoutManager(mContextWeakReference.get(), row, GridLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(mContextWeakReference.get(), row, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }

        recyclerView.setAdapter(menuAdapter);

        LinearLayout linearLayout = mPopupView.findViewById(R.id.ll_box_bg);
        if (box_bg_color != 0)
            linearLayout.setBackgroundColor(mContextWeakReference.get().getResources().getColor(box_bg_color));

        TextView textView = mPopupView.findViewById(R.id.tv_box_title);
        if (!TextUtils.isEmpty(box_title)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(box_title);
        } else {
            textView.setVisibility(View.GONE);
        }

        if (box_title_front_size != 0)
            textView.setTextSize(box_title_front_size);
        if (box_title_color != 0)
            textView.setTextColor(box_title_color);

        ImageView closeImageView = mPopupView.findViewById(R.id.im_box_close);
        if (box_close_image != 0)
            closeImageView.setImageDrawable(mContextWeakReference.get().getResources().getDrawable(box_close_image));
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentView(mPopupView);
        popupWindow.setFocusable(true);
    }

    /**
     * 设置宽度
     *
     * @param width  宽
     * @param height 高
     */
    private void setWidthAndHeight(int width, int height) {
        if (width == 0 || height == 0) {
            //如果没设置宽高，默认是WRAP_CONTENT
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
        }
    }


    /**
     * 设置背景灰色程度
     *
     * @param level 0.0f-1.0f
     */
    public void setBackGroundLevel(float level) {
        mWindow = ((Activity) mContextWeakReference.get()).getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = level;
        mWindow.setAttributes(params);
    }


    /**
     * 设置动画
     */
    private void setAnimationStyle(int animationStyle) {
        popupWindow.setAnimationStyle(animationStyle);
    }

    /**
     * 设置Outside是否可点击
     *
     * @param touchable 是否可点击
     */
    private void setOutsideTouchable(boolean touchable) {
        popupWindow.setBackgroundDrawable(null);//设置透明背景
        popupWindow.setOutsideTouchable(touchable);//设置outside可点击
        popupWindow.setFocusable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupWindow.setTouchModal(!touchable);
        } else {
            Method method;
            try {
                method = PopupWindow.class.getDeclaredMethod("setTouchModal", boolean.class);
                method.setAccessible(true);
                method.invoke(popupWindow, !touchable);
            }
            catch (Exception e) {
                Log.e("menu box", "not support");
            }
        }
    }

    public void setOnItemClick(IMenuItemClick click) {
        this.click = click;
    }

    public static class PopupParams {
        @LayoutRes
        public int layoutResId;//布局id
        public Context mContext;
        public int mWidth, mHeight;//弹窗的宽和高
        public boolean isShowBg, isShowAnim, isLeftOrRight, isButtonAnim;
        public float bg_level;//屏幕背景灰色程度
        public int animationStyle;//动画Id
        public int row;
        public View mView;
        public boolean isTouchable = true;
        public String box_title;
        public int box_title_color;
        public float box_title_front_size;
        public int box_close_image_id;
        public int box_bg_color_id;

        public PopupParams(Context mContext) {
            this.mContext = mContext;
        }

        public void apply(MenuController controller) {
            controller.setLeftOrRight(isLeftOrRight);
            controller.setBoxTitle(box_title);
            controller.setBoxCloseImage(box_close_image_id);
            controller.setBoxTitleColor(box_title_color);
            controller.setBoxTitleSize(box_title_front_size);
            controller.setBoxBgColor(box_bg_color_id);
            controller.setButtonAnim(isButtonAnim);

            if (isLeftOrRight)
                layoutResId = R.layout.layout_menu_box_vertical;
            else
                layoutResId = R.layout.layout_menu_box;

            if (mView != null) {
                controller.setView(mView, row);
            } else if (layoutResId != 0) {
                controller.setView(layoutResId, row);
            } else {
                throw new IllegalArgumentException("PopupView's contentView is null");
            }
            controller.setWidthAndHeight(mWidth, mHeight);
            controller.setOutsideTouchable(isTouchable);//设置outside可点击
            if (isShowBg) {
                //设置背景
                controller.setBackGroundLevel(bg_level);
            }
            if (isShowAnim) {
                controller.setAnimationStyle(animationStyle);
            }
        }
    }
}
