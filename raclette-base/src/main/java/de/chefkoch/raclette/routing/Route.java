package de.chefkoch.raclette.routing;

import android.app.Activity;

/**
 * Created by christophwidulle on 07.12.15.
 */
public abstract class Route {

    final String path;
    final Class<? extends Activity> targetClass;

    protected Route(String path, Class<? extends Activity> targetClass) {
        this.path = path;
        this.targetClass = targetClass;
    }

    public static class Param {
        public final String key;
        public final Class type;

        public Param(String key, Class type) {
            this.key = key;
            this.type = type;
        }
    }

    public String getPath() {
        return path;
    }

    public Class<? extends Activity> getTargetClass() {
        return targetClass;
    }

    public NavRequest request() {
        return requestBuilder().build();
    }

    public abstract NavRequestBuilder requestBuilder();

    public static abstract class NavRequestBuilder {
        public abstract NavRequest build();
    }
}
