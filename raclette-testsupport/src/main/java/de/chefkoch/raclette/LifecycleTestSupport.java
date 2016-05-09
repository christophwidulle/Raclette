package de.chefkoch.raclette;

import android.os.Bundle;

/**
 * Created by christophwidulle on 09.05.16.
 */
public class LifecycleTestSupport {


    public static void startLifecycle(ViewModel viewModel,
                                      Bundle params) {
        playLifecycleUntil(viewModel, params, ViewModelLifecycleState.RESUME);

    }

    public static void playLifecycleUntil(ViewModel viewModel,
                                          Bundle params,
                                          ViewModelLifecycleState untilLifecycleState) {

        ViewModelLifecycleState currentState = viewModel.getState();

        if (currentState != ViewModelLifecycleState.VIEWMODEL_CREATE) {
            viewModel.viewModelCreate(params);
            if (untilLifecycleState == ViewModelLifecycleState.VIEWMODEL_CREATE) return;
        }
        if (currentState != ViewModelLifecycleState.CREATE) {
            viewModel.create(params);
            if (untilLifecycleState == ViewModelLifecycleState.CREATE) return;
        }
        if (currentState != ViewModelLifecycleState.START) {
            viewModel.start();
            if (untilLifecycleState == ViewModelLifecycleState.START) return;
        }
        if (currentState != ViewModelLifecycleState.RESUME) {
            viewModel.resume();
            if (untilLifecycleState == ViewModelLifecycleState.RESUME) return;
        }
        if (currentState != ViewModelLifecycleState.PAUSE) {
            viewModel.pause();
            if (untilLifecycleState == ViewModelLifecycleState.PAUSE) return;
        }
        if (currentState != ViewModelLifecycleState.STOP) {
            viewModel.stop();
            if (untilLifecycleState == ViewModelLifecycleState.STOP) return;
        }
        if (currentState != ViewModelLifecycleState.VIEWMODEL_DESTROY) {
            viewModel.viewModelDestroy();
            if (untilLifecycleState == ViewModelLifecycleState.VIEWMODEL_DESTROY) return;
        }
    }

    public static void pauseLifecycle(ViewModel viewModel) {

        ViewModelLifecycleState currentState = viewModel.getState();

        if (currentState == ViewModelLifecycleState.RESUME) {
            viewModel.pause();
        } else {
            throw new IllegalStateException("ViewModel was expected in state RESUME but was " + currentState);
        }

    }

    public static void resumeLifecycle(ViewModel viewModel) {

        ViewModelLifecycleState currentState = viewModel.getState();
        if (currentState == ViewModelLifecycleState.STOP) {
            viewModel.start();
            viewModel.resume();
        } else if (currentState == ViewModelLifecycleState.PAUSE) {
            viewModel.resume();
        } else {
            throw new IllegalStateException("ViewModel was expected in state STOP or PAUSE but was " + currentState);
        }

    }

    public static void destroyLifecycle(ViewModel viewModel) {
        ViewModelLifecycleState currentState = viewModel.getState();
        if (currentState == ViewModelLifecycleState.STOP) {
            viewModel.destroy();
            viewModel.viewModelDestroy();
        } else if (currentState == ViewModelLifecycleState.PAUSE) {
            viewModel.stop();
            viewModel.destroy();
            viewModel.viewModelDestroy();
        }  else if (currentState == ViewModelLifecycleState.RESUME) {
            viewModel.pause();
            viewModel.stop();
            viewModel.destroy();
            viewModel.viewModelDestroy();
        } else {
            throw new IllegalStateException("ViewModel was expected in state STOP or PAUSE but was " + currentState);
        }

    }

    public static void resumeLifecycleFromDestroyed(ViewModel viewModel, Bundle params) {
        ViewModelLifecycleState currentState = viewModel.getState();
        if (currentState == ViewModelLifecycleState.DESTROY) {
            viewModel.create(params);
            viewModel.start();
            viewModel.resume();
        } else {
            throw new IllegalStateException("ViewModel was expected in state DESTROY but was " + currentState);
        }

    }


}