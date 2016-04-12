package de.chefkoch.raclette;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by christophwidulle on 01.10.15.
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Bind {

    Class<? extends ViewModel> viewModel();

    int layoutResource();

}
