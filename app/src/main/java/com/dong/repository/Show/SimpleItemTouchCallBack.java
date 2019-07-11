package com.dong.repository.Show;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.dong.repository.Base.BaseAdapter;

/**
 * @author pd
 * time     2019/4/26 08:59
 * 这个例子实现了拖拽移动以及手指从右向左滑动删除
 */
public class SimpleItemTouchCallBack extends ItemTouchHelper.Callback {
    private BaseAdapter adapter;//adapter是自己的

    public SimpleItemTouchCallBack(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 返回滑动的方向
     * 如：只允许从右向左滑动删除、上下拖动等
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//允许上下拖动
        int swipeFlag = ItemTouchHelper.LEFT;//只允许从右向左滑动
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    /**
     * 当一个Item被拖动的时候调用该方法
     * 可以在这里通知RecyclerView将数据进行位置交换
     *
     * @param recyclerView
     * @param viewHolder   拖动前的vh
     * @param viewHolder1  拖动后的vh
     * @return true:表示Item已经到达目的位置
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        return true;
    }

    /**
     * 当Item滑动到触发删除条件时调用
     * 如果不在该方法内执行删除数据的操作，会留下一块空白区域
     * 因为此时的数据并没有删除，只是把数据对应的Item移出视野范围而已
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        adapter.onItemRemove(viewHolder.getAdapterPosition());
    }

    /**
     * 是否开启长按拖动，默认true
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    /**
     * 是否开启滑动删除Item，默认true
     *
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    /**
     * 从静止状态变为拖动状态时调用
     *
     * @param viewHolder
     * @param actionState 当前状态
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 当用户对某个Item操作完成并且动画执行完成后调用
     * 一般在这里对Item进行一些初始化操作，防止一些由于复用导致的问题
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }

    /**
     * 可以自定义一些动画逻辑
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
