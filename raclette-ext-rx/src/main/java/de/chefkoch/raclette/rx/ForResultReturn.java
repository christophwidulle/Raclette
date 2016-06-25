package de.chefkoch.raclette.rx;

import android.os.Bundle;

/**
 * Created by christophwidulle on 25.06.16.
 */
public class ForResultReturn {

    Bundle bundle;

    private ForResultReturn(Bundle bundle) {
        this.bundle = bundle;
    }

    private ForResultReturn() {

    }

    public static ForResultReturn from(Bundle bundle) {
        return new ForResultReturn(bundle);
    }

    public static ForResultReturn canceled() {
        return new ForResultReturn();
    }

    public Bundle getBundle() {
        return bundle;
    }

    public boolean isCanceled() {
        return bundle == null;
    }

}
