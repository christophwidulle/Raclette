package de.chefkoch.raclette.sample;

import android.os.Bundle;
import de.chefkoch.raclette.routing.NavParams;

/**
 * Created by christophwidulle on 20.04.16.
 */
//Generated
public class CharacterParams extends NavParams implements NavParams.Injector<CharacterViewModel> {

    private final String characterIndex;

    public CharacterParams(Bundle params) {
        this.characterIndex = params.getString("characterIndex");
    }

    public static CharacterParams from(Bundle params) {
        return new CharacterParams(params);
    }

    @Override
    public void inject(CharacterViewModel viewModel) {
        viewModel.characterIndex = this.characterIndex;
    }


}
