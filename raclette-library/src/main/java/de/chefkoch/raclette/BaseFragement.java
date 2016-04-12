package de.chefkoch.raclette;

import android.support.v4.app.Fragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by christophwidulle on 28.09.15.
 */
public class BaseFragement extends Fragment {


    private CompositeSubscription compositeSubscription;

    protected void managed(Subscription subscription){
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);

    }

    @Override
    public void onDestroy() {
        if(compositeSubscription != null){
            compositeSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
