package de.chefkoch.raclette.android.support;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.android.AdapterItemClickListener;
import de.chefkoch.raclette.android.UpdatableViewComposition;

/**
 * Created by christophwidulle on 16.04.16.
 */
public class CompositionMultiViewBindingAdapter<T> extends RecyclerView.Adapter<CompositionMultiViewBindingAdapter.BasicViewHolder<T>> {

    private final Map<Integer, UpdatableViewCompositionFactory> elements;
    private AdapterItemClickListener<T> itemClickListener;

    private ItemViewTypeMapping<T> itemViewTypeMapping = new DefaultItemViewTypeMapping<>();

    private List<T> items = new ArrayList<>();

    private CompositionMultiViewBindingAdapter(Map<Integer, UpdatableViewCompositionFactory> elements) {
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
        UpdatableViewCompositionFactory factory = elements.get(viewType);
        if (factory != null) {
            UpdatableViewComposition updatableViewComposition = factory.create();
            return new BasicViewHolder<>(itemClickListener, updatableViewComposition);

        } else {
            throw new IllegalStateException("found viewType that has not been configured");
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
        private final UpdatableViewComposition<T, ? extends UpdatableViewModel<T>, ? extends ViewDataBinding> viewComposition;
        private T item;

        BasicViewHolder(final AdapterItemClickListener<T> itemClickListener,
                        final UpdatableViewComposition<T, ? extends UpdatableViewModel<T>, ? extends ViewDataBinding> viewComposition) {
            super(viewComposition);
            this.viewComposition = viewComposition;

            if (itemClickListener != null) {
                viewComposition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onClick(item, getAdapterPosition(), viewComposition);
                    }
                });
            }
        }

        void bind(T item) {
            this.item = item;
            viewComposition.update(item);
        }
    }


    public interface UpdatableViewCompositionFactory<T> {
        UpdatableViewComposition<T, ? extends UpdatableViewModel<T>, ?> create();
    }

    public static <T> ByClassBuilder<T> builder() {
        return new Builder<>();
    }

    public static <T> ViewTypeBuilder<T> builder(ItemViewTypeMapping<T> itemViewTypeMapping) {
        return new Builder<>(itemViewTypeMapping);
    }

    public static class Builder<T> implements ByClassBuilder<T>, ViewTypeBuilder<T> {

        private ItemViewTypeMapping<T> itemViewTypeMapping;
        private Map<Integer, UpdatableViewCompositionFactory> elements = new HashMap<>();
        private AdapterItemClickListener<T> itemClickListener;


        Builder(ItemViewTypeMapping<T> itemViewTypeMapping) {
            this.itemViewTypeMapping = itemViewTypeMapping;
        }

        Builder() {
        }

        @Override
        public Builder<T> withElement(Class<T> klass, UpdatableViewCompositionFactory<T> factory) {
            elements.put(klass.hashCode(), factory);
            return this;
        }

        @Override
        public Builder<T> withElement(int itemViewType, UpdatableViewCompositionFactory<T> factory) {
            elements.put(itemViewType, factory);
            return this;
        }

        @Override
        public Builder<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        @Override
        public CompositionMultiViewBindingAdapter<T> build() {
            if (elements.isEmpty()) {
                throw new IllegalArgumentException("needs to support at least one element");
            }
            CompositionMultiViewBindingAdapter<T> adapter = new CompositionMultiViewBindingAdapter<T>(elements);
            if (itemClickListener != null) {
                adapter.setItemClickListener(itemClickListener);
            }
            if (itemViewTypeMapping != null) {
                adapter.setItemViewTypeMapping(itemViewTypeMapping);
            }
            return adapter;
        }
    }

    public interface ByClassBuilder<T> {
        Builder<T> withElement(Class<T> klass, UpdatableViewCompositionFactory<T> factory);

        Builder<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener);

        CompositionMultiViewBindingAdapter<T> build();

    }

    public interface ViewTypeBuilder<T> {
        Builder<T> withElement(int itemViewType, UpdatableViewCompositionFactory<T> factory);

        Builder<T> withItemClickListener(AdapterItemClickListener<T> itemClickListener);

        CompositionMultiViewBindingAdapter<T> build();

    }


}