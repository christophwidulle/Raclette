package de.chefkoch.raclette.android;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import de.chefkoch.raclette.routing.NavRequest;
import de.chefkoch.raclette.routing.NavigationSupport;

import java.lang.ref.WeakReference;

/**
 * Created by christophwidulle on 16.05.16.
 */
public abstract class DefaultNavigationSupport implements NavigationSupport {


    public static DefaultNavigationSupport of(android.app.Activity activity) {
        return new ForActivity(activity);
    }

    public static DefaultNavigationSupport of(android.app.DialogFragment dialogFragment) {
        return new ForDialog(dialogFragment);
    }

    public static DefaultNavigationSupport of(android.support.v4.app.DialogFragment dialogFragment) {
        return new ForSupportDialog(dialogFragment);
    }

    @Override
    public boolean onNavigateTo(NavRequest navRequest) {
        return false;
    }


    static class ForSupportDialog extends DefaultNavigationSupport {

        private final WeakReference<android.support.v4.app.DialogFragment> dialogFragmentRef;

        ForSupportDialog(android.support.v4.app.DialogFragment dialogFragment) {
            this.dialogFragmentRef = new WeakReference<>(dialogFragment);
        }

        @Override
        public boolean onBack() {
            if (dialogFragmentRef.get() != null) {
                dialogFragmentRef.get().dismissAllowingStateLoss();
                return true;
            }
            return false;
        }
    }

    static class ForDialog extends DefaultNavigationSupport {

        private final WeakReference<android.app.DialogFragment> dialogFragmentRef;

        ForDialog(android.app.DialogFragment dialogFragment) {
            this.dialogFragmentRef = new WeakReference<>(dialogFragment);
        }

        @Override
        public boolean onBack() {
            if (dialogFragmentRef.get() != null) {
                dialogFragmentRef.get().dismissAllowingStateLoss();
            }
            return true;
        }
    }

    static class ForActivity extends DefaultNavigationSupport {

        private final WeakReference<android.app.Activity> activityRef;

        public ForActivity(android.app.Activity activity) {
            this.activityRef = new WeakReference<Activity>(activity);
        }

        @Override
        public boolean onBack() {
            if (activityRef.get() != null) {
                activityRef.get().finish();
            }
            return true;
        }
    }

}
