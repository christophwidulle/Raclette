package de.chefkoch.raclette;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.rx.lifecycle.RxViewModelLifecycle;
import de.chefkoch.raclette.ViewModelLifecycleState;
import de.chefkoch.raclette.rx.lifecycle.ViewModelLifecycleProvider;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by christophwidulle on 14.04.16.
 */
public class RxViewModel extends ViewModel implements ViewModelLifecycleProvider {

    private final BehaviorSubject<ViewModelLifecycleState> lifecycleSubject = BehaviorSubject.create();

    @Override
    public final Observable<ViewModelLifecycleState> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    public final <T> Observable.Transformer<T, T> bindUntilEvent(ViewModelLifecycleState event) {
        return RxViewModelLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    public final <T> Observable.Transformer<T, T> bindToLifecycle() {
        return RxViewModelLifecycle.bind(lifecycleSubject);
    }

    @Override
    void viewModelCreate(Bundle bundle) {
        super.viewModelCreate(bundle);
        lifecycleSubject.onNext(ViewModelLifecycleState.VIEWMODEL_CREATE);

    }

    @Override
    void viewModelDestroy() {
        super.viewModelDestroy();
        lifecycleSubject.onNext(ViewModelLifecycleState.VIEWMODEL_DESTROY);
    }

    @Override
    void create(Bundle params) {
        super.create(params);
        lifecycleSubject.onNext(ViewModelLifecycleState.CREATE);
    }

    @Override
    void start() {
        super.start();
        lifecycleSubject.onNext(ViewModelLifecycleState.START);
    }

    @Override
    void resume() {
        super.resume();
        lifecycleSubject.onNext(ViewModelLifecycleState.RESUME);
    }

    @Override
    void pause() {
        super.pause();
        lifecycleSubject.onNext(ViewModelLifecycleState.PAUSE);
    }

    @Override
    void stop() {
        super.stop();
        lifecycleSubject.onNext(ViewModelLifecycleState.STOP);
    }

    @Override
    void destroy() {
        super.destroy();
        lifecycleSubject.onNext(ViewModelLifecycleState.DESTROY);
    }
}
