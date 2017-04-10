package de.chefkoch.raclette.android.support;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.chefkoch.raclette.android.AdapterItemClickListener;
import de.chefkoch.raclette.android.UpdatableCustomView;

/**
 * Created by christophwidulle on 16.04.16.
 */
public class MultiCustomViewAdapter<T> extends RecyclerView.Adapter<MultiCustomViewAdapter.BasicViewHolder<T>> {

    private final Map<Integer, ElementConfig> elements;
    private AdapterItemClickListener<T> itemClickListener;

    private ItemViewTypeMapping<T> itemViewTypeMapping = new DefaultItemViewTypeMapping<>();

    private List<T> items = new ArrayList<>();

    private MultiCustomViewAdapter(Map<Integer, ElementConfig> elements) {
        this.elements = elements;
    }

    private void setItemViewTypeMapping(ItemViewTypeMapping<T> itemViewTypeMapping) {
        this.itemViewTypeMapping = itemViewTypeMapping;
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
        items = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setItemClickListener(AdapterItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    @SuppressWarnings("unchecked assignement")
    public BasicViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        ElementConfig elementConfig = elements.get(viewType);
        if (elementConfig != null) {
            return new BasicViewHolder<>(itemClickListener, elementConfig.getUpdatableCustomViewFactory().create(), elementConfig.getTransformer());
        } else {
            throw new IllegalStateException("no configuration found for viewType=" + viewType);
        }
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


    private int viewType(T item) {
        return itemViewTypeMapping.getItemViewTypeFor(item);
    }


    static class BasicViewHolder<T> extends RecyclerView.ViewHolder {
        private final UpdatableCustomView customView;
        private final Transformer transformer;
        private T item;

        BasicViewHolder(final AdapterItemClickListener<T> itemClickListener,
                        final UpdatableCustomView customView, Transformer transformer) {
            super(customView);
            this.customView = customView;
            this.transformer = transformer;

            if (itemClickListener != null) {
                customView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onClick(item, getAdapterPosition(), customView);
                    }
                });
            }
        }

        void bind(T item) {
            this.item = item;
            customView.update(transformer.transform(item));
        }
    }


    public static <T> ViewTypeBuilder<T> builder(ItemViewTypeMapping<T> itemViewTypeMapping) {
        return new Builder<>(itemViewTypeMapping);
    }

    public static class Builder<T> implements ViewTypeBuilder<T> {

        private ItemViewTypeMapping<T> itemViewTypeMapping;
        private Map<Integer, ElementConfig> elements = new HashMap<>();
        private AdapterItemClickListener<T> itemClickListener;

        Builder(ItemViewTypeMapping<T> itemViewTypeMapping) {
            this.itemViewTypeMapping = itemViewTypeMapping;
        }

        Builder() {
        }


        @Override
        public <K> Builder<T> withElement(int itemViewType, Transformer<T, K> transformer, UpdatableCustomViewFactory<? extends K> factory) {
            elements.put(itemViewType, new ElementConfig(factory, transformer));
            return this;
        }

        @Override
        public Builder<T> withElement(int itemViewType, UpdatableCustomViewFactory<? extends T> factory) {
            elements.put(itemViewType, new ElementConfig(factory));
            return this;
        }

        @Override
        public Builder<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        @Override
        public MultiCustomViewAdapter<T> build() {
            if (elements.isEmpty()) {
                throw new IllegalArgumentException("needs to support at least one element");
            }
            MultiCustomViewAdapter<T> adapter = new MultiCustomViewAdapter<T>(elements);
            if (itemClickListener != null) {
                adapter.setItemClickListener(itemClickListener);
            }
            if (itemViewTypeMapping != null) {
                adapter.setItemViewTypeMapping(itemViewTypeMapping);
            }
            return adapter;
        }
    }

    private static class ElementConfig {
        private final UpdatableCustomViewFactory updatableCustomViewFactory;
        private final Transformer transformer;

        ElementConfig(UpdatableCustomViewFactory updatableCustomViewFactory, Transformer transformer) {
            this.updatableCustomViewFactory = updatableCustomViewFactory;
            this.transformer = transformer;
        }

        ElementConfig(UpdatableCustomViewFactory updatableCustomViewFactory) {
            this.updatableCustomViewFactory = updatableCustomViewFactory;
            this.transformer = NoTransform;
        }

        UpdatableCustomViewFactory getUpdatableCustomViewFactory() {
            return updatableCustomViewFactory;
        }

        public Transformer getTransformer() {
            return transformer;
        }
    }

    public interface Transformer<K, L> {
        L transform(K item);
    }

    private static final Transformer NoTransform = new Transformer() {
        @Override
        public Object transform(Object item) {
            return item;
        }
    };


    public interface ViewTypeBuilder<T> {

        <K> Builder<T> withElement(int itemViewType, Transformer<T, K> transformer, UpdatableCustomViewFactory<? extends K> factory);

        Builder<T> withElement(int itemViewType, UpdatableCustomViewFactory<? extends T> factory);

        Builder<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener);

        MultiCustomViewAdapter<T> build();

    }


}