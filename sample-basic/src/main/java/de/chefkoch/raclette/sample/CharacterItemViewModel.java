package de.chefkoch.raclette.sample;

import android.databinding.ObservableField;
import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.sample.rest.Character;

/**
 * Created by christophwidulle on 21.05.16.
 */
public class CharacterItemViewModel extends UpdatableViewModel<Character> {

    public ObservableField<Character> item = new ObservableField<>();

    @Override
    public void update(Character item) {
        this.item.set(item);
    }
}
