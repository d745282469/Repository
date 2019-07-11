package com.dong.repository.Show;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.dong.repository.Base.BaseAdapter;
import com.dong.repository.Base.BaseViewHolder;
import com.dong.repository.R;

import java.util.List;

/**
 * @author pd
 * time     2019/4/26 08:37
 */
public class TestAdapter extends BaseAdapter {
    private List<String> itemList;
    public TestAdapter(Context context, List<String> itemList, int itemViewId) {
        super(context, itemList, itemViewId);
        this.itemList = itemList;
    }

    @Override
    public void onActive(@NonNull BaseViewHolder holder, int position) {
        holder.setText(R.id.item_scroll_menu_tv_content,itemList.get(position));
    }
}
