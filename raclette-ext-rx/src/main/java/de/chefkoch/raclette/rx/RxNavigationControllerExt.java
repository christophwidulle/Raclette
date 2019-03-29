package de.chefkoch.raclette.rx;

import android.content.Intent;

import de.chefkoch.raclette.routing.*;
import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by christophwidulle on 25.06.16.
 */
public class RxNavigationControllerExt {

    final NavigationController navigationController;

    public RxNavigationControllerExt(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    public Observable<ForResultReturn> toForResult(NavRequest navRequest) {
        return Observable.create(OnSubscribeToResult.create(navigationController, navRequest));
    }


    public Observable<ForResultReturn> toForResult(Intent intent) {
        return Observable.create(OnSubscribeToResult.create(navigationController, intent));
    }

    public Observable<Boolean> to(final Intent intent) {
        return Observable.create(new Action1<Emitter<Boolean>>() {
            @Override
            public void call(Emitter<Boolean> emitter) {

                try {
                    navigationController.to(intent);
                    emitter.onNext(true);
                    emitter.onCompleted();
                } catch (Exception e) {
                    emitter.onError(e);
                }


            }
        }, Emitter.BackpressureMode.BUFFER);
    }


    private final static class OnSubscribeTo implements Observable.OnSubscribe<ForResultReturn> {

        Intent intent;
        NavRequest navRequest;
        NavigationController navigationController;

        OnSubscribeTo(NavRequest navRequest, NavigationController navigationController) {
            this.navRequest = navRequest;
            this.navigationController = navigationController;

        }

        OnSubscribeTo(Intent intent, NavigationController navigationController) {
            this.intent = intent;
            this.navigationController = navigationController;
        }

        public static OnSubscribeToResult create(NavigationController navigationController, Intent intent) {
            return new OnSubscribeToResult(intent, navigationController);
        }

        public static OnSubscribeToResult create(NavigationController navigationController, NavRequest navRequest) {
            return new OnSubscribeToResult(navRequest, navigationController);
        }

        @Override
        public void call(final Subscriber<? super ForResultReturn> subscriber) {

            final ResultCallback resultCallback = new ResultCallback() {
                @Override
                public void onResult(ResultValue result) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(ForResultReturn.from(result));
                        subscriber.onCompleted();
                    }
                }

                @Override
                public void onCancel() {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(ForResultReturn.canceled());
                        subscriber.onCompleted();
                    }
                }
            };

            if (intent != null) {
                navigationController.to(intent);


                navigationController.toForResult(intent, resultCallback);
            } else if (navRequest != null) {
                navigationController.toForResult(navRequest, resultCallback);
            }

        }
    }


    private final static class OnSubscribeToResult implements Observable.OnSubscribe<ForResultReturn> {

        Intent intent;
        NavRequest navRequest;
        NavigationController navigationController;

        OnSubscribeToResult(NavRequest navRequest, NavigationController navigationController) {
            this.navRequest = navRequest;
            this.navigationController = navigationController;

        }

        OnSubscribeToResult(Intent intent, NavigationController navigationController) {
            this.intent = intent;
            this.navigationController = navigationController;
        }

        public static OnSubscribeToResult create(NavigationController navigationController, Intent intent) {
            return new OnSubscribeToResult(intent, navigationController);
        }

        public static OnSubscribeToResult create(NavigationController navigationController, NavRequest navRequest) {
            return new OnSubscribeToResult(navRequest, navigationController);
        }

        @Override
        public void call(final Subscriber<? super ForResultReturn> subscriber) {

            final ResultCallback resultCallback = new ResultCallback() {
                @Override
                public void onResult(ResultValue result) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(ForResultReturn.from(result));
                        subscriber.onCompleted();
                    }
                }

                @Override
                public void onCancel() {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(ForResultReturn.canceled());
                        subscriber.onCompleted();
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
                    navigationController.cancelResult();
                }
            });
        }
    }
}
