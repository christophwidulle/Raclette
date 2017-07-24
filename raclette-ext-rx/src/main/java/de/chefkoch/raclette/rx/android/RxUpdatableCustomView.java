package de.chefkoch.raclette.rx.android;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.android.UpdatableCustomView;
import de.chefkoch.raclette.rx.lifecycle.HasBindToLifecycle;
import de.chefkoch.raclette.rx.lifecycle.RxViewCompositionLifecycle;
import de.chefkoch.raclette.rx.lifecycle.CustomViewLifecycleState;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by christophwidulle on 22.05.16.
 */
public class RxUpdatableCustomView<T, V extends UpdatableViewModel<T>, B extends ViewDataBinding>
        extends UpdatableCustomView<T, V, B> implements HasBindToLifecycle {


    private BehaviorSubject<CustomViewLifecycleState> lifecycleSubject;


    public RxUpdatableCustomView(Context context) {
        super(context);
    }

    public RxUpdatableCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxUpdatableCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void create(Context context) {
        lifecycleSubject = BehaviorSubject.create();
        lifecycleSubject.onNext(CustomViewLifecycleState.NEW);
        super.create(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        lifecycleSubject.onNext(CustomViewLifecycleState.ON_ATTACH);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        lifecycleSubject.onNext(CustomViewLifecycleState.ON_DETACH);
        lifecycleSubject.onNext(CustomViewLifecycleState.NEW);

    }

    @NonNull
    @CheckResult
    public final Observable<CustomViewLifecycleState> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @NonNull
    @CheckResult
    public final <T> Observable.Transformer<T, T> bindUntilEvent(@NonNull CustomViewLifecycleState event) {
        return RxViewCompositionLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @CheckResult
    public final <T> Observable.Transformer<T, T> bindToLifecycle() {
        return RxViewCompositionLifecycle.bind(lifecycleSubject);
    }
}
