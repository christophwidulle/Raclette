package de.chefkoch.raclette.routing;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import de.chefkoch.raclette.util.BundleEquals;

/**
 * Return value for forResult in {@link NavigationController}
 */
public class ResultValue {

    private Bundle extra;
    private Uri data;

    public ResultValue(Bundle extra, Uri data) {
        this.extra = extra;
        this.data = data;
    }

    public static ResultValue of(Bundle extra) {
        return new ResultValue(extra, null);
    }

    public static ResultValue of(Bundle extra, Uri data) {
        return new ResultValue(extra, data);
    }

    public static ResultValue empty() {
        return new ResultValue(null, null);
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

        ResultValue result = (ResultValue) o;

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
