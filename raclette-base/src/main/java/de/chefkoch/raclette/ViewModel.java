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
    }

    private ViewModelLifecycleState state = ViewModelLifecycleState.NEW;
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

    protected NavigationController navigate() {
        return navigationController;
    }

    public ViewModelLifecycleState getState() {
        return state;
    }

    void viewModelCreated(Bundle viewModelParams) {
        this.onViewModelCreate(viewModelParams);
    }

    void viewModelCreate(Bundle bundle) {
        this.state = ViewModelLifecycleState.VIEWMODEL_CREATE;
        this.onViewModelCreate(bundle);
    }

    protected void onViewModelCreate(Bundle viewModelParams) {

    }

    void create(Bundle bundle) {
        this.state = ViewModelLifecycleState.CREATE;
        this.onCreate(bundle);
    }

    protected void onCreate(Bundle params) {

    }

    void start() {
        this.state = ViewModelLifecycleState.START;
        this.onStart();
    }

    protected void onStart() {

    }

    void resume() {
        this.state = ViewModelLifecycleState.RESUME;
        this.onResume();
    }

    protected void onResume() {

    }

    void pause() {
        this.state = ViewModelLifecycleState.PAUSE;
        this.onPause();
    }

    protected void onPause() {

    }

    void stop() {
        this.state = ViewModelLifecycleState.STOP;
        this.onStop();
    }

    protected void onStop() {

    }

    void destroy() {
        this.state = ViewModelLifecycleState.DESTROY;
        this.onDestroy();
    }

    protected void onDestroy() {

    }

    void viewModelDestroy() {
        this.state = ViewModelLifecycleState.VIEWMODEL_DESTROY;
        this.onViewModelDestroy();
    }

    protected void onViewModelDestroy() {

    }

}
