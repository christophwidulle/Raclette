package de.chefkoch.raclette.sample;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.util.Log;
import com.jakewharton.rxrelay.PublishRelay;
import de.chefkoch.raclette.TestParameter;
import de.chefkoch.raclette.TestParameter2;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.routing.Nav;
import de.chefkoch.raclette.rx.Command;
import de.chefkoch.raclette.rx.RxUtil;
import de.chefkoch.raclette.sample.rest.Character;
import de.chefkoch.raclette.sample.rest.SWApiClient;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by christophwidulle on 18.10.15.
 */
@Nav.InjectParams
public class CharacterViewModel extends ViewModel {

    public final ObservableField<Character> characterField = new ObservableField<>();

    public final Command<Void> testCommand = Command.create();


    @Nav.Param
    String characterIndex;

    @Nav.Param
    int id;

    @Nav.Param
    boolean gogo;

    @Nav.Param
    TestParameter testParameter;


    @Nav.Param
    TestParameter2 testParameter2;


    @Override
    protected void onViewModelCreated(Bundle viewModelParams) {
        load(characterIndex);

        testCommand.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                System.out.println();
            }
        });

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
