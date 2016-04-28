package de.chefkoch.raclette.routing;

import android.app.Activity;

/**
 * Created by christophwidulle on 07.12.15.
 */
public abstract class Route {


    public static enum TargetType {
        Activity, DialogFragment, SupportDialogFragment
    }

    final String path;
    Class targetClass;
    final TargetType targetType;

    protected Route(String path, Class targetClass, TargetType targetType) {
        this.path = path;
        this.targetClass = targetClass;

        this.targetType = targetType;
    }

    public boolean isDialogTargetType() {
        return targetType == TargetType.DialogFragment || targetType == TargetType.SupportDialogFragment;
    }

    public boolean isActivityTargetType() {
        return targetType == TargetType.Activity;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public String getPath() {
        return path;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public abstract NavRequestBuilder with();

    public interface NavRequestBuilder {
        NavRequest build();
    }
}
