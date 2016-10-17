package de.chefkoch.raclette.android.support;

/**
 * Created by christophwidulle on 17.10.16.
 */
public interface ItemViewTypeMapping<T> {

    int getItemViewTypeFor(T item);
}
