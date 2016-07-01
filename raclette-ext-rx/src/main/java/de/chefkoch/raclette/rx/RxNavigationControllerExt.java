package de.chefkoch.raclette.rx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import de.chefkoch.raclette.routing.NavRequest;
import de.chefkoch.raclette.routing.NavigationController;
import de.chefkoch.raclette.routing.NavigationControllerImpl;
import de.chefkoch.raclette.routing.ResultCallback;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

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
        return Observable.create(OnSubscribe.create(navigationController, navRequest));
    }


    public Observable<ForResultReturn> toForResult(Intent intent) {
        return Observable.create(OnSubscribe.create(navigationController, intent));
    }


    final static class OnSubscribe implements Observable.OnSubscribe<ForResultReturn> {

        Intent intent;
        NavRequest navRequest;
        NavigationController navigationController;

        OnSubscribe(NavRequest navRequest, NavigationController navigationController) {
            this.navRequest = navRequest;
            this.navigationController = navigationController;
        }

        OnSubscribe(Intent intent, NavigationController navigationController) {
            this.intent = intent;
            this.navigationController = navigationController;
        }

        public static OnSubscribe create(NavigationController navigationController, Intent intent) {
            return new OnSubscribe(intent, navigationController);
        }

        public static OnSubscribe create(NavigationController navigationController, NavRequest navRequest) {
            return new OnSubscribe(navRequest, navigationController);
        }

        @Override
        public void call(final Subscriber<? super ForResultReturn> subscriber) {

            final ResultCallback resultCallback = new ResultCallback() {
                @Override
                public void onResult(Bundle values) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(ForResultReturn.from(values));
                    }
                }

                @Override
                public void onCancel() {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(ForResultReturn.canceled());
                    }
                }
            };

            if (intent != null) {
                navigationController.toForResult(intent, resultCallback);
            } else if (navRequest != null) {
                navigationController.toForResult(navRequest, resultCallback);
            }


            subscriber.add(new MainThreadSubscription() {
                @Override
                protected void onUnsubscribe() {
                    //view.setOnClickListener(null);
                    navigationController.cancelResult();
                }
            });
        }
    }

    /*
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

    }*/
}
