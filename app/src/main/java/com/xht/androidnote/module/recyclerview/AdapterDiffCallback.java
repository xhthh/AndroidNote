package com.xht.androidnote.module.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.xht.androidnote.module.listview.LvEntity;

import java.util.List;

/**
 * 用来判断 新旧Item是否相等
 * Created by xht on 2019/4/11.
 */

public class AdapterDiffCallback extends DiffUtil.Callback {
    private List<LvEntity> mOldList;
    private List<LvEntity> mNewList;

    public AdapterDiffCallback(List<LvEntity> oldList, List<LvEntity> newList) {
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList == null ? 0 : mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList == null ? 0 : mNewList.size();
    }

    /**
     * 被DiffUtil调用，用来判断 两个对象是否是相同的Item。
     * 例如，如果你的Item有唯一的id字段，这个方法就 判断id是否相等。
     * 本例判断name字段是否一致
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).title.equals(mNewList.get(newItemPosition).title);
    }

    /**
     * 被DiffUtil调用，用来检查 两个item是否含有相同的数据
     * DiffUtil用返回的信息（true false）来检测当前item的内容是否发生了变化
     * DiffUtil 用这个方法替代equals方法去检查是否相等。
     * 所以你可以根据你的UI去改变它的返回值
     * 例如，如果你用RecyclerView.Adapter 配合DiffUtil使用，你需要返回Item的视觉表现是否相同。
     * 这个方法仅仅在areItemsTheSame()返回true时，才调用。
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        LvEntity oldEntity = mOldList.get(oldItemPosition);
        LvEntity newEntity = mNewList.get(newItemPosition);

        return oldEntity.imgId == newEntity.imgId;
    }

    /**
     * 当{@link #areItemsTheSame(int, int)} 返回true，且{@link #areContentsTheSame(int, int)} 返回false时，DiffUtils会回调此方法，
     * 去得到这个Item（有哪些）改变的payload。
     * <p>
     * 例如，如果你用RecyclerView配合DiffUtils，你可以返回  这个Item改变的那些字段，
     * {@link android.support.v7.widget.RecyclerView.ItemAnimator ItemAnimator} 可以用那些信息去执行正确的动画
     * <p>
     * Default implementation returns {@code null}.\
     * 默认的实现是返回null
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list
     * @return A payload object that represents the change between the two items.
     * 返回 一个 代表着新老item的改变内容的 payload对象，
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        LvEntity oldEntity = mOldList.get(oldItemPosition);
        LvEntity newEntity = mNewList.get(newItemPosition);

        Bundle payload = new Bundle();

        if (oldEntity.imgId != newEntity.imgId) {
            payload.putInt("imgid", newEntity.imgId);
        }

        if (payload.size() == 0) {
            return null;
        }

        return payload;
    }
}
