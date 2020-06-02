package com.zndroid.menubox.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zndroid.menubox.R;
import com.zndroid.menubox.core.IMenuItemClick;
import com.zndroid.menubox.core.MenuItem;
import com.zndroid.menubox.model.MenuViewHolder;

import java.util.List;

/**
 * Created by lzy on 2020/5/27.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {
    private List<MenuItem> menuItems;
    private IMenuItemClick iMenuItemClick;

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_layout, null);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position) {
        if (null != menuItems) {
            final MenuItem item = menuItems.get(position);
            if (null != item) {
                item.setPosition(position);

                holder.itemImageView.setImageResource(item.getIconId());
                holder.itemTitleTextView.setText(item.getTitle());

                if (item.getFrontColor() != 0)
                    holder.itemTitleTextView.setTextColor(item.getFrontColor());
                if (item.getFrontSize() != 0)
                    holder.itemTitleTextView.setTextSize(item.getFrontSize());

                if (item.isShowDot()) {
                    holder.redDotImageView.setVisibility(View.VISIBLE);
                } else //未设置显示
                    holder.redDotImageView.setVisibility(View.GONE);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //按钮动画
                        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f,1.0f, 0.9f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnimation.setDuration(300);
                        scaleAnimation.setFillAfter(true);
                        scaleAnimation.setRepeatMode(Animation.REVERSE);
                        scaleAnimation.setRepeatCount(1);
                        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                holder.redDotImageView.setVisibility(View.GONE);
                                if (null != iMenuItemClick) {
                                    iMenuItemClick.onItemClick(item);
                                }
                                holder.itemView.clearAnimation();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        holder.itemView.startAnimation(scaleAnimation);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (menuItems == null) return 0;
        return menuItems.size();
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    public void setItemClick(IMenuItemClick click) {
        this.iMenuItemClick = click;
    }
}
