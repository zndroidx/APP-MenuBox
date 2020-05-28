package com.zndroid.menubox.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zndroid.menubox.core.IMenuItemClick;
import com.zndroid.menubox.core.MenuItem;
import com.zndroid.menubox.R;

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
                        if (null != iMenuItemClick) {
                            holder.redDotImageView.setVisibility(View.GONE);
                            iMenuItemClick.onItemClick(item);
                        }
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
