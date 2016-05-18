package de.chefkoch.raclette.routing;

import android.app.Activity;

/**
 * Created by christophwidulle on 07.12.15.
 */
public abstract class Route<T extends NavParams> {

    public enum TargetType {
        Activity, DialogFragment, SupportDialogFragment
    }

    private final String path;
    private Class targetClass;
    private final TargetType targetType;

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

    public NavRequest with(T params) {
        return new NavRequest(path, params.toBundle());
    }

    public interface NavRequestBuilder {
        NavRequest build();
    }
}
