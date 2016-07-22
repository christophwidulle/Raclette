package de.chefkoch.raclette.rx;

import android.databinding.ObservableField;
import com.jakewharton.rxrelay.BehaviorRelay;
import de.chefkoch.raclette.ViewModelLifecycleState;
import de.chefkoch.raclette.rx.lifecycle.RxViewModelLifecycle;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by christophwidulle
 */
public class Value<T> extends ObservableField<T> {

    public final BehaviorRelay<T> subject = BehaviorRelay.create();


    public static <T> Value<T> create() {
        return new Value<>();
    }

    @Override
    public void set(T value) {
        super.set(value);
        subject.call(value);
    }
/*
    public Observable<T> observeAndBind(Observable<ActivityEvent> lifecycle)

    //todo  still a draft
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
*/
}