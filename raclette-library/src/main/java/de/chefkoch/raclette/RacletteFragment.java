package de.chefkoch.raclette;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Christoph on 05.10.2015.
 */
public class RacletteFragment<V extends ViewModel, B extends ViewDataBinding> extends Fragment {

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
        racletteLifecycleDelegate.create(this, savedInstanceState);
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
