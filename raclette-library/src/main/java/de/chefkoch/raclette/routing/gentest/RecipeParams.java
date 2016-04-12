package de.chefkoch.raclette.routing.gentest;

import android.os.Bundle;


/**
 * Created by christophwidulle on 07.12.15.
 */
public class RecipeParams {

    private final Bundle params;

    public RecipeParams(Bundle params) {
        this.params = params;
    }

    public static RecipeParams of(Bundle params) {
        return new RecipeParams(params);
    }

   /* public void inject(RecipeViewModel recipeViewModel) {
        new RecipeViewModel$NavParamsInjector().inject(this, recipeViewModel);
    }*/

    public String recipeId() {
        return params.getString(Recipe$Route.Params.RecipeId.Key, null);
    }
}
