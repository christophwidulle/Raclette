package de.chefkoch.raclette;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;


/**
 * Created by christophwidulle on 28.09.15.
 */
public class BaseViewModelActivity<V extends ViewModel, B extends ViewDataBinding> extends BaseRxActivity {

    private ViewModelLifecycleDelegate<V, B> viewModelLifecycleDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelLifecycleDelegate = new ViewModelLifecycleDelegate<>(
                getRaclette(),
                getViewModelBindingConfig());
        viewModelLifecycleDelegate.onCreateViewBinding(this);
        viewModelLifecycleDelegate.create(this, savedInstanceState);
        this.onViewModelCreated();
    }

    protected void onViewModelCreated() {

    }

    @Override
    protected void onDestroy() {
        viewModelLifecycleDelegate.onDestroy(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        viewModelLifecycleDelegate.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModelLifecycleDelegate.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
