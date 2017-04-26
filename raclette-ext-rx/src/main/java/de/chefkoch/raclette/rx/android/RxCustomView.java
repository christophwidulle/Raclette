package de.chefkoch.raclette.rx.android;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.android.CustomView;
import de.chefkoch.raclette.rx.lifecycle.RxViewCompositionLifecycle;
import de.chefkoch.raclette.rx.lifecycle.ViewCompositionLifecycleState;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by christophwidulle on 07.11.16.
 */

public class RxCustomView<V extends ViewModel, B extends ViewDataBinding> extends CustomView<V, B> {


    private BehaviorSubject<ViewCompositionLifecycleState> lifecycleSubject;


    public RxCustomView(Context context) {
        super(context);
    }

    public RxCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void create(Context context) {
        lifecycleSubject = BehaviorSubject.create();
        lifecycleSubject.onNext(ViewCompositionLifecycleState.NEW);
        super.create(context);
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
        lifecycleSubject.onNext(ViewCompositionLifecycleState.NEW);
    }

    @Override
    protected void onViewModelCreated() {

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
