package de.chefkoch.raclette.android.support;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.android.AdapterItemClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * Use {@link MultiViewBindingAdapter}
 *
 * @param <B>
 */
@Deprecated
public class MultiBindingAdapter<B extends ViewDataBinding> extends RecyclerView.Adapter<MultiBindingAdapter.BasicViewHolder<Object>> {

    private HashMap<Integer, MultiBindingElement> bindingElements;

    private AdapterItemClickListener<Object> itemClickListener;

    private List<Object> items = new ArrayList<>();

    public MultiBindingAdapter(HashMap<Integer, MultiBindingElement> bindingElements) {
        this.bindingElements = bindingElements;
    }

    public MultiBindingAdapter() {
    }

    public void setAll(Collection<?> items) {
        if (items != null) {
            this.items = new ArrayList<>(items);
            notifyDataSetChanged();
        } else {
            removeAll();
        }
    }

    public void addAll(Collection<?> items) {
        if (items != null) {
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void add(Object item) {
        if (item != null) {
            this.items.add(item);
            notifyDataSetChanged();
        }
    }

    public void removeAll() {
        items = new ArrayList<>();
        notifyDataSetChanged();
    }

    protected void setItemClickListener(AdapterItemClickListener<Object> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public BasicViewHolder<Object> onCreateViewHolder(ViewGroup parent, int viewType) {
        MultiBindingElement multiBindingElement = bindingElements.get(viewType);
        B binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                multiBindingElement.itemLayoutResource,
                parent,
                false);

        return new BasicViewHolder<>(binding,
                multiBindingElement.itemBindingId,
                multiBindingElement.viewModelBindingId,
                multiBindingElement.viewModel,
                itemClickListener);
    }


    @Override
    public void onBindViewHolder(BasicViewHolder<Object> holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        return item.getClass().hashCode();
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
                        final int viewModelBindingId,
                        final ViewModel viewModel,
                        final AdapterItemClickListener<T> itemClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.bindingId = bindingId;

            if (itemClickListener != null) {
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onClick(item, getAdapterPosition(), binding.getRoot());
                    }
                });
            }
            binding.setVariable(viewModelBindingId, viewModel);
        }

        void bind(T item) {
            this.item = item;
            this.binding.setVariable(bindingId, item);
        }
    }


    public static <B extends ViewDataBinding> Builder<B> builder() {
        return new Builder<>();
    }


    public static class Builder<B extends ViewDataBinding> {
        private AdapterItemClickListener<Object> itemClickListener;
        private HashMap<Integer, MultiBindingElement> multiBindingElements = new HashMap<>();

        public Builder<B> withItemBinding(Class klass, int vireModelBindingId, int itemBindingId, int itemLayoutResource, ViewModel viewModel) {

            MultiBindingElement multiBindingElement = new MultiBindingElement(vireModelBindingId,
                    itemBindingId,
                    itemLayoutResource,
                    viewModel);

            this.multiBindingElements.put(klass.hashCode(), multiBindingElement);
            return this;
        }

        public Builder() {
        }

        public Builder<B> withItemClickListener(AdapterItemClickListener<Object> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        public MultiBindingAdapter<B> build() {

            MultiBindingAdapter<B> bindingAdapter = new MultiBindingAdapter<>(multiBindingElements);
            if (itemClickListener != null) {
                bindingAdapter.setItemClickListener(itemClickListener);
            }
            return bindingAdapter;
        }

    }


    public static class MultiBindingElement {
        private int itemLayoutResource;
        private int itemBindingId = -1;
        private int viewModelBindingId = -1;
        private ViewModel viewModel;

        public MultiBindingElement(int itemLayoutResource) {
            this.itemLayoutResource = itemLayoutResource;
        }

        public MultiBindingElement(int viewModelBindingId, int itemBindingId, int itemLayoutResource, ViewModel viewModel) {
            this.viewModelBindingId = viewModelBindingId;
            this.itemBindingId = itemBindingId;
            this.itemLayoutResource = itemLayoutResource;
            this.viewModel = viewModel;
        }

        public int getItemLayoutResource() {
            return itemLayoutResource;
        }

        public int getItemBindingId() {
            return itemBindingId;
        }

        public int getViewModelBindingId() {
            return viewModelBindingId;
        }

        public ViewModel getViewModel() {
            return viewModel;
        }

        public void setViewModel(ViewModel viewModel) {
            this.viewModel = viewModel;
        }
    }
}