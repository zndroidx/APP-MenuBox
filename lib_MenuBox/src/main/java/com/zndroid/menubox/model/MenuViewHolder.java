package com.zndroid.menubox.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zndroid.menubox.R;

/**
 * Created by lzy on 2020/5/27.
 */
public class MenuViewHolder extends RecyclerView.ViewHolder {
    public ImageView itemImageView;
    public TextView itemTitleTextView;
    public ImageView redDotImageView;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        itemImageView = itemView.findViewById(R.id.im_menu_icon);
        itemTitleTextView = itemView.findViewById(R.id.tv_menu_title);
        redDotImageView = itemView.findViewById(R.id.im_red_dot);
    }
}
