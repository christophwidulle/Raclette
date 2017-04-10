package de.chefkoch.raclette.android.support;

import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.android.UpdatableCustomView;

/**
 * Created by christophwidulle on 17.06.16.
 */

public interface UpdatableCustomViewFactory<T> {
    UpdatableCustomView<T, ? extends UpdatableViewModel<? extends T>, ?> create();
}