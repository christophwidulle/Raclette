package de.chefkoch.raclette;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.support.annotation.CallSuper;
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
    private WeakReference<Context> context;
    private NavigationController navigationController;

    public ViewModel() {
    }

    void setId(String id) {
        this.id = id;
    }

    void setContext(Context context) {
        this.context = new WeakReference<Context>(context);
    }


    public String getId() {
        return id;
    }



    protected void onViewModelCreated(Bundle viewModelParams) {

    }

    protected void onViewModelDestroyed() {

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

}
