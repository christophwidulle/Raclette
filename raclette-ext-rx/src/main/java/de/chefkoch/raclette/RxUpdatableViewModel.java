package de.chefkoch.raclette;

import de.chefkoch.raclette.routing.NavigationController;
import de.chefkoch.raclette.rx.lifecycle.ViewModelLifecycleProvider;
import rx.Observable;

/**
 * Created by christophwidulle on 22.05.16.
 */
public abstract class RxUpdatableViewModel<T> extends UpdatableViewModel<T> implements Updatable<T>, ViewModelLifecycleProvider {
    private final ViewModelRxExtension rxExtension = new ViewModelRxExtension();

    @Override
    public final Observable<ViewModelLifecycleState> lifecycle() {
        return rxExtension.lifecycle();
    }

    @Override
    public final <T> Observable.Transformer<T, T> bindUntilEvent(ViewModelLifecycleState event) {
        return rxExtension.bindUntilEvent(event);
    }

    @Override
    public <T> Observable.Transformer<T, T> bindUntilDestroy() {
        return bindUntilEvent(ViewModelLifecycleState.VIEWMODEL_DESTROY);
    }

    @Override
    public final <T> Observable.Transformer<T, T> bindToLifecycle() {
        return rxExtension.bindToLifecycle();
    }

    public ViewModelRxExtension rx() {
        return rxExtension;
    }

    @Override
    void setNavigationController(NavigationController navigationController) {
        super.setNavigationController(navigationController);
        rxExtension.setNavigationController(navigationController);
    }

    @Override
    protected void updateState(ViewModelLifecycleState viewModelLifecycleState) {
        super.updateState(viewModelLifecycleState);
        rxExtension.updateState(viewModelLifecycleState);
    }
}
