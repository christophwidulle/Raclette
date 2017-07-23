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
public class MultiCustomViewAdapter<T> extends RecyclerView.Adapter<MultiCustomViewAdapter.BasicViewHolder<T>> implements AdapterUpdateable<T> {

    private final Map<Integer, ElementConfig> elements;
    private AdapterItemClickListener<T> itemClickListener;

    private ItemViewTypeMapping<T> itemViewTypeMapping;

    private List<T> items = new ArrayList<>();

    private MultiCustomViewAdapter(AdapterConfig<T> adapterConfig) {
        this.elements = adapterConfig.getElements();
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("needs to support at least one element");
        }
        this.itemClickListener = adapterConfig.getItemClickListener();
        this.itemViewTypeMapping = adapterConfig.getItemViewTypeMapping();
    }


    public static <T> Builder<T> builder(ItemViewTypeMapping<T> itemViewTypeMapping) {
        return new Builder<>(itemViewTypeMapping);
    }

    public static <T> MultiCustomViewAdapter<T> create(AdapterConfig<T> adapterConfig) {
        return new MultiCustomViewAdapter<T>(adapterConfig);
    }

    @Override
    public void setAll(Collection<T> items) {
        if (items != null) {
            this.items = new ArrayList<>(items);
            notifyDataSetChanged();
        } else {
            removeAll();
        }
    }

    @Override
    public void addAll(Collection<T> items) {
        if (items != null) {
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
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


    public static class AdapterConfig<T> {

        private ItemViewTypeMapping<T> itemViewTypeMapping;
        private Map<Integer, ElementConfig> elements = new HashMap<>();
        private AdapterItemClickListener<T> itemClickListener;

        public static <T> AdapterConfig<T> create(ItemViewTypeMapping<T> itemViewTypeMapping) {
            return new AdapterConfig<T>(itemViewTypeMapping);
        }

        AdapterConfig(ItemViewTypeMapping<T> itemViewTypeMapping) {
            this.itemViewTypeMapping = itemViewTypeMapping;
        }

        public AdapterConfig<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        public <K> AdapterConfig<T> withElement(int itemViewType, Transformer<T, K> transformer, UpdatableCustomViewFactory<? extends K> factory) {
            elements.put(itemViewType, new ElementConfig(factory, transformer));
            return this;
        }

        public AdapterConfig<T> withElement(int itemViewType, UpdatableCustomViewFactory<? extends T> factory) {
            elements.put(itemViewType, new ElementConfig(factory));
            return this;
        }


        private ItemViewTypeMapping<T> getItemViewTypeMapping() {
            return itemViewTypeMapping;
        }

        private Map<Integer, ElementConfig> getElements() {
            return elements;
        }

        private AdapterItemClickListener<T> getItemClickListener() {
            return itemClickListener;
        }
    }

    public static class Builder<T> {

        AdapterConfig<T> adapterConfig;

        Builder(ItemViewTypeMapping<T> itemViewTypeMapping) {
            this.adapterConfig = new AdapterConfig<T>(itemViewTypeMapping);
        }

        Builder() {
        }

        public <K> Builder<T> withElement(int itemViewType, Transformer<T, K> transformer, UpdatableCustomViewFactory<? extends K> factory) {
            adapterConfig.withElement(itemViewType, transformer, factory);
            return this;
        }

        public Builder<T> withElement(int itemViewType, UpdatableCustomViewFactory<? extends T> factory) {
            adapterConfig.withElement(itemViewType, factory);
            return this;
        }

        public Builder<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener) {
            adapterConfig.withItemClickListener(itemClickListener);
            return this;
        }

        public MultiCustomViewAdapter<T> build() {

            return new MultiCustomViewAdapter<T>(
                    adapterConfig
            );
        }
    }

    private static class ElementConfig {
        private final UpdatableCustomViewFactory updatableCustomViewFactory;
        private final Transformer transformer;

        public ElementConfig(UpdatableCustomViewFactory updatableCustomViewFactory, Transformer transformer) {
            this.updatableCustomViewFactory = updatableCustomViewFactory;
            this.transformer = transformer;
        }

        public ElementConfig(UpdatableCustomViewFactory updatableCustomViewFactory) {
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


}