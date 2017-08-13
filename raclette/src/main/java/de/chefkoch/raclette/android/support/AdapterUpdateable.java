package de.chefkoch.raclette.android.support;

import java.util.Collection;

/**
 * Created by christophwidulle on 23.07.17.
 */
public interface AdapterUpdateable<T> {

    void setAll(Collection<T> items);

    void addAll(Collection<T> items);

    void add(T item);

    void removeAll();
}
