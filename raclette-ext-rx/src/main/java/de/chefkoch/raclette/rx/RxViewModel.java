package de.chefkoch.raclette.rx;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.rx.lifecycle.RxViewModelLifecycle;
import de.chefkoch.raclette.rx.lifecycle.ViewModelLivecycleEvent;
import de.chefkoch.raclette.rx.lifecycle.ViewModelLivecycleProvider;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by christophwidulle on 14.04.16.
 */
public class RxViewModel extends ViewModel implements ViewModelLivecycleProvider {

    private final BehaviorSubject<ViewModelLivecycleEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    public final Observable<ViewModelLivecycleEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    public final <T> Observable.Transformer<T, T> bindUntilEvent(ViewModelLivecycleEvent event) {
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
        lifecycleSubject.onNext(ViewModelLivecycleEvent.VIEWMODEL_CREATE);
    }

    @CallSuper
    @Override
    protected void onViewModelDestroyed() {
        super.onViewModelDestroyed();
        lifecycleSubject.onNext(ViewModelLivecycleEvent.VIEWMODEL_DESTROY);
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle params) {
        super.onCreate(params);
        lifecycleSubject.onNext(ViewModelLivecycleEvent.CREATE);
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ViewModelLivecycleEvent.START);
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ViewModelLivecycleEvent.RESUME);
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        lifecycleSubject.onNext(ViewModelLivecycleEvent.PAUSE);
    }

    @CallSuper
    @Override
    protected void onStop() {
        super.onStop();
        lifecycleSubject.onNext(ViewModelLivecycleEvent.STOP);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ViewModelLivecycleEvent.DESTROY);
    }
}
