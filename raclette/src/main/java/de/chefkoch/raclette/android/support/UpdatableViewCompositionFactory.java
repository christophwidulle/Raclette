package de.chefkoch.raclette.android.support;

import android.databinding.ViewDataBinding;
import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.android.UpdatableViewComposition;

/**
 * Created by christophwidulle on 17.06.16.
 */
public interface UpdatableViewCompositionFactory<T, B extends ViewDataBinding> {
    UpdatableViewComposition<T, ? extends UpdatableViewModel<T>, B> create();
}
