package de.chefkoch.raclette.rx;

import android.content.Intent;
import android.os.Bundle;
import de.chefkoch.raclette.routing.NavRequest;
import de.chefkoch.raclette.routing.NavigationController;
import de.chefkoch.raclette.routing.ResultCallback;
import rx.Observable;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by christophwidulle on 25.06.16.
 */
public class RxNavigationControllerExt {

    final NavigationController navigationController;

    public RxNavigationControllerExt(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    public Observable<ForResultReturn> toForResult(NavRequest navRequest) {
        final MyCallbackAdapter adapter = new MyCallbackAdapter();
        navigationController.toForResult(navRequest, adapter);
        return adapter.toObservable();
    }


    public Observable<ForResultReturn> toForResult(Intent intent) {
        final MyCallbackAdapter adapter = new MyCallbackAdapter();
        navigationController.toForResult(intent, adapter);
        return adapter.toObservable();
    }


    private class MyCallbackAdapter extends ResultCallback {

        private final FutureAdapter futureAdapter = new FutureAdapter();

        @Override
        public void onResult(Bundle values) {
            futureAdapter.setValue(ForResultReturn.from(values));
        }

        @Override
        public void onCancel() {
            futureAdapter.setValue(ForResultReturn.canceled());
        }

        Observable<ForResultReturn> toObservable() {
            return Observable.from(futureAdapter);
        }


        class FutureAdapter implements Future<ForResultReturn> {

            private boolean isDone;
            private ForResultReturn value;

            void setValue(ForResultReturn value) {
                synchronized (this) {
                    this.value = value;
                    this.isDone = true;
                    this.notify();
                }
            }


            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return isDone;
            }

            @Override
            public ForResultReturn get() throws InterruptedException {
                synchronized (this) {
                    while (value == null) {
                        this.wait();
                    }
                }
                return value;
            }

            @Override
            public ForResultReturn get(long t, TimeUnit u) throws InterruptedException {
                return get();
            }
        }
    }
}
