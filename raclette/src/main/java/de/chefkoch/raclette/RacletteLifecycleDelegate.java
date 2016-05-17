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
public class RacletteLifecycleDelegate<V extends ViewModel, B extends ViewDataBinding> {

    private final Raclette raclette;
    private NavigationSupport navigationSupport;

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
        if (!setNavigationSupport(fragment)) {
            navigationSupport = DefaultNavigationSupport.of(fragment.getActivity());
        }
        create(fragment.getActivity(), savedInstanceState, extras);
    }


    public void create(android.app.Fragment fragment, Bundle savedInstanceState, Bundle extras) {
        if (!setNavigationSupport(fragment)) {
            navigationSupport = DefaultNavigationSupport.of(fragment.getActivity());
        }
        create(fragment.getActivity(), savedInstanceState, extras);
    }

    public void create(android.app.DialogFragment dialogFragment, Bundle savedInstanceState, Bundle extras) {
        if (!setNavigationSupport(dialogFragment)) {
            navigationSupport = DefaultNavigationSupport.of(dialogFragment);
        }
        create(dialogFragment.getActivity(), savedInstanceState, extras);
    }

    public void create(android.support.v4.app.DialogFragment dialogFragment, Bundle savedInstanceState, Bundle extras) {
        if (!setNavigationSupport(dialogFragment)) {
            navigationSupport = DefaultNavigationSupport.of(dialogFragment);
        }
        create(dialogFragment.getActivity(), savedInstanceState, extras);
    }

    public void create(Activity activity, Bundle savedInstanceState) {
        create(activity, savedInstanceState, activity.getIntent() != null ? activity.getIntent().getExtras() : null);
    }

    private void create(Context context, Bundle savedInstanceState, Bundle extras) {
        checkViewBindung();
        Bundle params = null;
        if (extras != null) {
            params = ViewModel.Params.from(extras);
        }
        if (checkNavRequestAndIsContinue(context, extras)) {
            init(context, savedInstanceState, params);
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
                //todo log when viewModel was null
            }
        }
        if (viewModel == null) {
            viewModel = raclette.getViewModelManager().createViewModel(viewModelBindingConfig.getViewModelClass());
            viewModel.setNavigationController(raclette.getNavigationController());
            viewModel.injectParams(params);
            viewModel.viewModelCreate(params);
        } else {
            viewModel.injectParams(params);
        }
        this.context = context;
        raclette.getContextManager().setCurrentContext(context);
        setNavigationSupportIfNeeded();
        binding.setVariable(raclette.getViewModelBindingId(), viewModel);
        viewModel.create(params);
    }

    private boolean setNavigationSupport(Object target) {
        if (NavigationSupport.class.isAssignableFrom(target.getClass())) {
            this.navigationSupport = (NavigationSupport) target;
            return true;
        }
        return false;
    }

    private boolean checkNavRequestAndIsContinue(Context context, Bundle extras) {
        NavRequest navRequest = NavRequest.from(extras);
        if (navRequest != null && context instanceof NavRequestInterceptor) {
            return ((NavRequestInterceptor) context).onHandle(navRequest);
        }
        return true;
    }

    private void setNavigationSupportIfNeeded() {
        if (UsesNavigationSupport.class.isAssignableFrom(raclette.getNavigationController().getClass())) {
            ((UsesNavigationSupport) raclette.getNavigationController()).setActiveNavigationSupport(navigationSupport);
        }
    }

    private void clearNavigationSupportIfNeeded() {
        if (UsesNavigationSupport.class.isAssignableFrom(raclette.getNavigationController().getClass())) {
            ((UsesNavigationSupport) raclette.getNavigationController()).clearActiveNavigationSupport();
        }
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
        setNavigationSupportIfNeeded();
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
        clearNavigationSupportIfNeeded();
    }

    private void viewModelDestroy() {
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
