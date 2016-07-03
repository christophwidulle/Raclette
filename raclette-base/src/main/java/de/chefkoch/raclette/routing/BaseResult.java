package de.chefkoch.raclette.routing;

import android.os.Bundle;

/**
 * Created by christophwidulle on 02.07.16.
 */
public abstract class BaseResult<T> {

    T value;

    public BaseResult(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        writeValue(value, bundle);
        return bundle;
    }

    public abstract void writeValue(T value, Bundle bundle);

}
