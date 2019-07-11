package com.dong.repository.Base;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author pd
 * time     2019/4/22 08:42
 * 仅仅提供一些便捷的方法而已，并没有什么特别的地方
 * 比如之前的finidViewById被缩短为getView(int id)这样的形式
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    private View itemView;
    private int position;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public <V extends View> V getView(@IdRes int id) {
        return (V) itemView.findViewById(id);
    }

    public View getItemView() {
        return itemView;
    }

    public void setItemView(View itemView) {
        this.itemView = itemView;
    }

    public BaseViewHolder setText(int id, String content) {
        ((TextView) getView(id)).setText(content);
        return this;
    }

    public BaseViewHolder setImg(int id, Bitmap resource) {
        ((ImageView) getView(id)).setImageBitmap(resource);
        return this;
    }

    public BaseViewHolder setImg(int id, Drawable resource) {
        ((ImageView) getView(id)).setImageDrawable(resource);
        return this;
    }

    public BaseViewHolder setBg(int id, Drawable drawable) {
        getView(id).setBackground(drawable);
        return this;
    }

    public BaseViewHolder setBgRes(int id, int resourceId) {
        getView(id).setBackgroundResource(resourceId);
        return this;
    }

    public BaseViewHolder setBgColor(int id, int color) {
        getView(id).setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBgColor(int id, String colorStr) {
        getView(id).setBackgroundColor(Color.parseColor(colorStr));
        return this;
    }

    public BaseViewHolder setTextColor(int id, int color) {
        ((TextView) getView(id)).setTextColor(color);
        return this;
    }

    public BaseViewHolder setTextColor(int id, String colorStr) {
        ((TextView) getView(id)).setTextColor(Color.parseColor(colorStr));
        return this;
    }
}
