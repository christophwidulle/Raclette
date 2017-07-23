package de.chefkoch.raclette.rx.lifecycle;

import de.chefkoch.raclette.ViewModelLifecycleState;
import rx.Observable;

/**
 * Created by christophwidulle on 22.07.17.
 */
public interface HasBindToLifecycle {


    <T> Observable.Transformer<T, T> bindUntilEvent(ViewModelLifecycleState event);

    <T> Observable.Transformer<T, T> bindToLifecycle();
}
