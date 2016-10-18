package de.chefkoch.raclette.android.support;

import org.junit.Test;

import java.lang.ref.WeakReference;

import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.android.UpdatableViewComposition;

import static org.junit.Assert.*;

/**
 * Created by christophwidulle on 17.10.16.
 */
public class CompositionMultiViewBindingAdapterTest {

    @Test
    public void testBuilder() throws Exception {


        CompositionMultiViewBindingAdapter
                .builder(new ItemViewTypeMapping<WeakReference<String>>() {
                    @Override
                    public int getItemViewTypeFor(WeakReference<String> item) {
                        return 0;
                    }
                })
                .withElement(1, new CompositionMultiViewBindingAdapter.UpdatableViewCompositionFactory<WeakReference<String>>() {
                    @Override
                    public UpdatableViewComposition<WeakReference<String>, ? extends UpdatableViewModel<WeakReference<String>>, ?> create() {
                        return null;
                    }
                })
        .build();


        final CompositionMultiViewBindingAdapter<String > build = CompositionMultiViewBindingAdapter
                .builder(new ItemViewTypeMapping<String>() {
                    @Override
                    public int getItemViewTypeFor(String item) {
                        return 0;
                    }
                })
                .withElement(1, new CompositionMultiViewBindingAdapter.UpdatableViewCompositionFactory<String>() {
                    @Override
                    public UpdatableViewComposition<String, ? extends UpdatableViewModel<String>, ?> create() {
                        return null;
                    }
                })
                .build();

    }
}