package de.chefkoch.raclette.rx.android;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import de.chefkoch.raclette.Updatable;
import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.android.UpdatableViewComposition;
import de.chefkoch.raclette.android.ViewComposition;
import de.chefkoch.raclette.rx.lifecycle.RxViewCompositionLifecycle;
import de.chefkoch.raclette.rx.lifecycle.ViewCompositionLifecycleState;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by christophwidulle on 22.05.16.
 */
public class RxUpdatableViewComposition<T, V extends UpdatableViewModel<T>, B extends ViewDataBinding>
        extends UpdatableViewComposition<T, V, B> {


    private BehaviorSubject<ViewCompositionLifecycleState> lifecycleSubject;


    public RxUpdatableViewComposition(Context context) {
        super(context);
    }

    public RxUpdatableViewComposition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxUpdatableViewComposition(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void create() {
        lifecycleSubject = BehaviorSubject.create();
        lifecycleSubject.onNext(ViewCompositionLifecycleState.NEW);
        super.create();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        lifecycleSubject.onNext(ViewCompositionLifecycleState.ON_ATTACH);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        lifecycleSubject.onNext(ViewCompositionLifecycleState.ON_DETACH);

    }

    @NonNull
    @CheckResult
    public final Observable<ViewCompositionLifecycleState> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @NonNull
    @CheckResult
    public final <T> Observable.Transformer<T, T> bindUntilEvent(@NonNull ViewCompositionLifecycleState event) {
        return RxViewCompositionLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @CheckResult
    public final <T> Observable.Transformer<T, T> bindToLifecycle() {
        return RxViewCompositionLifecycle.bind(lifecycleSubject);
    }
}
