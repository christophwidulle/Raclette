package de.chefkoch.raclette;

import android.os.Bundle;
import de.chefkoch.raclette.routing.NavigationController;
import de.chefkoch.raclette.rx.lifecycle.ViewModelLifecycleProvider;
import rx.Observable;

/**
 * Created by christophwidulle on 14.04.16.
 */
public class RxViewModel extends ViewModel implements ViewModelLifecycleProvider {

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
    void viewModelCreate(Bundle bundle) {
        super.viewModelCreate(bundle);
        rxExtension.viewModelCreate(bundle);

    }

    @Override
    void viewModelDestroy() {
        super.viewModelDestroy();
        rxExtension.viewModelDestroy();
    }

    @Override
    void create(Bundle params) {
        super.create(params);
        rxExtension.create(params);
    }

    @Override
    void start() {
        super.start();
        rxExtension.start();
    }

    @Override
    void resume() {
        super.resume();
        rxExtension.resume();
    }

    @Override
    void pause() {
        super.pause();
        rxExtension.pause();
    }

    @Override
    void stop() {
        super.stop();
        rxExtension.stop();
    }

    @Override
    void destroy() {
        super.destroy();
        rxExtension.destroy();
    }
}
