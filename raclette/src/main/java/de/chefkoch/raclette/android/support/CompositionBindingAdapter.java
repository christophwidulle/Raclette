package de.chefkoch.raclette.android.support;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.android.AdapterItemClickListener;
import de.chefkoch.raclette.Updatable;
import de.chefkoch.raclette.android.UpdatableViewComposition;
import de.chefkoch.raclette.android.ViewComposition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by christophwidulle on 16.04.16.
 */
public abstract class CompositionBindingAdapter<T> extends RecyclerView.Adapter<CompositionBindingAdapter.BasicViewHolder<T>> {

    private AdapterItemClickListener<T> itemClickListener;

    final private List<T> items = new ArrayList<>();

    private CompositionBindingAdapter() {

    }

    protected abstract UpdatableViewComposition<T, UpdatableViewModel<T>, ? extends ViewDataBinding> createView();

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
        UpdatableViewComposition<T, UpdatableViewModel<T>, ? extends ViewDataBinding> viewComposition = createView();
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
        private final UpdatableViewComposition<T, UpdatableViewModel<T>, ? extends ViewDataBinding> viewComposition;
        private T item;

        BasicViewHolder(final AdapterItemClickListener<T> itemClickListener,
                        final UpdatableViewComposition<T, UpdatableViewModel<T>, ? extends ViewDataBinding> viewComposition) {
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
}