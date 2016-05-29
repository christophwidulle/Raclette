package de.chefkoch.raclette.routing;

import android.os.Bundle;
import de.chefkoch.raclette.ViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by christophwidulle on 06.12.15.
 */
public class Nav {

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.TYPE)
    public @interface Route {

        String value();

        Class<? extends ViewModel> navParamsFrom() default AutoDetect.class;

        public static class AutoDetect extends ViewModel {
        }
    }

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.TYPE)
    public @interface Dispatch {

        Route[] value() default {};

    }

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.FIELD)
    public @interface Param {

        String value() default "";

    }

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.TYPE)
    public @interface InjectParams {
    }

    public interface ParamsInjector<V> {

        void inject(Bundle params, V viewModel);
    }

}
