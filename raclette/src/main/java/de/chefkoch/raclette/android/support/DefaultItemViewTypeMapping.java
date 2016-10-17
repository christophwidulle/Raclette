package de.chefkoch.raclette.android.support;

/**
 * Created by christophwidulle on 17.10.16.
 */
public class DefaultItemViewTypeMapping<T> implements ItemViewTypeMapping<T> {

    @Override
    public int getItemViewTypeFor(T item) {
        return item.getClass().hashCode();
    }
}