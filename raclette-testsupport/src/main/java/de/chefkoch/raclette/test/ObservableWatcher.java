package de.chefkoch.raclette.test;

import android.databinding.Observable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by christophwidulle on 09.05.16.
 */
public class ObservableWatcher {

    final CountDownLatch latch;
    final Observable.OnPropertyChangedCallback onPropertyChangedCallback;
    final Observable observable;

    ObservableWatcher(CountDownLatch latch, Observable observable, Observable.OnPropertyChangedCallback onPropertyChangedCallback) {
        this.latch = latch;
        this.observable = observable;
        this.onPropertyChangedCallback = onPropertyChangedCallback;
    }

    public static ObservableWatcher hasChanged(Observable observable) {
        final CountDownLatch latch = new CountDownLatch(1);
        final Observable.OnPropertyChangedCallback onPropertyChangedCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                latch.countDown();
            }
        };
        observable.addOnPropertyChangedCallback(onPropertyChangedCallback);
        return new ObservableWatcher(latch, observable, onPropertyChangedCallback);
    }

    public boolean await() {
        return await(1, TimeUnit.MINUTES);
    }

    public boolean await(long timeout, TimeUnit unit) {
        try {
            latch.await(timeout, unit);
            if (latch.getCount() == 0) return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {
            observable.removeOnPropertyChangedCallback(onPropertyChangedCallback);
        }
        return false;
    }
}
