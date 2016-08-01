package de.chefkoch.raclette.rx;

import android.databinding.ObservableField;

import com.jakewharton.rxrelay.BehaviorRelay;
import com.jakewharton.rxrelay.Relay;
import com.jakewharton.rxrelay.ReplayRelay;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by christophwidulle
 */
public abstract class Value<T> extends ObservableField<T> {


    protected abstract Relay<T, T> subject();


    public static <T> Value<T> create() {
        return new BehaviorValue<T>();
    }

    public static <T> Value<T> createReplay() {
        return new ReplayValue<T>();
    }

    @Override
    public void set(T value) {
        super.set(value);
        subject().call(value);
    }

    @Override
    public T get() {
        return super.get();
    }

    public Observable<T> asObservable() {
        return subject().asObservable();
    }


    public Action1<T> asSetAction() {
        return new Action1<T>() {
            @Override
            public void call(T val) {
                set(val);
            }
        };
    }

    public static class BehaviorValue<T> extends Value<T> {

        private final BehaviorRelay<T> subject = BehaviorRelay.create();

        @Override
        protected Relay<T, T> subject() {
            return subject;
        }
    }

    public static class ReplayValue<T> extends Value<T> {

        private final ReplayRelay<T> subject = ReplayRelay.create();

        @Override
        protected Relay<T, T> subject() {
            return subject;
        }
    }
}