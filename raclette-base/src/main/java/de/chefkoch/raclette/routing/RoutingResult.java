package de.chefkoch.raclette.routing;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import de.chefkoch.raclette.util.BundleEquals;

/**
 * A result für den Rückgabewert von forResult im {@link NavigationController}
 */
public class RoutingResult {

    private Bundle extra;
    private Uri data;

    public RoutingResult(Bundle extra, Uri data) {
        this.extra = extra;
        this.data = data;
    }

    public static RoutingResult of(Bundle extra) {
        return new RoutingResult(extra, null);
    }

    public static RoutingResult of(Bundle extra, Uri data) {
        return new RoutingResult(extra, data);
    }

    public static RoutingResult empty() {
        return new RoutingResult(null, null);
    }

    public Bundle getExtra() {
        return extra;
    }

    public void setExtra(Bundle extra) {
        this.extra = extra;
    }

    public boolean isEmpty() {
        return extra == null && data == null;
    }

    @Nullable
    public Uri getData() {
        return data;
    }

    @Nullable
    public void setData(Uri data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoutingResult result = (RoutingResult) o;

        if (extra != null ? !BundleEquals.equalsBundles(extra, result.getExtra()) : result.extra != null) return false;
        return data != null ? data.equals(result.data) : result.data == null;
    }

    @Override
    public int hashCode() {
        int result = extra != null ? extra.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
