package de.chefkoch.raclette.android.support;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class AdapterDiff<T> extends DiffUtil.Callback {


    private final ItemViewTypeMapping<T> itemViewTypeMapping;

    private final List<T> oldList;
    private final List<T> newList;

    public AdapterDiff(ItemViewTypeMapping<T> itemViewTypeMapping, List<T> oldList, List<T> newList) {
        this.itemViewTypeMapping = itemViewTypeMapping;
        this.oldList = oldList;
        this.newList = newList;
    }


    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldList.get(oldItemPosition);
        T newItem = newList.get(newItemPosition);
        return itemViewTypeMapping.getItemViewTypeFor(oldItem) == itemViewTypeMapping.getItemViewTypeFor(newItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldList.get(oldItemPosition);
        T newItem = newList.get(newItemPosition);
        return oldItem.equals(newItem);
    }
}
