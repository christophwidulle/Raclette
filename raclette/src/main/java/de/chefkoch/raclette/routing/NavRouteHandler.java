package de.chefkoch.raclette.routing;

import android.os.Bundle;

/**
 * Created by christophwidulle on 20.12.15.
 */
public interface NavRouteHandler {

    void onHandle(String path, Bundle params);

}
