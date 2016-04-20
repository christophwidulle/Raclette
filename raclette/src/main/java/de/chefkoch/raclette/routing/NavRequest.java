package de.chefkoch.raclette.routing;

import android.os.Bundle;

import de.chefkoch.raclette.Raclette;
import de.chefkoch.raclette.ViewModel;

/**
 * Created by christophwidulle on 07.12.15.
 */
public class NavRequest {

    public static final String ROUTE_KEY = Raclette.BUNDLE_PREFIX + "ROUTE_KEY";

    private final String route;
    private final Bundle params;

    public NavRequest(String route, Bundle params) {
        this.route = route;
        this.params = params;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ROUTE_KEY, route);
        bundle.putBundle(ViewModel.Params.EXTRA_KEY, params);
        return bundle;
    }

    public static NavRequest from(Bundle bundle) {
        final Bundle params = bundle.getBundle(ViewModel.Params.EXTRA_KEY);
        final String route = bundle.getString(ROUTE_KEY);
        return new NavRequest(route, params);
    }

}
