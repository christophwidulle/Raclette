package de.chefkoch.raclette;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.chefkoch.raclette.routing.NavRequest;
import de.chefkoch.raclette.routing.NavRouteHandler;


/**
 * Created by christophwidulle on 01.10.15.
 */
public class SupportRacletteLifecycleDelegate<V extends ViewModel, B extends ViewDataBinding> extends RacletteLifecycleDelegate<V, B> {


    public SupportRacletteLifecycleDelegate(Raclette raclette, ViewModelBindingConfig<V> viewModelBindingConfig) {
        super(raclette, viewModelBindingConfig);
    }

    public void onDestroy(Fragment fragment) {
        viewModel.onDestroy();
        if (fragment.getActivity().isFinishing()) {
            destroy();
        } else if (fragment.isRemoving()) {
            //todo can be in backstack. check it.
            destroy();
        }
    }
}
