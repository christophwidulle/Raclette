package de.chefkoch.raclette.android.support;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.chefkoch.raclette.android.AdapterItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christophwidulle on 16.04.16.
 */
public class SimpleBindingAdapter<T> extends RecyclerView.Adapter<SimpleBindingAdapter.BasicViewHolder<T>> {

    final private int bindingId;
    final private int layoutResource;
    private AdapterItemClickListener<T> itemClickListener = new AdapterItemClickListener.Empty<T>();

    final private List<T> items = new ArrayList<>();

    public SimpleBindingAdapter(int itemBindingId, int layoutResource) {
        this.bindingId = itemBindingId;
        this.layoutResource = layoutResource;
    }

    public void addAll(List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void add(T item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void setItemClickListener(AdapterItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public BasicViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                layoutResource,
                parent,
                false);

        return new BasicViewHolder<T>(binding, bindingId, itemClickListener);
    }


    @Override
    public void onBindViewHolder(BasicViewHolder<T> holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class BasicViewHolder<T> extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;
        private final int bindingId;
        private T item;

        BasicViewHolder(final ViewDataBinding binding,
                        final int bindingId,
                        final AdapterItemClickListener<T> itemClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.bindingId = bindingId;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(item, getAdapterPosition(), binding.getRoot());
                }
            });
        }

        void bind(T item) {
            this.item = item;
            this.binding.setVariable(bindingId, item);
        }
    }


}