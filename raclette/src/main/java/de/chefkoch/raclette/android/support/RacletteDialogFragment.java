package de.chefkoch.raclette.android.support;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

    private RacletteLifecycleDelegate<V, B> racletteLifecycleDelegate;

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
        return racletteLifecycleDelegate.onCreateViewBinding(inflater, null, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        racletteLifecycleDelegate.create(this.getActivity(), savedInstanceState, this.getArguments());
        onViewModelCreated();

    }

    protected void onViewModelCreated() {

    }


    @Override
    public void onDestroy() {
        racletteLifecycleDelegate.onDestroy(getActivity());
        super.onDestroy();
    }

    @Override
    public void onPause() {
        racletteLifecycleDelegate.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        racletteLifecycleDelegate.onResume();
    }

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
