package de.chefkoch.raclette.rx;

import com.jakewharton.rxrelay.PublishRelay;
import de.chefkoch.raclette.ViewModelLifecycleState;
import de.chefkoch.raclette.rx.lifecycle.RxViewModelLifecycle;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by christophwidulle on 13.05.16.
 */
public class Command<T> {

    public final PublishRelay<T> subject = PublishRelay.create();

    Observable<ViewModelLifecycleState> lifecycleSubject;

    public static <T> Command<T> createAndBind(Observable<ViewModelLifecycleState> lifecycleSubject) {
        return new Command<>(lifecycleSubject);
    }

    public static <T> Command<T> create() {
        return new Command<>();
    }

    Command(Observable<ViewModelLifecycleState> lifecycleSubject) {
        this.lifecycleSubject = lifecycleSubject;
    }

    Command() {
    }

    public void call(T t) {
        subject.call(t);
    }

    public void call() {
        subject.call(null);
    }

    public Observable<T> asObservable() {
        if (lifecycleSubject != null) {
            return subject.asObservable().compose(RxViewModelLifecycle.<T>bind(lifecycleSubject));
        } else {
            return subject.asObservable();
        }
    }

    public Subscription subscribe(Action1<T> action1) {
        return asObservable().subscribe(action1);
    }

}