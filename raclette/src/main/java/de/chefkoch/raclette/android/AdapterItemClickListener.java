package de.chefkoch.raclette.android;

import android.view.View;

/**
 * Created by christophwidulle on 16.04.16.
 */
public interface AdapterItemClickListener<T> {

    void onClick(T item, int position, View view);


    class Empty<T> implements AdapterItemClickListener<T> {

        @Override
        public void onClick(T item, int position, View view) {

        }
    }
}
