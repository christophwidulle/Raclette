package de.chefkoch.raclette.routing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by christophwidulle on 06.12.15.
 */
public class Nav {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Route {

        String value();

        Param[] params() default {};

    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Routes {

        Route[] value() default {};

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Dispatch {

        String[] value() default {};

        boolean handParams() default false;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface Param {

        String value();

        Class type() default String.class;

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.FIELD)
        public static @interface Inject {

            String value() default "";

        }
    }


}
