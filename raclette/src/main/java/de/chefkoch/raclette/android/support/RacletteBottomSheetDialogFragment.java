package de.chefkoch.raclette.android.support;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
public class RacletteBottomSheetDialogFragment<V extends ViewModel, B extends ViewDataBinding> extends BottomSheetDialogFragment {

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
        View view = racletteLifecycleDelegate.onCreateViewBinding(inflater, container, false);
        this.onBindingCreated();
        return view;
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

    protected void onBindingCreated() {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return super.show(transaction, tag);
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
