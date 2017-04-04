package de.chefkoch.raclette.android.support;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.android.AdapterItemClickListener;
import de.chefkoch.raclette.android.UpdatableCustomView;

import java.util.*;

/**
 * Not Typesafe. Use {@link MultiCustomViewAdapter} instead.
 * <p>
 * Created by christophwidulle on 16.04.16.
 */
@Deprecated
public class CompositionMultiBindingAdapter extends RecyclerView.Adapter<CompositionMultiBindingAdapter.BasicViewHolder<Object>> {

    private final Map<Integer, UpdatableViewCompositionFactory> elements;
    private AdapterItemClickListener<Object> itemClickListener;

    private List<Object> items = new ArrayList<>();

    private CompositionMultiBindingAdapter(Map<Integer, UpdatableViewCompositionFactory> elements) {
        this.elements = elements;
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

    public void setItemClickListener(AdapterItemClickListener<Object> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    @SuppressWarnings("unchecked assignement")
    public BasicViewHolder<Object> onCreateViewHolder(ViewGroup parent, int viewType) {
        UpdatableViewCompositionFactory factory = elements.get(viewType);
        if (factory != null) {
            UpdatableCustomView updatableViewComposition = factory.create();
            return new BasicViewHolder<>(itemClickListener, updatableViewComposition);

        } else {
            throw new IllegalStateException("found viewType that has not been configured");
        }
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
        private final UpdatableCustomView<T, ? extends UpdatableViewModel<T>, ? extends ViewDataBinding> viewComposition;
        private Object item;

        BasicViewHolder(final AdapterItemClickListener<Object> itemClickListener,
                        final UpdatableCustomView<T, ? extends UpdatableViewModel<T>, ? extends ViewDataBinding> viewComposition) {
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
        UpdatableCustomView<T, ? extends UpdatableViewModel<T>, ?> create();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        Map<Integer, UpdatableViewCompositionFactory> elements = new HashMap<>();
        AdapterItemClickListener<Object> itemClickListener;

        public <T> Builder withElement(Class<T> klass, UpdatableViewCompositionFactory<T> factory) {
            elements.put(klass.hashCode(), factory);
            return this;
        }

        public Builder withItemClickListener(AdapterItemClickListener<Object> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        public CompositionMultiBindingAdapter build() {
            if (elements.isEmpty()) {
                throw new IllegalArgumentException("needs to support at least one element");
            }
            CompositionMultiBindingAdapter adapter = new CompositionMultiBindingAdapter(elements);
            if (itemClickListener != null) {
                adapter.setItemClickListener(itemClickListener);
            }
            return adapter;
        }
    }


}