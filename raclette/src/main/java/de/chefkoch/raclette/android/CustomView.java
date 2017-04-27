package de.chefkoch.raclette.android;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import de.chefkoch.raclette.*;

/**
 * Created by christophwidulle on 21.05.16.
 */
public class CustomView<V extends ViewModel, B extends ViewDataBinding> extends FrameLayout {

    RacletteViewLifecycleDelegate<V, B> delegate;

    public CustomView(Context context) {
        super(context);
        create(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        create(context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        create(context);
    }


    protected void create(Context context) {
        delegate = new RacletteViewLifecycleDelegate<>(getRaclette(), findActivity(), getViewModelBindingConfig(), new RacletteViewLifecycleDelegate.OnViewModelCreatedCallback() {
            @Override
            public void onCreated() {
                onViewModelCreated();
            }
        });

        delegate.onCreateViewBinding(LayoutInflater.from(getContext()), this, true);

    }

    public Activity findActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }


    protected void onViewModelCreated() {

    }

    public void setParams(Bundle params) {
        delegate.setParams(params);
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
        delegate.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    protected Raclette getRaclette() {
        return Raclette.get();
    }

    public V viewModel() {
        return delegate.viewModel();
    }

    public B binding() {
        return delegate.binding();
    }

}
