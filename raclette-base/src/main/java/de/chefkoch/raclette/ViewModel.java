package de.chefkoch.raclette;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.support.annotation.CallSuper;
import de.chefkoch.raclette.routing.NavParams;
import de.chefkoch.raclette.routing.NavigationController;

import java.lang.ref.WeakReference;

/**
 * Created by christophwidulle on 28.09.15.
 */
public class ViewModel {

    public static final String EXTRA_VIEWMODEL_ID = "#RACLETTE_VIEWMODEL_ID";

    public static enum LifecycleState {
        ACTIVE, INACTIVE, DESTROYED

    }

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

    protected NavigationController navigate(){
        return navigationController;
    }

    protected void onViewModelCreated(Bundle viewModelParams) {

    }

    protected void onCreate(Bundle params) {

    }

    protected void onStart() {

    }

    protected void onResume() {

    }

    protected void onPause() {

    }

    protected void onStop() {

    }

    protected void onDestroy() {

    }

    protected void onViewModelDestroyed() {

    }

}
