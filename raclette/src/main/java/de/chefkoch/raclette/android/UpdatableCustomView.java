package de.chefkoch.raclette.android;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.util.AttributeSet;

import de.chefkoch.raclette.Updatable;
import de.chefkoch.raclette.UpdatableViewModel;

/**
 * Created by christophwidulle on 22.05.16.
 */
public class UpdatableCustomView<T, V extends UpdatableViewModel<T>, B extends ViewDataBinding>
        extends CustomView<V, B>
        implements Updatable<T> {

    private T updatedItem;
    private boolean updateOnAttach = false;

    public UpdatableCustomView(Context context) {
        super(context);
    }

    public UpdatableCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UpdatableCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void update(T item) {
        updatedItem = item;

        if (viewModel() != null) {
            viewModel().update(item);
        } else {
            updateOnAttach = true;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (updatedItem != null && updateOnAttach) {
            viewModel().update(updatedItem);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        updateOnAttach = true;
    }
}
