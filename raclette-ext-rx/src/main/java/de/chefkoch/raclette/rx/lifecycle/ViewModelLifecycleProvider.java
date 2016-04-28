package de.chefkoch.raclette.rx.lifecycle;

import rx.Observable;

/**
 * Thx to https://github.com/trello/RxLifecycle
 *
 * Created by christophwidulle on 14.04.16.
 */
public interface ViewModelLifecycleProvider {

    Observable<ViewModelLifecycleEvent> lifecycle();

    <T> Observable.Transformer<T, T> bindUntilEvent(ViewModelLifecycleEvent event);

    <T> Observable.Transformer<T, T> bindToLifecycle();
}
