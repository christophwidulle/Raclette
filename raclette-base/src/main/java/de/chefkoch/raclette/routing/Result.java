package de.chefkoch.raclette.routing;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import de.chefkoch.raclette.util.BundleEquals;

import java.util.Objects;

/**
 * A result für den Rückgabewert von forResult im {@link NavigationController}
 */
public class Result {

    private Bundle extra;
    private Uri data;

    public Result(Bundle extra, Uri data) {
        this.extra = extra;
        this.data = data;
    }

    public static Result of(Bundle extra) {
        return new Result(extra, null);
    }

    public static Result of(Bundle extra, Uri data) {
        return new Result(extra, data);
    }

    public static Result empty() {
        return new Result(null, null);
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
        Result result = (Result) o;
        return BundleEquals.equalsBundles(extra, result.getExtra()) &&
                Objects.equals(data, result.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extra, data);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
