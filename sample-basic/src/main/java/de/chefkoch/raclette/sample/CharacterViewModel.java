package de.chefkoch.raclette.sample;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.util.Log;
import com.jakewharton.rxrelay.PublishRelay;
import de.chefkoch.raclette.*;
import de.chefkoch.raclette.routing.Nav;
import de.chefkoch.raclette.routing.Routes;
import de.chefkoch.raclette.rx.Command;
import de.chefkoch.raclette.rx.RxUtil;
import de.chefkoch.raclette.sample.rest.Character;
import de.chefkoch.raclette.sample.rest.SWApiClient;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by christophwidulle on 18.10.15.
 */
@Nav.InjectParams
public class CharacterViewModel extends RxViewModel {

    public final ObservableField<Character> characterField = new ObservableField<>();

    public final Command<Void> testCommand = Command.create();

    @Nav.Param
    int id;


    @Nav.Param
    String characterIndex;


    @Nav.Param
    boolean gogo;

    @Nav.Param
    TestParameter testParameter;


    @Nav.Param
    TestParameter2 testParameter2;


    @Override
    protected void onViewModelCreated(Bundle viewModelParams) {
        load(characterIndex);

        navigate().to(Routes.character().with(CharacterParams.create().characterIndex("1")));

        testCommand.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                System.out.println();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void load(String id) {

        new SWApiClient().people().get(id)
                .compose(RxUtil.<Character>applySchedulers())
                .compose(this.<Character>bindToLifecycle())
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
