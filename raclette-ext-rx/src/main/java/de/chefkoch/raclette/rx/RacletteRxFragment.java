package de.chefkoch.raclette.rx;

import de.chefkoch.raclette.RacletteFragment;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Christoph on 05.10.2015.
 */
public class RacletteRxFragment extends RacletteFragment {

    private CompositeSubscription compositeSubscription;

    protected void managed(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);

    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
