package de.chefkoch.raclette.android;

import android.app.DialogFragment;
import android.app.Fragment;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.chefkoch.raclette.Raclette;
import de.chefkoch.raclette.RacletteLifecycleDelegate;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.ViewModelBindingConfig;


/**
 * Created by Christoph on 05.10.2015.
 */
public class RacletteDialogFragment<V extends ViewModel, B extends ViewDataBinding> extends DialogFragment {

    protected RacletteLifecycleDelegate<V, B> racletteLifecycleDelegate;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        racletteLifecycleDelegate = new RacletteLifecycleDelegate<>(
                getRaclette(),
                getViewModelBindingConfig());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return racletteLifecycleDelegate.onCreateViewBinding(inflater, container, false);
    }

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        racletteLifecycleDelegate.create(this, savedInstanceState, this.getArguments());
        onViewModelCreated();
    }

    protected void onViewModelCreated() {

    }

    @CallSuper
    @Override
    public void onDestroy() {
        racletteLifecycleDelegate.onDestroy(this);
        super.onDestroy();
    }

    @CallSuper
    @Override
    public void onPause() {
        racletteLifecycleDelegate.onPause();
        super.onPause();
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        racletteLifecycleDelegate.onResume();
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        racletteLifecycleDelegate.onStart();
    }

    @CallSuper
    @Override
    public void onStop() {
        super.onStop();
        racletteLifecycleDelegate.onStop();
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle outState) {
        racletteLifecycleDelegate.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
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
