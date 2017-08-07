package de.chefkoch.raclette.android;

import de.chefkoch.raclette.routing.NavRequest;
import de.chefkoch.raclette.routing.NavigationSupport;

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

        android.support.v4.app.DialogFragment dialogFragment;

        ForSupportDialog(android.support.v4.app.DialogFragment dialogFragment) {
            this.dialogFragment = dialogFragment;
        }

        @Override
        public boolean onBack() {
            this.dialogFragment.dismissAllowingStateLoss();
            return true;
        }
    }

    static class ForDialog extends DefaultNavigationSupport {

        android.app.DialogFragment dialogFragment;

        ForDialog(android.app.DialogFragment dialogFragment) {
            this.dialogFragment = dialogFragment;
        }

        @Override
        public boolean onBack() {
            this.dialogFragment.dismissAllowingStateLoss();
            return true;
        }
    }

    static class ForActivity extends DefaultNavigationSupport {

        android.app.Activity activity;

        public ForActivity(android.app.Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean onBack() {
            activity.finish();
            return true;
        }
    }

}
