package de.chefkoch.raclette.routing;

import android.os.Bundle;
import de.chefkoch.raclette.ViewModel;

import java.util.Set;

/**
 * Created by christophwidulle on 07.12.15.
 */
public class NavRequest {

    public static final String ROUTE_KEY = "##RACLETTE_ROUTE_KEY";
    public static final String EXTERNAL_ROUTE_VALUE = "##RACLETTE_EXTERNAL_CALL";
    public static final String FOR_RESULT_CODE__KEY = "##RACLETTE_FOR_RESULT_KEY";

    private final String routePath;
    private Bundle params;
    private int resultCode = -1;

    public NavRequest(String routePath, Bundle params) {
        this.routePath = routePath;
        this.params = params == null ? new Bundle() : params;
    }

    public NavRequest(String routePath) {
        this.routePath = routePath;
        this.params = new Bundle();
    }

    public Bundle getParams() {
        return params;
    }

    public String getRoutePath() {
        return routePath;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ROUTE_KEY, routePath);
        if (resultCode > 0) {
            bundle.putInt(FOR_RESULT_CODE__KEY, resultCode);
        }
        if (params != null)
            ViewModel.Params.apply(params, bundle);
        return bundle;
    }

    public static NavRequest from(Bundle bundle) {
        if (bundle == null) return external();
        final String route = bundle.getString(ROUTE_KEY);
        int resultCode = bundle.getInt(FOR_RESULT_CODE__KEY, -1);
        if (route != null) {
            final Bundle params = bundle.getBundle(ViewModel.Params.EXTRA_KEY);
            NavRequest navRequest = new NavRequest(route, params);
            if (resultCode > 0) {
                navRequest.setResultCode(resultCode);
            }
            return navRequest;
        } else
            return null;
    }

    public boolean isExternal() {
        return EXTERNAL_ROUTE_VALUE.equals(routePath);
    }

    private static NavRequest external() {
        return new NavRequest(EXTERNAL_ROUTE_VALUE, new Bundle());
    }

    void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public boolean hasRequltCode() {
        return resultCode > 0;
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

    @Override
    public String toString() {
        return "NavRequest{" +
                "routePath='" + routePath + '\'' +
                ", params=" + params +
                ", resultCode=" + resultCode +
                '}';
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

            if (valueA == null && valueB == null) {
                return true;
            }

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
