package de.chefkoch.raclette.rx.lifecycle;

import de.chefkoch.raclette.ViewModelLifecycleState;
import rx.Observable;

/**
 * Thx to https://github.com/trello/RxLifecycle
 *
 * Created by christophwidulle on 14.04.16.
 */
public interface ViewCompositionLifecycleProvider {

    Observable<ViewCompositionLifecycleState> lifecycle();

    <T> Observable.Transformer<T, T> bindUntilEvent(ViewCompositionLifecycleState event);

    <T> Observable.Transformer<T, T> bindToLifecycle();
}
