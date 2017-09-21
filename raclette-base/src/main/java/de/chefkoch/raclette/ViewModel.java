package de.chefkoch.raclette;

import android.os.Bundle;


import de.chefkoch.raclette.routing.NavParams;
import de.chefkoch.raclette.routing.NavigationController;

/**
 * Created by christophwidulle on 28.09.15.
 */
public class ViewModel {

    public static final String EXTRA_VIEWMODEL_ID = "#RACLETTE_VIEWMODEL_ID";


    public static class Params {

        public static final String EXTRA_KEY = "#RACLETTE_VIEWMODEL_PARAMS";

        public static Bundle from(Bundle source) {
            return source == null ? null : source.getBundle(EXTRA_KEY);
        }

        public static void apply(Bundle params, Bundle target) {
            if (params != null) {
                target.putBundle(EXTRA_KEY, params);
            }
        }

        public static Bundle asBundle(Bundle params) {
            Bundle target = new Bundle();
            apply(params, target);
            return target;
        }
    }

    ViewModelLifecycleState state = ViewModelLifecycleState.NEW;
    private String id;
    private NavigationController navigationController;

    void setId(String id) {
        this.id = id;
    }

    void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    public String getId() {
        return id;
    }

    void injectParams(Bundle params) {
        NavParams.injectors().inject(this, params);
    }

    public NavigationController navigate() {
        return navigationController;
    }

    public ViewModelLifecycleState getState() {
        return state;
    }

    protected void updateState(ViewModelLifecycleState viewModelLifecycleState) {
        state = viewModelLifecycleState;
    }

    void viewModelCreate(Bundle bundle) {
        updateState(ViewModelLifecycleState.VIEWMODEL_CREATE);
        this.onViewModelCreated(bundle);
    }

    protected void onViewModelCreated(Bundle viewModelParams) {

    }

    void create(Bundle bundle) {
        updateState(ViewModelLifecycleState.CREATE);
        this.onCreate(bundle);
    }

    protected void onCreate(Bundle params) {

    }

    void start() {
        updateState(ViewModelLifecycleState.START);
        this.onStart();
    }

    protected void onStart() {

    }

    void resume() {
        updateState(ViewModelLifecycleState.RESUME);
        this.onResume();
    }

    protected void onResume() {

    }

    void pause() {
        updateState(ViewModelLifecycleState.PAUSE);
        this.onPause();
    }

    protected void onPause() {

    }

    void stop() {
        updateState(ViewModelLifecycleState.STOP);
        this.onStop();
    }

    protected void onStop() {

    }

    void destroy() {
        updateState(ViewModelLifecycleState.DESTROY);
        this.onDestroy();
    }

    protected void onDestroy() {

    }

    void viewModelDestroy() {
        updateState(ViewModelLifecycleState.VIEWMODEL_DESTROY);
        this.onViewModelDestroy();
    }

    protected void onViewModelDestroy() {

    }

}
