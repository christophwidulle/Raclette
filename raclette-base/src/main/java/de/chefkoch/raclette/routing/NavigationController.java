package de.chefkoch.raclette.routing;

import android.content.Intent;

/**
 * Created by christophwidulle on 06.12.15.
 */
public interface NavigationController {

    public void to(NavRequest navRequest);

    public void to(Route.NavRequestBuilder navRequestBuilder);

    public void to(Intent intent);

}
