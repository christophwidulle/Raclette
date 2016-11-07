package de.chefkoch.raclette.rx.android;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.android.ViewComposition;
import de.chefkoch.raclette.rx.lifecycle.RxViewCompositionLifecycle;
import de.chefkoch.raclette.rx.lifecycle.ViewCompositionLifecycleState;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by christophwidulle on 07.11.16.
 */

public class RxViewComposition<V extends ViewModel, B extends ViewDataBinding> extends ViewComposition<V, B> {


    private BehaviorSubject<ViewCompositionLifecycleState> lifecycleSubject;


    public RxViewComposition(Context context) {
        super(context);
    }

    public RxViewComposition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxViewComposition(Context context, AttributeSet attrs, int defStyleAttr) {
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
