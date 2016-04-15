package de.chefkoch.raclette.android;

import android.app.Activity;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import de.chefkoch.raclette.Raclette;
import de.chefkoch.raclette.RacletteLifecycleDelegate;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.ViewModelBindingConfig;


/**
 * Created by christophwidulle on 28.09.15.
 */
public class RacletteActivity<V extends ViewModel, B extends ViewDataBinding> extends Activity {

    private RacletteLifecycleDelegate<V, B> racletteLifecycleDelegate;

    @CallSuper
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

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        racletteLifecycleDelegate.onDestroy(this);
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        racletteLifecycleDelegate.onPause();
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        racletteLifecycleDelegate.onResume();
    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        racletteLifecycleDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    public V viewModel() {
        return racletteLifecycleDelegate.viewModel();
    }

    public B binding() {
        return racletteLifecycleDelegate.binding();
    }

    protected ViewModelBindingConfig<V> getViewModelBindingConfig() {
        return ViewModelBindingConfig.fromBindAnnotation(this.getClass());
    }

    protected Raclette getRaclette() {
        return Raclette.get();
    }
}
