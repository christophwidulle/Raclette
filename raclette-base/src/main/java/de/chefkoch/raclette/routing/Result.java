package de.chefkoch.raclette.routing;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

    public Bundle getExtra() {
        return extra;
    }

    public void setExtra(Bundle extra) {
        this.extra = extra;
    }

    @Nullable
    public Uri getData() {
        return data;
    }

    @Nullable
    public void setData(Uri data) {
        this.data = data;
    }
}
