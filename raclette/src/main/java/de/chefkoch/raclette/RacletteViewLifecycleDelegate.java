package de.chefkoch.raclette;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by christophwidulle on 01.10.15.
 */
public class RacletteViewLifecycleDelegate<V extends ViewModel, B extends ViewDataBinding> {

    private final Raclette raclette;

    protected V viewModel;
    protected B binding;
    private final ViewModelBindingConfig<V> viewModelBindingConfig;
    private Bundle params;

    public RacletteViewLifecycleDelegate(Raclette raclette, ViewModelBindingConfig<V> viewModelBindingConfig) {
        this.raclette = raclette;
        this.viewModelBindingConfig = viewModelBindingConfig;
    }

    public View onCreateViewBinding(LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
        binding = DataBindingUtil.inflate(inflater, viewModelBindingConfig.getLayoutResource(), parent, attachToParent);
        return binding.getRoot();
    }

    public void create() {
        checkViewBindung();
        if (viewModel == null) {
            viewModel = raclette.getViewModelManager().createViewModel(viewModelBindingConfig.getViewModelClass());
            viewModel.setNavigationController(raclette.getNavigationController());

            injectParams();
            binding.setVariable(raclette.getViewModelBindingId(), viewModel);
            viewModel.viewModelCreate(params);
        }
    }

    public void injectParams() {
        if (params != null && viewModel != null) {
            viewModel.injectParams(params);
        }
    }

    public void setParams(Bundle params) {
        this.params = params;
        injectParams();
    }

    public void onAttachedToWindow() {
        create();
        viewModel.create(params);
        viewModel.start();
        viewModel.resume();
    }

    public void onDetachedFromWindow() {
        viewModel.pause();
        viewModel.stop();
        viewModel.destroy();
        destroyViewModel();
    }

    private void destroyViewModel() {
        viewModel.viewModelDestroy();
        raclette.getViewModelManager().delete(viewModel.getId());
        viewModel = null;
    }

    private void checkViewBindung() {
        if (binding == null) throw new RacletteException("call onCreateViewBinding(...) before.");
    }

    public V viewModel() {
        return viewModel;
    }

    public B binding() {
        return binding;
    }


}
