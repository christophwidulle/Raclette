package de.chefkoch.raclette.rx;

import android.support.v7.app.AppCompatActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by christophwidulle on 28.09.15.
 */
public class RacletteRxAppCompatActivity extends AppCompatActivity {


    private CompositeSubscription compositeSubscription;

    protected void managed(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);

    }

    @Override
    protected void onDestroy() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
