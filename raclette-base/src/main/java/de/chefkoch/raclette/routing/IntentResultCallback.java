package de.chefkoch.raclette.routing;

import android.content.Intent;
import android.os.Bundle;

/**
 * A {@link ResultCallback} returns the hole {@link Intent}
 */
public abstract class IntentResultCallback extends ResultCallback {

    public abstract void onResult(Intent intent);

    @Override
    public void onResult(Bundle values) {
        // not implemented, just to hide the default functionality
    }
}
