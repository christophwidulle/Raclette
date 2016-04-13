package de.chefkoch.raclette.sample;

import android.databinding.ObservableField;
import android.os.Bundle;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.rx.RxUtil;
import de.chefkoch.raclette.sample.rest.Character;
import de.chefkoch.raclette.sample.rest.SWApiClient;
import rx.functions.Action1;

/**
 * Created by christophwidulle on 18.10.15.
 */
public class CharacterViewModel extends ViewModel {

    public ObservableField<Character> characterField = new ObservableField<>();


    @Override
    protected void onViewModelCreated(Bundle viewModelParams) {
        load("1");
    }


    private void load(String id) {
        new SWApiClient().people().get(id)
                .compose(RxUtil.<Character>applySchedulers())
                .subscribe(new Action1<Character>() {
                    @Override
                    public void call(Character character) {
                        characterField.set(character);
                    }
                });
    }

}
