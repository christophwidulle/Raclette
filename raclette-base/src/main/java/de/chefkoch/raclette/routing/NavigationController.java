package de.chefkoch.raclette.routing;

import android.content.Intent;

/**
 * Created by christophwidulle on 06.12.15.
 */
public interface NavigationController {

    void to(NavRequest navRequest);

    void to(Route.NavRequestBuilder navRequestBuilder);

    void to(Intent intent);

    void back();



}
