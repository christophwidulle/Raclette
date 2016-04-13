package de.chefkoch.raclette.routing;

/**
 * Created by christophwidulle on 07.12.15.
 */
public abstract class Route {

    public String Path;

    public static class Param {
        public final String Key;
        public final Class Type;

        public Param(String key, Class type) {
            this.Key = key;
            this.Type = type;
        }
    }

    public NavRequest request() {
        return requestBuilder().build();
    }

    public abstract NavRequestBuilder requestBuilder();

    public static abstract class NavRequestBuilder {
        public abstract NavRequest build();
    }
}
