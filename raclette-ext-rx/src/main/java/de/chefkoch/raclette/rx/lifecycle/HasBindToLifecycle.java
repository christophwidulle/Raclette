package de.chefkoch.raclette.rx.lifecycle;

import android.support.annotation.NonNull;
import com.trello.rxlifecycle.ActivityEvent;
import de.chefkoch.raclette.ViewModelLifecycleState;
import rx.Observable;

/**
 * Created by christophwidulle on 22.07.17.
 */
public interface HasBindToLifecycle {

    <T> Observable.Transformer<T, T> bindToLifecycle();

    <T> Observable.Transformer<T, T> bindUntilDestroy();

}
