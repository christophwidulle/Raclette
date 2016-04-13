package de.chefkoch.raclette;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by christophwidulle on 28.09.15.
 */
public class RacletteActivity<V extends ViewModel, B extends ViewDataBinding> extends AppCompatActivity {

    private RacletteLifecycleDelegate<V, B> racletteLifecycleDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        racletteLifecycleDelegate = new RacletteLifecycleDelegate<>(
                getRaclette(),
                getViewModelBindingConfig());
        racletteLifecycleDelegate.onCreateViewBinding(this);
        racletteLifecycleDelegate.create(this, savedInstanceState);
        this.onViewModelCreated();
    }

    protected void onViewModelCreated() {

    }

    @Override
    protected void onDestroy() {
        racletteLifecycleDelegate.onDestroy(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        racletteLifecycleDelegate.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        racletteLifecycleDelegate.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        racletteLifecycleDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    public V getViewModel() {
        return racletteLifecycleDelegate.getViewModel();
    }

    public B getBinding() {
        return racletteLifecycleDelegate.getBinding();
    }

    protected ViewModelBindingConfig<V> getViewModelBindingConfig() {
        return ViewModelBindingConfig.fromBindAnnotation(this.getClass());
    }

    protected Raclette getRaclette() {
        return Raclette.get();
    }
}
