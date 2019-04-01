package de.chefkoch.raclette.rx;

import com.jakewharton.rxrelay.BehaviorRelay;
import com.jakewharton.rxrelay.PublishRelay;

import com.jakewharton.rxrelay.Relay;

import de.chefkoch.raclette.ViewModelLifecycleState;
import de.chefkoch.raclette.rx.lifecycle.RxViewModelLifecycle;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by christophwidulle on 13.05.16.
 */
public class Command<T> {

    private Relay<T, T> subject;

    Observable<ViewModelLifecycleState> lifecycleSubject;

    public static <T> Command<T> createAndBind(Observable<ViewModelLifecycleState> lifecycleSubject) {
        return new Command<T>(PublishRelay.<T>create(), lifecycleSubject);
    }


    public static <T> Command<T> createBehaviorAndBind(Observable<ViewModelLifecycleState> lifecycleSubject) {
        return new Command<T>(BehaviorRelay.<T>create(), lifecycleSubject);
    }

    public static <T> Command<T> create() {
        return new Command<T>(PublishRelay.<T>create());
    }

    public static <T> Command<T> createBehavior() {
        return new Command<T>(BehaviorRelay.<T>create());
    }

    private Command(Relay<T, T> subject, Observable<ViewModelLifecycleState> lifecycleSubject) {
        this.lifecycleSubject = lifecycleSubject;
        this.subject = subject;
    }

    private Command(Relay<T, T> subject) {
        this.subject = subject;
    }
    
    public void call(T t) {
        subject.call(t);
    }

    public void call() {
        subject.call(null);
    }

    public Action1<T> callAction() {
        return new Action1<T>() {
            @Override
            public void call(T o) {
                Command.this.call(o);
            }
        };
    }

    public Observable<T> asObservable() {
        return asObservable(true);
    }

    public Observable<T> asObservable(boolean autobind) {
        if (lifecycleSubject != null && autobind) {
            return subject
                    .asObservable()
                    .onBackpressureBuffer()
                    .compose(RxViewModelLifecycle.<T>bind(lifecycleSubject));
        } else {
            return subject
                    .asObservable()
                    .onBackpressureBuffer();
        }
    }

    public Subscription subscribeFirst(Subscriber<T> subscriber) {
        if (lifecycleSubject != null) {
            return asObservable(false)
                    .first()
                    .compose(RxViewModelLifecycle.<T>bind(lifecycleSubject))
                    .subscribe(subscriber);
        } else {
            return asObservable().first().subscribe(subscriber);
        }
    }

    public Subscription subscribe(Action1<T> action1) {
        return asObservable().subscribe(action1);
    }

    public Subscription subscribe(Subscriber<T> subscriber) {
        return asObservable().subscribe(subscriber);
    }

}