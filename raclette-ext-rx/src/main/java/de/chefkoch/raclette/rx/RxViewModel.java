package de.chefkoch.raclette.rx;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.rx.lifecycle.RxViewModelLifecycle;
import de.chefkoch.raclette.rx.lifecycle.ViewModelLifecycleEvent;
import de.chefkoch.raclette.rx.lifecycle.ViewModelLifecycleProvider;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by christophwidulle on 14.04.16.
 */
public class RxViewModel extends ViewModel implements ViewModelLifecycleProvider {

    private final BehaviorSubject<ViewModelLifecycleEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    public final Observable<ViewModelLifecycleEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    public final <T> Observable.Transformer<T, T> bindUntilEvent(ViewModelLifecycleEvent event) {
        return RxViewModelLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    public final <T> Observable.Transformer<T, T> bindToLifecycle() {
        return RxViewModelLifecycle.bind(lifecycleSubject);
    }

    @CallSuper
    @Override
    protected void onViewModelCreated(Bundle viewModelParams) {
        super.onViewModelCreated(viewModelParams);
        lifecycleSubject.onNext(ViewModelLifecycleEvent.VIEWMODEL_CREATE);
    }

    @CallSuper
    @Override
    protected void onViewModelDestroyed() {
        super.onViewModelDestroyed();
        lifecycleSubject.onNext(ViewModelLifecycleEvent.VIEWMODEL_DESTROY);
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle params) {
        super.onCreate(params);
        lifecycleSubject.onNext(ViewModelLifecycleEvent.CREATE);
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ViewModelLifecycleEvent.START);
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ViewModelLifecycleEvent.RESUME);
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        lifecycleSubject.onNext(ViewModelLifecycleEvent.PAUSE);
    }

    @CallSuper
    @Override
    protected void onStop() {
        super.onStop();
        lifecycleSubject.onNext(ViewModelLifecycleEvent.STOP);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ViewModelLifecycleEvent.DESTROY);
    }
}
