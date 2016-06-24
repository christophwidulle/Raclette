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
import de.chefkoch.raclette.routing.NavigationController;
import de.chefkoch.raclette.routing.NavigationControllerImpl;
import de.chefkoch.raclette.routing.NavigationSupport;


/**
 * Created by christophwidulle on 01.10.15.
 */
public class RacletteViewLifecycleDelegate<V extends ViewModel, B extends ViewDataBinding> {

    private final Raclette raclette;
    private NavigationSupport navigationSupport;

    protected V viewModel;
    protected B binding;
    private final ViewModelBindingConfig<V> viewModelBindingConfig;
    private Bundle params;
    private Context context;

    public RacletteViewLifecycleDelegate(Raclette raclette, ViewModelBindingConfig<V> viewModelBindingConfig) {
        this.raclette = raclette;
        this.viewModelBindingConfig = viewModelBindingConfig;
        this.context = raclette.getContextManager().getCurrentContext();

        checkAndSetNavigationSupport(context);
    }

    public View onCreateViewBinding(LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
        binding = DataBindingUtil.inflate(inflater, viewModelBindingConfig.getLayoutResource(), parent, attachToParent);
        return binding.getRoot();
    }

    public void create() {
        checkViewBindung();
        if (viewModel == null) {
            viewModel = raclette.getViewModelManager().createViewModel(viewModelBindingConfig.getViewModelClass());
            viewModel.setNavigationController(raclette.createNavigationController());
            getNavigationControllerImpl().setContext(context);
            setNavigationSupportIfNeeded();
            injectParams();
            binding.setVariable(raclette.getViewModelBindingId(), viewModel);
            viewModel.viewModelCreate(params);
        }
    }

    private boolean checkAndSetNavigationSupport(Object target) {
        if (NavigationSupport.class.isAssignableFrom(target.getClass())) {
            this.navigationSupport = (NavigationSupport) target;
            return true;
        }
        return false;
    }

    private void setNavigationSupportIfNeeded() {
        if (navigationSupport != null) {
            getNavigationControllerImpl().setActiveNavigationSupport(navigationSupport);
        }
    }

    private NavigationController getNavigationController() {
        return viewModel.navigate();
    }

    private NavigationControllerImpl getNavigationControllerImpl() {
        return (NavigationControllerImpl) viewModel.navigate();
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
