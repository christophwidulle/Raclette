package de.chefkoch.raclette.sample;

import android.os.Bundle;
import de.chefkoch.raclette.routing.NavParams;

/**
 * Created by christophwidulle on 20.04.16.
 */
public class CharacterViewModel$ParamsInjector implements NavParams.Injector<CharacterViewModel> {

    @Override
    public void inject(Bundle params, CharacterViewModel viewModel) {
        viewModel.characterId = params.getString("characterId");

    }
}
