package de.chefkoch.raclette.util;

import android.os.Bundle;

import java.util.Set;

/**
 * Created by markus.seifarth on 24.07.17.
 */
public class BundleEquals {

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
