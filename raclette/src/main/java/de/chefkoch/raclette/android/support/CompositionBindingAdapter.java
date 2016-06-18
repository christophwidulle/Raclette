package de.chefkoch.raclette.android.support;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.android.AdapterItemClickListener;
import de.chefkoch.raclette.android.UpdatableViewComposition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by christophwidulle on 16.04.16.
 */
public class CompositionBindingAdapter<T> extends RecyclerView.Adapter<CompositionBindingAdapter.BasicViewHolder<T>> {

    private AdapterItemClickListener<T> itemClickListener;
    private final UpdatableViewCompositionFactory<T> factory;

    private final List<T> items = new ArrayList<>();

    private CompositionBindingAdapter(UpdatableViewCompositionFactory<T> factory) {
        this.factory = factory;
    }

    public void addAll(Collection<T> items) {
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
        UpdatableViewComposition<T, ? extends UpdatableViewModel<T>, ? extends ViewDataBinding> viewComposition = factory.create();
        return new BasicViewHolder<T>(itemClickListener, viewComposition);
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

    public static <T> CompositionBindingAdapter<T> create(final UpdatableViewCompositionFactory<T> factory) {
        return new CompositionBindingAdapter<>(factory);
    }


}