package de.chefkoch.raclette.android.support;

import android.databinding.ObservableField;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.android.AdapterViewModel;

/**
 * Created by christophwidulle on 16.04.16.
 */
public class SimpleAdapterViewModel<T> extends ViewModel implements AdapterViewModel<T> {

    ObservableField<T> item;

    @Override
    public void update(T item) {
        this.item = new ObservableField<>(item);
    }
}
