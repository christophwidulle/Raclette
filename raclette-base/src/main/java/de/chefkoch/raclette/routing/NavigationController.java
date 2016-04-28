package de.chefkoch.raclette.routing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import de.chefkoch.raclette.ContextManager;
import de.chefkoch.raclette.Raclette;
import de.chefkoch.raclette.ViewModel;

/**
 * Created by christophwidulle on 06.12.15.
 */
public class NavigationController {


    private final ContextManager contextManager;

    public NavigationController(ContextManager contextManager) {
        this.contextManager = contextManager;
    }

    public void to(NavRequest navRequest) {

        Route route = RoutesDict.findBy(navRequest.getRoutePath());
        if (route != null) {
            Intent intent = new Intent(contextManager.getCurrentContext(), route.getTargetClass());
            Bundle bundle = navRequest.toBundle();
            intent.putExtras(bundle);
            to(intent);
        } else {
            //todo warning
        }


    }

    public void to(Route.NavRequestBuilder navRequestBuilder) {
        to(navRequestBuilder.build());
    }


    public void to(Intent intent) {
        Context currentContext = contextManager.getCurrentContext();
        currentContext.startActivity(intent);

    }


}
