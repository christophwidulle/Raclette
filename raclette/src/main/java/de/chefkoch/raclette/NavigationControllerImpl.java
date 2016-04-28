package de.chefkoch.raclette;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import de.chefkoch.raclette.routing.NavRequest;
import de.chefkoch.raclette.routing.NavigationController;
import de.chefkoch.raclette.routing.Route;
import de.chefkoch.raclette.routing.RoutesDict;

/**
 * Created by christophwidulle on 06.12.15.
 */
public class NavigationControllerImpl implements NavigationController {

    private final ContextManager contextManager;

    public NavigationControllerImpl(ContextManager contextManager) {
        this.contextManager = contextManager;
    }

    @Override
    public void to(NavRequest navRequest) {

        Route route = RoutesDict.findBy(navRequest.getRoutePath());
        if (route != null) {
            if (route.isActivityTargetType()) {
                Intent intent = new Intent(contextManager.getCurrentContext(), route.getTargetClass());
                Bundle bundle = navRequest.toBundle();
                intent.putExtras(bundle);
                to(intent);
            } else if (route.getTargetType() == Route.TargetType.SupportDialogFragment) {
                startSupportDialog(route, navRequest);
            } else if (route.getTargetType() == Route.TargetType.DialogFragment) {
                startDialog(route, navRequest);
            }
        } else {
            //todo warning
        }


    }

    @Override
    public void to(Route.NavRequestBuilder navRequestBuilder) {
        to(navRequestBuilder.build());
    }

    @Override
    public void to(Intent intent) {
        Context currentContext = contextManager.getCurrentContext();
        currentContext.startActivity(intent);
    }

    private Activity getCurrentActivity() {
        Context currentContext = contextManager.getCurrentContext();
        if (currentContext instanceof Activity) {
            return (Activity) currentContext;
        } else {
            throw new RacletteException("CurrentContext is not an Activity");
        }
    }

    private android.support.v7.app.AppCompatActivity getCurrentSupportActivity() {
        Context currentContext = contextManager.getCurrentContext();
        if (currentContext instanceof android.support.v7.app.AppCompatActivity) {
            return (android.support.v7.app.AppCompatActivity) currentContext;
        } else {
            throw new RacletteException("CurrentContext is not an AppCompatActivity");
        }
    }


    private void startDialog(Route route, NavRequest navRequest) {
        try {
            Activity currentActivity = getCurrentActivity();

            android.app.DialogFragment fragment = (android.app.DialogFragment) route.getTargetClass().newInstance();
            fragment.setArguments(navRequest.toBundle());
            fragment.show(currentActivity.getFragmentManager(), null);

        } catch (Exception e) {
            throw new RacletteException(e);
        }
    }

    private void startSupportDialog(Route route, NavRequest navRequest) {
        try {
            android.support.v7.app.AppCompatActivity currentActivity = getCurrentSupportActivity();

            android.support.v4.app.DialogFragment fragment = (android.support.v4.app.DialogFragment) route.getTargetClass().newInstance();
            fragment.setArguments(navRequest.toBundle());
            fragment.show(currentActivity.getSupportFragmentManager(), null);

        } catch (Exception e) {
            throw new RacletteException(e);
        }
    }

}
