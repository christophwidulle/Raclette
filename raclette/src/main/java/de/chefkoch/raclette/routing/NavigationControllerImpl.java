package de.chefkoch.raclette.routing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import de.chefkoch.raclette.ContextManager;
import de.chefkoch.raclette.RacletteException;

import java.lang.ref.WeakReference;

/**
 * Created by christophwidulle on 06.12.15.
 */
public class NavigationControllerImpl implements NavigationController {

    private NavigationSupport navigationSupport;
    private ForResultSupport forResultSupport = new ForResultSupport();
    private WeakReference<Context> currentContext;

    public NavigationControllerImpl() {

    }

    @Override
    public void to(NavRequest navRequest) {

        if (!navigateToBySupport(navRequest)) {
            Route route = RoutesDict.findBy(navRequest.getRoutePath());
            if (route != null) {
                if (route.isActivityTargetType()) {
                    Intent intent = new Intent(getCurrentContext(), route.getTargetClass());
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
    }

    @Override
    public void toForResult(NavRequest navRequest, ResultCallback resultCallback) {
        int resultCode = forResultSupport.register(resultCallback);
        navRequest.setResultCode(resultCode);
        to(navRequest);
    }

    @Override
    public void to(Intent intent) {
        Context currentContext = getCurrentContext();
        currentContext.startActivity(intent);
    }

    @Override
    public void toForResult(Intent intent, ResultCallback resultCallback) {
        int resultCode = forResultSupport.register(resultCallback);
        Context currentContext = getCurrentContext();
        if (currentContext instanceof Activity) {
            ((Activity) currentContext).startActivityForResult(intent, resultCode);
        }
    }

    @Override
    public void back() {
        if (!navigateBackBySupport()) {
            getCurrentActivity().finish();
        }
    }

    @Override
    public void returnResult(Bundle values) {
        this.forResultSupport.returnResult(values);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.forResultSupport.onActivityResult(requestCode, resultCode, data);
    }

    public void setCurrentResultCode(Integer currentResultCode) {
        this.forResultSupport.setCurrentResultCode(currentResultCode);
    }

    public void onDestroy() {
        this.forResultSupport.onDestroy();
    }

    private boolean navigateToBySupport(NavRequest navRequest) {
        return navigationSupport != null && (navigationSupport.onNavigateTo(navRequest));
    }

    private boolean navigateBackBySupport() {
        return navigationSupport != null && navigationSupport.onBack();
    }

    private Activity getCurrentActivity() {
        Context currentContext = getCurrentContext();
        if (currentContext instanceof Activity) {
            return (Activity) currentContext;
        } else {
            throw new RacletteException("CurrentContext is not an Activity");
        }
    }

    private android.support.v7.app.AppCompatActivity getCurrentSupportActivity() {
        Context currentContext = getCurrentContext();
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

    private Context getCurrentContext() {
        if (currentContext != null && currentContext.get() != null) {
            return currentContext.get();
        } else {
            throw new RacletteException("Using a dead Context, this should never happen.");
        }
    }

    public void setActiveNavigationSupport(NavigationSupport navigationSupport) {
        this.navigationSupport = navigationSupport;
    }

    public void setContext(Context context) {
        this.currentContext = new WeakReference<Context>(context);
    }

}
