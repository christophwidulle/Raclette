package de.chefkoch.raclette.android.support;

import android.content.Intent;
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
public class RacletteAppCompatActivity<V extends ViewModel, B extends ViewDataBinding> extends AppCompatActivity {

    protected RacletteLifecycleDelegate<V, B> racletteLifecycleDelegate;

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
        racletteLifecycleDelegate.onDestroy(this);
        super.onDestroy();
    }

    @CallSuper
    @Override
    protected void onPause() {
        racletteLifecycleDelegate.onPause();
        super.onPause();
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        racletteLifecycleDelegate.onResume();
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        racletteLifecycleDelegate.onStart();
    }

    @CallSuper
    @Override
    protected void onStop() {
        super.onStop();
        racletteLifecycleDelegate.onStop();
    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        racletteLifecycleDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        racletteLifecycleDelegate.onActivityResult(requestCode, resultCode, data);
    }

    public V getViewModel() {
        return racletteLifecycleDelegate.viewModel();
    }

    public B getBinding() {
        return racletteLifecycleDelegate.binding();
    }

    protected ViewModelBindingConfig<V> getViewModelBindingConfig() {
        return ViewModelBindingConfig.fromBindAnnotation(this.getClass());
    }

    protected Raclette getRaclette() {
        return Raclette.get();
    }
}
