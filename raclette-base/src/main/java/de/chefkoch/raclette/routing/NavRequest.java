package de.chefkoch.raclette.routing;

import android.os.Bundle;
import de.chefkoch.raclette.ViewModel;

import java.util.Set;

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

    public NavRequest(String routePath) {
        this.routePath = routePath;
        this.params = null;
    }

    public String getRoutePath() {
        return routePath;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ROUTE_KEY, routePath);
        if (params != null)
            ViewModel.Params.apply(params, bundle);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NavRequest that = (NavRequest) o;

        if (!routePath.equals(that.routePath)) return false;
        return equalsBundles(this.params, ((NavRequest) o).params);

    }

    @Override
    public int hashCode() {
        int result = routePath.hashCode();
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }


    public static boolean equalsBundles(Bundle a, Bundle b) {
        if (a == b) return true;

        Set<String> aks = a.keySet();
        Set<String> bks = b.keySet();

        if (!aks.containsAll(bks)) {
            return false;
        }

        for (String key : aks) {
            Object valueA = a.get(key);
            Object valueB = b.get(key);

            if (valueA instanceof Bundle && valueB instanceof Bundle) {
                if (!equalsBundles((Bundle) valueA, (Bundle) valueB)) {
                    return false;
                }

            } else if (!valueA.equals(valueB)) {
                return false;
            }
        }

        return true;
    }
}
