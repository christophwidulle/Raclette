package de.chefkoch.raclette;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.chefkoch.raclette.android.DefaultNavigationSupport;
import de.chefkoch.raclette.routing.NavRequest;
import de.chefkoch.raclette.routing.NavRequestInterceptor;
import de.chefkoch.raclette.routing.NavigationSupport;
import de.chefkoch.raclette.routing.UsesNavigationSupport;


/**
 * Created by christophwidulle on 01.10.15.
 */
public class RacletteViewLifecycleDelegate<V extends ViewModel, B extends ViewDataBinding> {

    private final Raclette raclette;

    protected V viewModel;
    protected B binding;
    protected final ViewModelBindingConfig<V> viewModelBindingConfig;
    private Context context;
    private Bundle params = new Bundle();


    public RacletteViewLifecycleDelegate(Raclette raclette, ViewModelBindingConfig<V> viewModelBindingConfig) {
        this.raclette = raclette;
        this.viewModelBindingConfig = viewModelBindingConfig;
    }

    public View onCreateViewBinding(LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
        binding = DataBindingUtil.inflate(inflater, viewModelBindingConfig.getLayoutResource(), parent, attachToParent);
        return binding.getRoot();
    }

    public void create(View view) {
        checkViewBindung();
        //check for existing
       /* if (savedInstanceState != null) {
            String viewModelId = savedInstanceState.getString(ViewModel.EXTRA_VIEWMODEL_ID);
            if (viewModelId != null) {
                V viewModel = raclette.getViewModelManager().getViewModel(viewModelId);
                if (viewModel != null) {
                    this.viewModel = viewModel;
                }
                //todo log when viewModel was null
            }
        }*/
        if (viewModel == null) {
            viewModel = raclette.getViewModelManager().createViewModel(viewModelBindingConfig.getViewModelClass());
            viewModel.setNavigationController(raclette.getNavigationController());
            viewModel.injectParams(params);
        } else {
            viewModel.injectParams(params);
        }
        binding.setVariable(raclette.getViewModelBindingId(), viewModel);
    }

    public void onAttachedToWindow() {
        viewModel.viewModelCreate(params);
        viewModel.create(params);
        viewModel.start();
        viewModel.resume();
    }

    public void onDetachedFromWindow() {
        viewModel.pause();
        viewModel.stop();
        viewModel.destroy();
        viewModelDestroy();
    }

    private void viewModelDestroy() {
        viewModel.viewModelDestroy();
        raclette.getViewModelManager().delete(viewModel.getId());
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
