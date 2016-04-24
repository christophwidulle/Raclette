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

    public String getPath() {
        return path;
    }

    public Class<? extends Activity> getTargetClass() {
        return targetClass;
    }

    public abstract NavRequestBuilder with();

    public interface  NavRequestBuilder {
         NavRequest build();
    }
}
