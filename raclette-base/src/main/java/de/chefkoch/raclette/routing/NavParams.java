package de.chefkoch.raclette.routing;

import android.os.Bundle;
import de.chefkoch.raclette.RacletteException;
import de.chefkoch.raclette.ViewModel;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christophwidulle on 20.04.16.
 */
public abstract class NavParams {

    public static class None extends NavParams {

        @Override
        public Bundle toBundle() {
            return new Bundle();
        }
    }


    public abstract Bundle toBundle();

    public Bundle toArgumentsBundle() {
        final Bundle params = toBundle();
        final Bundle bundle = new Bundle();
        ViewModel.Params.apply(params, bundle);
        return bundle;
    }

    private static InjectorFactory INJECTOR_FACTORY_INSTANCE;
    private static final Object LOCK = new Object();

    public static InjectorFactory injectors() {
        if (INJECTOR_FACTORY_INSTANCE == null) {
            synchronized (LOCK) {
                if (INJECTOR_FACTORY_INSTANCE == null) {
                    INJECTOR_FACTORY_INSTANCE = new InjectorFactory();
                    try {
                        Class.forName("de.chefkoch.raclette.routing.AllNavParamsDict");
                    } catch (ClassNotFoundException e) {
                        //Not Using NavParams Generator. Can be ignored.
                    }
                }
            }
        }
        return INJECTOR_FACTORY_INSTANCE;
    }

    public static class InjectorFactory {

        private final Map<Class<? extends ViewModel>, Class<? extends Injector<?>>> injectorMap = new HashMap<>();

        InjectorFactory() {
        }

        public <T extends ViewModel> void register(Class<T> klass, Class<? extends Injector<T>> navParamsInjector) {
            injectorMap.put(klass, navParamsInjector);
        }

        @SuppressWarnings("unchecked")
        <T extends ViewModel> Injector<T> createInjector(Class<T> klass, Bundle params) {
            Class<? extends Injector> injectorClass = injectorMap.get(klass);
            if (injectorClass != null) {
                try {
                    Constructor<? extends Injector> constructor = injectorClass.getConstructor(Bundle.class);
                    constructor.setAccessible(true);
                    return constructor.newInstance(params);
                } catch (Exception e) {
                    throw new RacletteException("some code generation went terrible wrong :(", e);
                }
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        public void inject(ViewModel viewModel, Bundle params) {
            Injector injector = createInjector(viewModel.getClass(), params);
            if (injector != null) {
                injector.inject(viewModel);
            }
        }
    }


    public interface Injector<V> {
        void inject(V viewModel);
    }

}
