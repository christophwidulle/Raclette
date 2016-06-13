package de.chefkoch.raclette.android;

import android.databinding.ViewDataBinding;

/**
 * Created by christophwidulle on 03.06.16.
 */
public interface BindingDecorator<B extends ViewDataBinding> {

    void decorate(B dataBinding);

}
