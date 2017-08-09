package de.chefkoch.raclette.routing;

/**
 * Created by christophwidulle on 24.06.16.
 */
public abstract class ResultCallback {

    public abstract void onResult(RoutingResult result);

    public void onCancel() {
    }
}
