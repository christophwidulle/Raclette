package de.chefkoch.raclette.android.support;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.chefkoch.raclette.android.AdapterItemClickListener;
import de.chefkoch.raclette.android.BindingDecorator;

import java.util.*;


/**
 * Created by christophwidulle on 16.04.16.
 */
public class BindingAdapter<T> extends RecyclerView.Adapter<BindingAdapter.BasicViewHolder<T>> {

    final private int itemLayoutResource;
    private int itemModelBindingId = -1;
    private Map<Integer, Object> bindings;

    private BindingDecorator bindingDecorator;
    private AdapterItemClickListener<T> itemClickListener;

    private List<T> items = new ArrayList<>();

    private BindingAdapter(int itemLayoutResource) {
        this.itemLayoutResource = itemLayoutResource;
    }

    public void setAll(Collection<T> items) {
        if (items != null) {
            this.items = new ArrayList<>(items);
            notifyDataSetChanged();
        } else {
            removeAll();
        }
    }

    public void addAll(Collection<T> items) {
        if (items != null) {
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void add(T item) {
        if (item != null) {
            this.items.add(item);
            notifyDataSetChanged();
        }
    }

    public void removeAll() {
        items = new ArrayList<T>();
        notifyDataSetChanged();
    }

    private void setItemClickListener(AdapterItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void setBindingDecorator(BindingDecorator bindingDecorator) {
        this.bindingDecorator = bindingDecorator;
    }

    private void setBindings(Map<Integer, Object> bindings) {
        this.bindings = bindings;
    }

    private void setItemModelBindingId(int itemModelBindingId) {
        this.itemModelBindingId = itemModelBindingId;
    }

    @Override
    public BasicViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                itemLayoutResource,
                parent,
                false);

        if (bindingDecorator != null) {
            bindingDecorator.decorate(binding);
        }
        return new BasicViewHolder<T>(binding, itemModelBindingId, itemClickListener, bindings);
    }

    @Override
    public void onBindViewHolder(BasicViewHolder<T> holder, int position) {
        if (itemModelBindingId != -1) {
            holder.bind(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class BasicViewHolder<T> extends RecyclerView.ViewHolder {
        private final ViewDataBinding viewBinding;
        private final int itemModelBindingId;
        private final Map<Integer, Object> bindings;
        private T item;

        BasicViewHolder(final ViewDataBinding viewBinding,
                        final int itemModelBindingId,
                        final AdapterItemClickListener<T> itemClickListener, Map<Integer, Object> bindings) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
            this.itemModelBindingId = itemModelBindingId;
            this.bindings = bindings;

            if (itemClickListener != null) {
                viewBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onClick(item, getAdapterPosition(), viewBinding.getRoot());
                    }
                });
            }
            if (this.bindings != null) {
                for (Map.Entry<Integer, Object> itemBinding : this.bindings.entrySet()) {
                    viewBinding.setVariable(itemBinding.getKey(), itemBinding.getValue());
                }
            }
        }

        void bind(T item) {
            this.item = item;
            this.viewBinding.setVariable(itemModelBindingId, item);
        }
    }

    public static <T> Builder<T> builder(int layoutResource) {
        return new Builder<>(layoutResource);
    }

    public static class Builder<T> {
        private int itemLayoutResource;
        private int itemModelBindingId = -1;
        private AdapterItemClickListener<T> itemClickListener;
        private BindingDecorator bindingDecorator;
        private Map<Integer, Object> bindings;

        public Builder(int itemLayoutResource) {
            this.itemLayoutResource = itemLayoutResource;
        }

        public Builder<T> withBinding(int bindingId, Object object) {
            if (bindings == null) bindings = new HashMap<>();
            bindings.put(bindingId, object);
            return this;
        }

        public Builder<T> withItemModelBindingAs(int itemModelBindingId) {
            this.itemModelBindingId = itemModelBindingId;
            return this;
        }

        public Builder<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        public Builder<T> withBindingDecorator(BindingDecorator bindingDecorator) {
            this.bindingDecorator = bindingDecorator;
            return this;
        }

        public BindingAdapter<T> build() {
            BindingAdapter<T> bindingAdapter = new BindingAdapter<>(itemLayoutResource);

            bindingAdapter.setBindings(bindings);

            if (itemModelBindingId != -1) {
                bindingAdapter.setItemModelBindingId(itemModelBindingId);
            }
            if (itemClickListener != null) {
                bindingAdapter.setItemClickListener(itemClickListener);
            }
            bindingAdapter.setBindingDecorator(bindingDecorator);
            return bindingAdapter;
        }

    }
}