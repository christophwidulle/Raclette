package de.chefkoch.raclette.android.support;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.android.AdapterItemClickListener;
import de.chefkoch.raclette.android.BindingDecorator;


public class MultiViewBindingAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<MultiViewBindingAdapter.BasicViewHolder<T>> {

    private ItemViewTypeMapping<T> itemViewTypeMapping = new DefaultItemViewTypeMapping<>();

    private HashMap<Integer, MultiBindingElement> bindingElements;

    private BindingDecorator<B> bindingDecorator;
    private AdapterItemClickListener<T> itemClickListener;

    private List<T> items = new ArrayList<>();

    public MultiViewBindingAdapter(HashMap<Integer, MultiBindingElement> bindingElements) {
        this.bindingElements = bindingElements;
    }

    public MultiViewBindingAdapter() {
    }

    public void setAll(Collection<T> items) {
        if (items != null) {
            this.items = new ArrayList<T>(items);
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
        items = new ArrayList<>();
        notifyDataSetChanged();
    }

    protected void setItemClickListener(AdapterItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void setBindingDecorator(BindingDecorator<B> bindingDecorator) {
        this.bindingDecorator = bindingDecorator;
    }


    @Override
    public BasicViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        MultiBindingElement multiBindingElement = bindingElements.get(viewType);
        B binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                multiBindingElement.itemLayoutResource,
                parent,
                false);

        if (bindingDecorator != null) {
            bindingDecorator.decorate(binding);
        }
        return new BasicViewHolder<T>(binding,
                multiBindingElement.itemBindingId,
                multiBindingElement.viewModelBindingId,
                multiBindingElement.viewModel,
                itemClickListener);
    }


    @Override
    public void onBindViewHolder(BasicViewHolder<T> holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        T item = items.get(position);
        return viewType(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private void setItemViewTypeMapping(ItemViewTypeMapping<T> itemViewTypeMapping) {
        this.itemViewTypeMapping = itemViewTypeMapping;
    }


    private int viewType(T item) {
        return itemViewTypeMapping.getItemViewTypeFor(item);
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
            if (viewModelBindingId != -1) {
                binding.setVariable(viewModelBindingId, viewModel);
            }
        }

        void bind(T item) {
            this.item = item;
            this.binding.setVariable(bindingId, item);
        }
    }


    public static <T, B extends ViewDataBinding> ByClassBuilder<T, B> builder() {
        return new Builder<>();
    }

    public static <T, B extends ViewDataBinding> ViewTypeBuilder<T, B> builder(ItemViewTypeMapping<T> itemViewTypeMapping) {
        return new Builder<>(itemViewTypeMapping);
    }


    public static class Builder<T, B extends ViewDataBinding> implements ByClassBuilder<T, B>, ViewTypeBuilder<T, B> {

        private ItemViewTypeMapping<T> itemViewTypeMapping;
        private AdapterItemClickListener<T> itemClickListener;
        private BindingDecorator<B> bindingDecorator;
        private HashMap<Integer, MultiBindingElement> multiBindingElements = new HashMap<>();

        public Builder() {
        }

        public Builder(ItemViewTypeMapping<T> itemViewTypeMapping) {
            this.itemViewTypeMapping = itemViewTypeMapping;
        }

        @Override
        public Builder<T, B> withItemBinding(Class klass, int itemBindingId, int itemLayoutResource) {

            MultiBindingElement multiBindingElement = new MultiBindingElement(
                    itemBindingId,
                    itemLayoutResource);

            this.multiBindingElements.put(klass.hashCode(), multiBindingElement);
            return this;
        }

        @Override
        public Builder<T, B> withItemBinding(Class klass, int itemBindingId, int itemLayoutResource, int viewModelBindingId, ViewModel viewModel) {

            MultiBindingElement multiBindingElement = new MultiBindingElement(viewModelBindingId,
                    itemBindingId,
                    itemLayoutResource,
                    viewModel);

            this.multiBindingElements.put(klass.hashCode(), multiBindingElement);
            return this;
        }

        @Override
        public Builder<T, B> withItemBinding(int itemViewType, int itemBindingId, int itemLayoutResource, int viewModelBindingId, ViewModel viewModel) {

            MultiBindingElement multiBindingElement = new MultiBindingElement(viewModelBindingId,
                    itemBindingId,
                    itemLayoutResource,
                    viewModel);

            this.multiBindingElements.put(itemViewType, multiBindingElement);
            return this;
        }

        @Override
        public Builder<T, B> withItemBinding(int itemViewType, int itemBindingId, int itemLayoutResource) {

            MultiBindingElement multiBindingElement = new MultiBindingElement(
                    itemBindingId,
                    itemLayoutResource);

            this.multiBindingElements.put(itemViewType, multiBindingElement);
            return this;
        }

        @Override
        public Builder<T, B> withItemClickListener(AdapterItemClickListener<T> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        @Override
        public Builder<T, B> withBindingDecorator(BindingDecorator<B> bindingDecorator) {
            this.bindingDecorator = bindingDecorator;
            return this;
        }

        @Override
        public MultiViewBindingAdapter<T, B> build() {

            MultiViewBindingAdapter<T, B> bindingAdapter = new MultiViewBindingAdapter<>(multiBindingElements);
            if (itemClickListener != null) {
                bindingAdapter.setItemClickListener(itemClickListener);
            }
            if (itemViewTypeMapping != null) {
                bindingAdapter.setItemViewTypeMapping(itemViewTypeMapping);
            }
            bindingAdapter.setBindingDecorator(bindingDecorator);
            return bindingAdapter;
        }

    }

    public interface ByClassBuilder<T, B extends ViewDataBinding> {
        Builder<T, B> withItemBinding(Class klass, int itemBindingId, int itemLayoutResource, int viewModelBindingId, ViewModel viewModel);

        Builder<T, B> withItemBinding(Class klass, int itemBindingId, int itemLayoutResource);

        Builder<T, B> withItemClickListener(AdapterItemClickListener<T> itemClickListener);

        Builder<T, B> withBindingDecorator(BindingDecorator<B> bindingDecorator);

        MultiViewBindingAdapter<T, B> build();
    }

    public interface ViewTypeBuilder<T, B extends ViewDataBinding> {
        Builder<T, B> withItemBinding(int itemViewType, int itemBindingId, int itemLayoutResource, int viewModelBindingId, ViewModel viewModel);

        Builder<T, B> withItemBinding(int itemViewType, int itemBindingId, int itemLayoutResource);

        Builder<T, B> withItemClickListener(AdapterItemClickListener<T> itemClickListener);

        Builder<T, B> withBindingDecorator(BindingDecorator<B> bindingDecorator);

        MultiViewBindingAdapter<T, B> build();
    }


    public static class MultiBindingElement {
        private int itemLayoutResource;
        private int itemBindingId = -1;
        private int viewModelBindingId = -1;
        private ViewModel viewModel;

        public MultiBindingElement(int itemLayoutResource) {
            this.itemLayoutResource = itemLayoutResource;
        }

        public MultiBindingElement(int itemBindingId, int itemLayoutResource) {
            this.itemBindingId = itemBindingId;
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