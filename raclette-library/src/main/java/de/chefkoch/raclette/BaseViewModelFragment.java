package de.chefkoch.raclette;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Christoph on 05.10.2015.
 */
public class BaseViewModelFragment<V extends ViewModel, B extends ViewDataBinding> extends BaseFragement {

    private final String TAG = this.getClass().getSimpleName();

    private ViewModelLifecycleDelegate<V, B> viewModelLifecycleDelegate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModelLifecycleDelegate = new ViewModelLifecycleDelegate<>(
                getRaclette(),
                getViewModelBindingConfig());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return viewModelLifecycleDelegate.onCreateViewBinding(inflater, null, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModelLifecycleDelegate.create(this, savedInstanceState);
        onViewModelCreated();
        Log.d(TAG, "onActivityCreated: with ViewModel=" + getViewModel());

    }

    protected void onViewModelCreated() {

    }


    @Override
    public void onDestroy() {
        viewModelLifecycleDelegate.onDestroy(getActivity());
        super.onDestroy();
    }

    @Override
    public void onPause() {
        viewModelLifecycleDelegate.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModelLifecycleDelegate.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        viewModelLifecycleDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    public V getViewModel() {
        return viewModelLifecycleDelegate.getViewModel();
    }

    public B getBinding() {
        return viewModelLifecycleDelegate.getBinding();
    }

    protected ViewModelBindingConfig<V> getViewModelBindingConfig() {
        return ViewModelBindingConfig.of(this.getClass());
    }

    protected Raclette getRaclette() {
        return Raclette.get();
    }
}
