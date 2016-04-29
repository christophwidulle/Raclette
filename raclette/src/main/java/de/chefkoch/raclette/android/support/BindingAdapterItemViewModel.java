package de.chefkoch.raclette.android.support;

import android.databinding.ObservableField;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.android.AdapterItemViewModel;

/**
 * Created by christophwidulle on 16.04.16.
 */
public class BindingAdapterItemViewModel<T> extends ViewModel implements AdapterItemViewModel<T> {

    ObservableField<T> item;

    @Override
    public void update(T item) {
        this.item = new ObservableField<>(item);
    }
}
