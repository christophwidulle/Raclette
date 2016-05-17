package de.chefkoch.raclette.routing;

import android.os.Bundle;

/**
 * Created by christophwidulle on 20.12.15.
 */
public interface NavRequestInterceptor {

    /**
     * @param navRequest
     * @return shouldContinue
     */
    boolean onHandle(NavRequest navRequest);

}
