package de.chefkoch.raclette;

import android.content.Context;
import android.os.Bundle;


import de.chefkoch.raclette.routing.NavigationController;

/**
 * Created by christophwidulle on 28.09.15.
 */
public class ViewModel {

    public static class Params {
        public static final String EXTRA_KEY = "#VIEWMODEL_PARAMS";

        public static Bundle from(Bundle source) {
            return source == null ? null : source.getBundle(EXTRA_KEY);
        }

        public static void apply(Bundle params, Bundle target) {
            if (params != null) {
                target.putBundle(EXTRA_KEY, params);
            }
        }

    }

    public static final String EXTRA_ID = "#VIEWMODEL_ID";

    private String id;
    private Context context;

    private NavigationController navigationController;


    public ViewModel() {
    }

    void setId(String id) {
        this.id = id;
    }

    void setContext(Context context) {
        this.context = context;
    }


    public String getId() {
        return id;
    }


    protected void onDestroy() {

    }

    protected void onCreate(Bundle params) {

    }

    protected void onResume() {

    }

    protected void onPause() {

    }


}
