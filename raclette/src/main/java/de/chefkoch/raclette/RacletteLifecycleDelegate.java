package de.chefkoch.raclette;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.chefkoch.raclette.android.DefaultNavigationSupport;
import de.chefkoch.raclette.routing.*;


/**
 * Created by christophwidulle on 01.10.15.
 */
public class RacletteLifecycleDelegate<V extends ViewModel, B extends ViewDataBinding> {

    private final Raclette raclette;
    private NavigationSupport navigationSupport;

    private NavRequest navRequest;

    protected V viewModel;
    protected B binding;
    protected final ViewModelBindingConfig<V> viewModelBindingConfig;
    private Context context;

    public RacletteLifecycleDelegate(Raclette raclette, ViewModelBindingConfig<V> viewModelBindingConfig) {
        this.raclette = raclette;
        this.viewModelBindingConfig = viewModelBindingConfig;
    }

    public View onCreateViewBinding(Activity activity) {
        binding = DataBindingUtil.setContentView(activity, viewModelBindingConfig.getLayoutResource());
        return binding.getRoot();
    }

    public View onCreateViewBinding(LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
        binding = DataBindingUtil.inflate(inflater, viewModelBindingConfig.getLayoutResource(), parent, attachToParent);
        return binding.getRoot();
    }

    public void create(android.support.v4.app.Fragment fragment, Bundle savedInstanceState, Bundle extras) {
        if (!checkAndSetNavigationSupport(fragment)) {
            navigationSupport = DefaultNavigationSupport.of(fragment.getActivity());
        }
        create(fragment, fragment.getActivity(), savedInstanceState, extras);
    }


    public void create(android.app.Fragment fragment, Bundle savedInstanceState, Bundle extras) {
        if (!checkAndSetNavigationSupport(fragment)) {
            navigationSupport = DefaultNavigationSupport.of(fragment.getActivity());
        }
        create(fragment, fragment.getActivity(), savedInstanceState, extras);
    }

    public void create(android.app.DialogFragment dialogFragment, Bundle savedInstanceState, Bundle extras) {
        if (!checkAndSetNavigationSupport(dialogFragment)) {
            navigationSupport = DefaultNavigationSupport.of(dialogFragment);
        }
        create(dialogFragment, dialogFragment.getActivity(), savedInstanceState, extras);
    }

    public void create(android.support.v4.app.DialogFragment dialogFragment, Bundle savedInstanceState, Bundle extras) {
        if (!checkAndSetNavigationSupport(dialogFragment)) {
            navigationSupport = DefaultNavigationSupport.of(dialogFragment);
        }
        create(dialogFragment, dialogFragment.getActivity(), savedInstanceState, extras);
    }

    public void create(Activity activity, Bundle savedInstanceState) {
        create(activity, activity, savedInstanceState, activity.getIntent() != null ? activity.getIntent().getExtras() : null);
    }

    private void create(Object parent, Context context, Bundle savedInstanceState, Bundle extras) {
        checkViewBindung();
        if (checkNavRequest(parent, extras)) {
            init(context, savedInstanceState, navRequest.getParams());
        }
    }

    private void init(Context context, Bundle savedInstanceState, Bundle params) {
        //check for existing
        if (savedInstanceState != null) {
            String viewModelId = savedInstanceState.getString(ViewModel.EXTRA_VIEWMODEL_ID);
            if (viewModelId != null) {
                V viewModel = raclette.getViewModelManager().getViewModel(viewModelId);
                if (viewModel != null) {
                    this.viewModel = viewModel;
                }
            }
        }
        raclette.getContextManager().setCurrentContext(context);
        this.context = context;
        if (viewModel == null) {
            viewModel = raclette.getViewModelManager().createViewModel(viewModelBindingConfig.getViewModelClass());
            viewModel.setNavigationController(raclette.createNavigationController());
            getNavigationControllerImpl().setContext(context);
            checkNavResultCode();
            viewModel.injectParams(params);
            viewModel.viewModelCreate(params);
        } else {
            getNavigationControllerImpl().setContext(context);
        }

        setNavigationSupportIfNeeded();
        binding.setVariable(raclette.getViewModelBindingId(), viewModel);
        viewModel.create(params);
    }

    private NavigationController getNavigationController() {
        return viewModel.navigate();
    }

    private NavigationControllerImpl getNavigationControllerImpl() {
        return (NavigationControllerImpl) viewModel.navigate();
    }

    private boolean checkAndSetNavigationSupport(Object target) {
        if (NavigationSupport.class.isAssignableFrom(target.getClass())) {
            this.navigationSupport = (NavigationSupport) target;
            return true;
        }
        return false;
    }

    private boolean checkNavRequest(Object parentView, Bundle extras) {
        navRequest = NavRequest.from(extras);
        if (parentView instanceof NavRequestInterceptor) {
            return ((NavRequestInterceptor) parentView).onHandle(navRequest);
        }
        return true;
    }


    private void checkNavResultCode() {
        if (navRequest != null) {
            if (navRequest.hasRequltCode()) {
                getNavigationControllerImpl().setCurrentRequestCode(navRequest.getResultCode());
            }
        }
    }

    private void setNavigationSupportIfNeeded() {
        if (navigationSupport != null) {
            getNavigationControllerImpl().setActiveNavigationSupport(navigationSupport);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getNavigationControllerImpl().onActivityResult(requestCode, resultCode, data);
    }

    public void onDestroy(Activity activity) {
        destroy();
        if (activity.isFinishing()) {
            viewModelDestroy();
        }
    }

    public void onDestroy(android.app.Fragment fragment) {
        destroy();
        if (fragment.getActivity().isFinishing()) {
            viewModelDestroy();
        } else if (fragment.isRemoving()) {
            //todo can be in backstack. check it.
            viewModelDestroy();
        }
    }

    public void onDestroy(android.support.v4.app.Fragment fragment) {
        destroy();
        if (fragment.getActivity().isFinishing()) {
            viewModelDestroy();
        } else if (fragment.isRemoving()) {
            //todo can be in backstack. check it.
            viewModelDestroy();
        }
    }

    public void onStart() {
        raclette.getContextManager().setCurrentContext(context);
        viewModel.start();
    }

    public void onPause() {
        viewModel.pause();
    }

    public void onResume() {
        viewModel.resume();
    }

    public void onStop() {
        viewModel.stop();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ViewModel.EXTRA_VIEWMODEL_ID, viewModel.getId());
    }

    private void destroy() {
        viewModel.destroy();
    }

    private void viewModelDestroy() {
        getNavigationControllerImpl().onDestroy();
        raclette.getViewModelManager().delete(viewModel.getId());
        viewModel.viewModelDestroy();
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
