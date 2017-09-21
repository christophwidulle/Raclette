package de.chefkoch.raclette;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private final OnViewModelCreatedCallback callback;

    private String viewModelId;
    private boolean stateSaved;

    public RacletteViewLifecycleDelegate(Raclette raclette, Context context, ViewModelBindingConfig<V> viewModelBindingConfig, OnViewModelCreatedCallback callback) {
        this.raclette = raclette;
        this.viewModelBindingConfig = viewModelBindingConfig;
        this.context = validateContext(context);
        this.callback = callback;

        checkAndSetNavigationSupport(context);
    }

    public View onCreateViewBinding(LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
        binding = DataBindingUtil.inflate(inflater, viewModelBindingConfig.getLayoutResource(), parent, attachToParent);
        return binding.getRoot();
    }

    private Context validateContext(Context context) {
        if (context instanceof Activity) {
            return context;
        }
        if (context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("given context is not an Activity");
        }
        return context;
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
        createViewModel();
        viewModel.create(params);
        viewModel.start();
        viewModel.resume();
    }

    public void onDetachedFromWindow() {
        viewModel.pause();
        viewModel.stop();
        viewModel.destroy();
        if (!stateSaved) {
            destroyViewModel();
        }
    }


    public void createViewModel() {
        checkViewBindung();
        if (viewModelId != null) {
            V viewModel = raclette.getViewModelManager().getViewModel(viewModelId);
            if (viewModel != null) {
                this.viewModel = viewModel;
            }
        }
        if (viewModel == null) {
            viewModel = raclette.getViewModelManager().createViewModel(viewModelBindingConfig.getViewModelClass());
            viewModel.setNavigationController(raclette.createNavigationController());
            getNavigationControllerImpl().setContext(context);
            injectParams();
            viewModel.viewModelCreate(params);
        } else {
            getNavigationControllerImpl().setContext(context);
        }
        setNavigationSupportIfNeeded();
        binding.setVariable(raclette.getViewModelBindingId(), viewModel);
        callback.onCreated();
    }

    private void destroyViewModel() {
        viewModel.viewModelDestroy();
        raclette.getViewModelManager().delete(viewModel.getId());
        viewModel = null;
    }


    private void checkViewBindung() {
        if (binding == null) throw new RacletteException("call onCreateViewBinding(...) before.");
    }

    public static interface OnViewModelCreatedCallback {
        void onCreated();
    }


    public V viewModel() {
        return viewModel;
    }

    public B binding() {
        return binding;
    }

    private static class SavedState extends View.BaseSavedState {
        // include own data members here
        String viewModelId;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            viewModelId = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(viewModelId);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    public Parcelable onSaveInstanceState(Parcelable parcelable) {
        if (viewModel() != null) {
            SavedState state = new SavedState(parcelable);
            // set data members here
            state.viewModelId = viewModel().getId();
            stateSaved = true;
            return state;
        } else {
            return parcelable;
        }
    }

    public Parcelable onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState state = (SavedState) parcelable;
            this.viewModelId = state.viewModelId;
            return state.getSuperState();
        } else
            return parcelable;
    }

}
