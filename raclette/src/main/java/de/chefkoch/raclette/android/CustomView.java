package de.chefkoch.raclette.android;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
        delegate = new RacletteViewLifecycleDelegate<>(getRaclette(), context, getViewModelBindingConfig(), new RacletteViewLifecycleDelegate.OnViewModelCreatedCallback() {
            @Override
            public void onCreated() {
                onViewModelCreated();
            }
        });

        delegate.onCreateViewBinding(LayoutInflater.from(getContext()), this, true);

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

    @Override
    protected Parcelable onSaveInstanceState() {
        return delegate.onSaveInstanceState(super.onSaveInstanceState());

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Parcelable restored = delegate.onRestoreInstanceState(state);
        super.onRestoreInstanceState(restored);
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
