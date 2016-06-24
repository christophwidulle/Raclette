package de.chefkoch.raclette.routing;

import android.os.Bundle;

/**
 * Created by christophwidulle on 24.06.16.
 */
public abstract class ResultCallback {

    public abstract void onResult(Bundle values);

    public void onCancel() {
    }


}
