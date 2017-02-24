package de.chefkoch.raclette.rx;


import android.databinding.ObservableField;

import com.jakewharton.rxrelay.ReplayRelay;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

/**
 * Created by christophwidulle
 */
public abstract class Value<T> extends ObservableField<T> {


    public static <T> Value<T> create() {
        return new DefaultValue<T>(true);
    }

    public static <T> Value<T> create(T val) {
        return new DefaultValue<T>(val, true);
    }

    public static <T> Value<T> createPublish() {
        return new DefaultValue<T>(false);
    }

    public static <T> Value<T> createPublish(T val) {
        return new DefaultValue<T>(val, false);
    }

    public static <T> Value<T> createReplay() {
        return new ReplayValue<T>();
    }

    public static <T> Value<T> createReplay(T val) {
        return new ReplayValue<T>(val);
    }


    Value() {
    }

    Value(T value) {
        super(value);
    }


    public Action1<T> asSetAction() {
        return new Action1<T>() {
            @Override
            public void call(T val) {
                set(val);
            }
        };
    }

    public Subscriber<T> subscribe() {
        return new Subscriber<T>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(T t) {
                set(t);
            }
        };
    }

    public abstract Observable<T> asObservable();


    static class DefaultValue<T> extends Value<T> {

        private final boolean emitCurrent;


        public DefaultValue(boolean emitCurrent) {
            this.emitCurrent = emitCurrent;
        }

        DefaultValue(T value, boolean emitCurrent) {
            super(value);
            this.emitCurrent = emitCurrent;
        }

        public Observable<T> asObservable() {
            return toObservable(this, emitCurrent);
        }

    }

    static class ReplayValue<T> extends Value<T> {


        private final ReplayRelay<T> subject = ReplayRelay.create();

        ReplayValue(T val) {
            super(val);
            set(val);
        }

        ReplayValue() {
        }


        @Override
        public void set(T value) {
            super.set(value);
            subject.call(value);
        }

        public Observable<T> asObservable() {
            return subject.asObservable();
        }

    }


    public static <T> Observable<T> toObservable(final ObservableField<T> observableField, final boolean emitCurrent) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                if (emitCurrent && observableField.get() != null) {
                    subscriber.onNext(observableField.get());
                }
                final OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable dataBindingObservable, int propertyId) {
                        if (dataBindingObservable == observableField) {
                            subscriber.onNext(observableField.get());
                        }
                    }
                };

                observableField.addOnPropertyChangedCallback(callback);

                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        observableField.removeOnPropertyChangedCallback(callback);
                    }
                }));
            }
        });
    }
}