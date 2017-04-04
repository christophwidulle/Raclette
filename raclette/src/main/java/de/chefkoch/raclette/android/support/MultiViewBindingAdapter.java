package de.chefkoch.raclette.android.support;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.adapters.ViewBindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.*;

import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.android.AdapterItemClickListener;
import de.chefkoch.raclette.android.BindingDecorator;


public class MultiViewBindingAdapter<T> extends RecyclerView.Adapter<MultiViewBindingAdapter.BasicViewHolder<T>> {

    private ItemViewTypeMapping<T> itemViewTypeMapping = new DefaultItemViewTypeMapping<>();

    private Map<Integer, MultiBindingElement> bindingElements;

    private BindingDecorator bindingDecorator;
    private AdapterItemClickListener<T> itemClickListener;

    private List<T> items = new ArrayList<>();

    private MultiViewBindingAdapter(HashMap<Integer, MultiBindingElement> bindingElements) {
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

    private void setItemClickListener(AdapterItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void setBindingDecorator(BindingDecorator bindingDecorator) {
        this.bindingDecorator = bindingDecorator;
    }


    @Override
    public BasicViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        MultiBindingElement multiBindingElement = bindingElements.get(viewType);
        ViewDataBinding binding = DataBindingUtil.inflate(
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


    public static <T> ByClassBuilder<T> builder() {
        return new Builder<>();
    }

    public static <T> ViewTypeBuilder<T> builder(ItemViewTypeMapping<T> itemViewTypeMapping) {
        return new Builder<>(itemViewTypeMapping);
    }


    public static class Builder<T> implements ByClassBuilder<T>, ViewTypeBuilder<T> {

        private ItemViewTypeMapping<T> itemViewTypeMapping;
        private AdapterItemClickListener<T> itemClickListener;
        private BindingDecorator bindingDecorator;
        private HashMap<Integer, MultiBindingElement> multiBindingElements = new HashMap<>();

        public Builder() {
        }

        public Builder(ItemViewTypeMapping<T> itemViewTypeMapping) {
            this.itemViewTypeMapping = itemViewTypeMapping;
        }

        @Override
        public Builder<T> withItemBinding(Class klass, int itemBindingId, int itemLayoutResource) {

            MultiBindingElement multiBindingElement = new MultiBindingElement(
                    itemBindingId,
                    itemLayoutResource);

            this.multiBindingElements.put(klass.hashCode(), multiBindingElement);
            return this;
        }

        @Override
        public Builder<T> withItemBinding(Class klass, int itemBindingId, int itemLayoutResource, int viewModelBindingId, ViewModel viewModel) {

            MultiBindingElement multiBindingElement = new MultiBindingElement(viewModelBindingId,
                    itemBindingId,
                    itemLayoutResource,
                    viewModel);

            this.multiBindingElements.put(klass.hashCode(), multiBindingElement);
            return this;
        }

        @Override
        public Builder<T> withItemBinding(int itemViewType, int itemBindingId, int itemLayoutResource, int viewModelBindingId, ViewModel viewModel) {

            MultiBindingElement multiBindingElement = new MultiBindingElement(viewModelBindingId,
                    itemBindingId,
                    itemLayoutResource,
                    viewModel);

            this.multiBindingElements.put(itemViewType, multiBindingElement);
            return this;
        }

        @Override
        public Builder<T> withItemBinding(int itemViewType, int itemBindingId, int itemLayoutResource) {

            MultiBindingElement multiBindingElement = new MultiBindingElement(
                    itemBindingId,
                    itemLayoutResource);

            this.multiBindingElements.put(itemViewType, multiBindingElement);
            return this;
        }

        @Override
        public Builder<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        @Override
        public Builder<T> withBindingDecorator(BindingDecorator bindingDecorator) {
            this.bindingDecorator = bindingDecorator;
            return this;
        }

        @Override
        public MultiViewBindingAdapter<T> build() {

            MultiViewBindingAdapter<T> bindingAdapter = new MultiViewBindingAdapter<>(multiBindingElements);
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

    public interface ByClassBuilder<T> {
        Builder<T> withItemBinding(Class klass, int itemBindingId, int itemLayoutResource, int viewModelBindingId, ViewModel viewModel);

        Builder<T> withItemBinding(Class klass, int itemBindingId, int itemLayoutResource);

        Builder<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener);

        Builder<T> withBindingDecorator(BindingDecorator bindingDecorator);

        MultiViewBindingAdapter<T> build();
    }

    public interface ViewTypeBuilder<T> {
        Builder<T> withItemBinding(int itemViewType, int itemBindingId, int itemLayoutResource, int viewModelBindingId, ViewModel viewModel);

        Builder<T> withItemBinding(int itemViewType, int itemBindingId, int itemLayoutResource);

        Builder<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener);

        Builder<T> withBindingDecorator(BindingDecorator bindingDecorator);

        MultiViewBindingAdapter<T> build();
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