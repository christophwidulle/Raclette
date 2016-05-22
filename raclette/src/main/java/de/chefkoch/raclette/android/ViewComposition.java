package de.chefkoch.raclette.android;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import de.chefkoch.raclette.*;

/**
 * Created by christophwidulle on 21.05.16.
 */
public class ViewComposition<V extends ViewModel, B extends ViewDataBinding> extends FrameLayout {

    RacletteViewLifecycleDelegate<V, B> delegate;

    public ViewComposition(Context context) {
        super(context);
        create();
    }

    public ViewComposition(Context context, AttributeSet attrs) {
        super(context, attrs);
        create();
    }

    public ViewComposition(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        create();
    }

    protected void create() {
        delegate = new RacletteViewLifecycleDelegate<>(Raclette.get(), getViewModelBindingConfig());
        delegate.onCreateViewBinding(LayoutInflater.from(getContext()), this, true);
        delegate.create(this);

    }

    protected ViewModelBindingConfig<V> getViewModelBindingConfig() {
        return ViewModelBindingConfig.fromBindAnnotation(this.getClass());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        delegate.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        delegate.onDetachedFromWindow();
    }

    public V viewModel() {
        return delegate.viewModel();
    }

    public B binding() {
        return delegate.binding();
    }

}
