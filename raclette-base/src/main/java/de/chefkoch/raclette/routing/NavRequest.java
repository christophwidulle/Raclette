package de.chefkoch.raclette.routing;

import android.os.Bundle;
import de.chefkoch.raclette.ViewModel;

/**
 * Created by christophwidulle on 07.12.15.
 */
public class NavRequest {

    public static final String ROUTE_KEY = "##RACLETTE_ROUTE_KEY";

    private final String routePath;
    private final Bundle params;

    public NavRequest(String routePath, Bundle params) {
        this.routePath = routePath;
        this.params = params;
    }

    public String getRoutePath() {
        return routePath;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ROUTE_KEY, routePath);
        bundle.putBundle(ViewModel.Params.EXTRA_KEY, params);
        return bundle;
    }

    public static NavRequest from(Bundle bundle) {
        if (bundle == null) return null;
        final String route = bundle.getString(ROUTE_KEY);
        if (route != null) {
            final Bundle params = bundle.getBundle(ViewModel.Params.EXTRA_KEY);
            return new NavRequest(route, params);
        } else
            return null;
    }
}
