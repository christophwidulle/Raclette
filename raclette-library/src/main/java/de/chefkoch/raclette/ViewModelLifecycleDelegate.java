package de.chefkoch.raclette;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by christophwidulle on 01.10.15.
 */
public class ViewModelLifecycleDelegate<V extends ViewModel, B extends ViewDataBinding> {

    private final Raclette raclette;

    public V viewModel;
    public B binding;
    private final ViewModelBindingConfig<V> viewModelBindingConfig;

    public ViewModelLifecycleDelegate(Raclette raclette, ViewModelBindingConfig<V> viewModelBindingConfig) {
        this.raclette = raclette;
        this.viewModelBindingConfig = viewModelBindingConfig;
    }

    protected View onCreateViewBinding(Activity activity) {
        binding = DataBindingUtil.setContentView(activity, viewModelBindingConfig.getLayoutResource());
        return binding.getRoot();
    }

    protected View onCreateViewBinding(LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
        binding = DataBindingUtil.inflate(inflater, viewModelBindingConfig.getLayoutResource(), parent, attachToParent);
        return binding.getRoot();
    }

    protected void create(Activity activity, Bundle savedInstanceState) {
        checkViewBindung();
        Bundle params = null;
        Intent intent = activity.getIntent();
        if (intent != null) {
            params = ViewModel.Params.from(intent.getExtras());
        }
        create(activity, savedInstanceState, params);
    }

    protected void create(Fragment fragment, Bundle savedInstanceState) {
        checkViewBindung();
        Bundle params = null;
        Bundle arguments = fragment.getArguments();
        if (arguments != null) {
            params = ViewModel.Params.from(arguments);
        }
        create(fragment.getActivity(), savedInstanceState, params);
    }

    private void create(Context context, Bundle savedInstanceState, Bundle params) {
        //check for existing
        if (savedInstanceState != null) {
            String viewModelId = savedInstanceState.getString(ViewModel.EXTRA_ID);
            if (viewModelId != null) {
                V viewModel = raclette.getViewModelManager().getViewModel(viewModelId);
                if (viewModel == null) {
                    throw new RacletteException("ViewModel not found but should be alive.");
                } else {
                    this.viewModel = viewModel;
                    this.viewModel.setContext(context);

                }
            }
        }
        if (viewModel == null) {
            V viewModel = raclette.getViewModelManager().createViewModel(viewModelBindingConfig.getViewModelClass());
            this.viewModel = viewModel;
            viewModel.setContext(context);
            viewModel.onCreate(params);
        }
        binding.setVariable(raclette.getViewModelBindingId(), viewModel);
    }

    protected void onDestroy(Activity activity) {
        if (activity.isFinishing()) {
            viewModel.onDestroy();
            raclette.getViewModelManager().delete(viewModel.getId());
        }
    }

    protected void onPause() {
        viewModel.onPause();
    }

    protected void onResume() {
        viewModel.onResume();
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ViewModel.EXTRA_ID, viewModel.getId());
    }

    private void checkViewBindung() {
        if (binding == null) throw new RacletteException("call onCreateViewBinding(...) before.");
    }

    public V getViewModel() {
        return viewModel;
    }

    public B getBinding() {
        return binding;
    }


}
