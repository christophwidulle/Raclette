package de.chefkoch.raclette.routing.gentest;

import android.os.Bundle;

import de.chefkoch.raclette.routing.NavRequest;
import de.chefkoch.raclette.routing.Route;

/**
 * Created by christophwidulle on 06.12.15.
 */
public class Recipe$Route extends Route {

    public static final class Params {

        public static Param RecipeId = new Param("recipeId", String.class);

        private Params() {
        }
    }

    public Recipe$Route() {
        Path = "/recipe";
    }

    public boolean matches(String route) {
        return Path.equals(route);
    }

    public NavRequestBuilder requestBuilder() {
        return new NavRequestBuilder(Path);
    }


    public static class NavRequestBuilder extends Route.NavRequestBuilder {
        private final String route;
        private Bundle params = new Bundle();

        public NavRequestBuilder(String route) {
            this.route = route;
        }

        public NavRequestBuilder recipeId(String recipeId) {
            this.params.putString(Params.RecipeId.Key, recipeId);
            return this;
        }

        public NavRequest build() {
            return new NavRequest(route, params);
        }
    }
}
