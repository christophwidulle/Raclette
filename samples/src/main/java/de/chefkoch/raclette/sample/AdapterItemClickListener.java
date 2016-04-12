package de.chefkoch.raclette.sample;

import android.view.View;

/**
 * Created by christophwidulle on 30.09.15.
 */
public interface AdapterItemClickListener<T> {

    void onClick(T item, int position, View view);


    public static class Empty<T> implements AdapterItemClickListener<T> {

        @Override
        public void onClick(T item, int position, View view) {

        }
    }
}
