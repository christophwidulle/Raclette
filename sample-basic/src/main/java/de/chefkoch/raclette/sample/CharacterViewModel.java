package de.chefkoch.raclette.sample;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.util.Log;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.rx.RxUtil;
import de.chefkoch.raclette.sample.rest.Character;
import de.chefkoch.raclette.sample.rest.SWApiClient;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by christophwidulle on 18.10.15.
 */
public class CharacterViewModel extends ViewModel {

    public ObservableField<Character> characterField = new ObservableField<>();


    @Override
    protected void onViewModelCreated(Bundle viewModelParams) {
        int index = viewModelParams.getInt("id");
        load(String.valueOf(index));
    }


    private void load(String id) {
        new SWApiClient().people().get(id)
                .compose(RxUtil.<Character>applySchedulers())
                .onErrorReturn(new Func1<Throwable, Character>() {
                    @Override
                    public Character call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Action1<Character>() {
                    @Override
                    public void call(Character character) {
                        characterField.set(character);
                    }
                });
    }

}
