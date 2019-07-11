package com.dong.repository.Base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * @author pd
 * time     2019/4/22 08:51
 * 如果要使用自定义的ViewHolder，必须重写createMyViewHolder();
 * 并且自定义的ViewHolder必须继承BaseViewHolder
 */
public abstract class BaseAdapter<B extends BaseViewHolder> extends RecyclerView.Adapter<B> {
    private static final String TAG = "BaseAdapter";
    private Context context;
    private List itemList;
    private int itemViewId;
    private int itemCount;

    private int headerViewId = -1, footerViewId = -1;
    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_FOOTER = 2;

    //监听器相关变量
    private onItemClickListener onItemClickListener;
    private onItemLongClickListener onItemLongClickListener;

    /**
     * 普通构造函数
     *
     * @param context    上下文
     * @param itemList   数据源
     * @param itemViewId item布局文件的资源id
     */
    public BaseAdapter(Context context, List itemList, @IdRes int itemViewId) {
        this.context = context;
        this.itemList = itemList;
        this.itemViewId = itemViewId;
    }

    /**
     * 带headerView的构造参数
     *
     * @param context      上下文
     * @param itemList     数据源
     * @param itemViewId   item布局文件的资源id
     * @param headerViewId header布局文件的资源id
     */
    public BaseAdapter(Context context, List itemList, @IdRes int itemViewId, @IdRes int headerViewId) {
        this.context = context;
        this.itemList = itemList;
        this.itemViewId = itemViewId;
        this.headerViewId = headerViewId;
    }

    @SuppressWarnings("unchecked")
    public B createMyViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public B onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int viewType) {
        View itemView = null;

        //根据itemType的不同来加载不同的item布局文件
        if (headerViewId != -1 && viewType == VIEW_TYPE_HEADER) {
            itemView = LayoutInflater.from(context).inflate(headerViewId, viewGroup, false);
        } else {
            itemView = LayoutInflater.from(context).inflate(itemViewId, viewGroup, false);
        }

        B b = createMyViewHolder(viewGroup, viewType);
        //如果没有重写使用自己的ViewHolder，那就默认使用Base
        if (b == null) {
            b = (B) new BaseViewHolder(itemView);
        }

        bindListener(b);//绑定各种监听器
        return b;
    }

    @Override
    public void onBindViewHolder(@NonNull B b, int i) {
        if (headerViewId != -1) {
            //headerView的本质就是在数据源中新增一条数据，但是不显示出来而是加载headerView的布局
            if (getItemViewType(i) != VIEW_TYPE_HEADER) {
                onActive(b, i - 1);
            }
        } else {
            onActive(b, i);
        }
    }

    /**
     * 子adapter中以这一个中的回调为准，onBinViewHolder在设置了headerView和footerView之后，
     * position需要进行相应的加减操作
     *
     * @param b        viewHolder
     * @param position index，这里的Position是真正和原始数据源对应的
     */
    public abstract void onActive(@NonNull B b, int position);

    /**
     * 给ItemTouchHelper用的
     * 可用于实现拖动效果
     *
     * @param fromPosition 起始位置
     * @param toPosition   结束位置
     */
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(itemList, fromPosition, toPosition);//交换数据源中两个数据的位置
        notifyItemMoved(fromPosition, toPosition);//通知rv改变
    }

    /**
     * 也是给ItemTouchHelper用的
     * 可用于实现滑动删除(不是侧滑菜单)
     *
     * @param position 删除的位置
     */
    public void onItemRemove(int position) {
        itemList.remove(position);//在数据源中删除该数据
        notifyItemRemoved(position);//通知改变
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && headerViewId != -1) {
            return VIEW_TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        itemCount = itemList.size();
        if (headerViewId != -1) {
            itemCount++;
        }
        return itemCount;
    }

    /*
     ↓↓↓↓↓↓↓↓↓↓↓↓↓监听器相关↓↓↓↓↓↓↓↓↓↓↓↓↓
     */

    /**
     * 绑定各种事件监听器
     *
     * @param vh
     */
    private void bindListener(final B vh) {
        if (onItemClickListener != null) {
            vh.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = vh.getAdapterPosition();

                    //普通的item才触发点击事件
                    if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
                        //设置了header需要在数据源中-1才能拿到真正对应的数据
                        if (headerViewId != -1) {
                            position--;
                        }
                        onItemClickListener.onItemClick(vh, position);
                    }
                }
            });
        }

        if (onItemLongClickListener != null && getItemViewType(vh.getAdapterPosition()) == VIEW_TYPE_NORMAL) {
            vh.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = vh.getAdapterPosition();

                    //普通item才触发
                    if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
                        //设置了header需要在数据源中-1才能拿到真正对应的数据
                        if (headerViewId != -1) {
                            position--;
                        }
                        return onItemLongClickListener.onItemLongClick(vh, position);
                    }
                    return false;
                }
            });
        }
    }

    public void setOnItemClickListener(BaseAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(BaseAdapter.onItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(BaseViewHolder viewHolder, int position);
    }

    public interface onItemLongClickListener {
        boolean onItemLongClick(BaseViewHolder viewHolder, int position);
    }
}
