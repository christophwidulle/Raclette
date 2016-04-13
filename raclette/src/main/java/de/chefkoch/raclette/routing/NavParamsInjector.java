package de.chefkoch.raclette.routing;

/**
 * Created by christophwidulle on 07.12.15.
 */
public interface NavParamsInjector<B, V> {

    void inject(B params, V viewModel);
}
