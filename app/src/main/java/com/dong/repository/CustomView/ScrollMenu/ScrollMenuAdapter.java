package com.dong.repository.CustomView.ScrollMenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dong.repository.R;

import java.util.List;

/**
 * @author pd
 * time     2019/4/16 15:22
 */
public class ScrollMenuAdapter extends RecyclerView.Adapter<ScrollMenuAdapter.ViewHolder> {
    private Context context;
    private List<String> itemList;

    public ScrollMenuAdapter(Context context, List<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ScrollMenuLayout scrollMenuLayout = new ScrollMenuLayout(context, null);
        scrollMenuLayout.setItemView(LayoutInflater.from(context).inflate(R.layout.item_scroll_menu, null));
        scrollMenuLayout.setRightMenuView(LayoutInflater.from(context).inflate(R.layout.menu_scroll_menu, null));
        return new ViewHolder(scrollMenuLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, final int i) {
        vh.tv_content.setText(itemList.get(i));
        vh.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.remove(i);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_content;
        Button btn_delete, btn_edit;

        public ViewHolder(@NonNull final View v) {
            super(v);
            tv_content = v.findViewById(R.id.item_scroll_menu_tv_content);
            btn_delete = v.findViewById(R.id.menu_scroll_menu_btn_delete);
            btn_edit = v.findViewById(R.id.menu_scroll_menu_btn_edit);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View item) {
                    Toast.makeText(context, "Click edit btn.", Toast.LENGTH_SHORT).show();
                    ((ScrollMenuLayout) v).closeRightMenu();
                }
            });
        }
    }
}
