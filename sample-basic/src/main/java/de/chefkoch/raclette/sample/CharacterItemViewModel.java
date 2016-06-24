package de.chefkoch.raclette.sample;

import android.databinding.ObservableField;
import android.os.Bundle;
import de.chefkoch.raclette.RxUpdatableViewModel;
import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.routing.ResultCallback;
import de.chefkoch.raclette.routing.Routes;
import de.chefkoch.raclette.rx.Command;
import de.chefkoch.raclette.sample.rest.Character;
import rx.functions.Action1;

/**
 * Created by christophwidulle on 21.05.16.
 */
public class CharacterItemViewModel extends RxUpdatableViewModel<Character> {

    public ObservableField<Character> item = new ObservableField<>();

    public Command<Void> clickCommand = Command.createAndBind(lifecycle());


    @Override
    protected void onViewModelCreated(Bundle viewModelParams) {
        super.onViewModelCreated(viewModelParams);

        clickCommand.subscribe(new Action1<Void>() {
            @Override
            public void call(Void character) {
                navigate().toForResult(Routes.character().with(CharacterParams.create().characterIndex("1")), new ResultCallback() {
                    @Override
                    public void onResult(Bundle values) {
                        System.out.println(values);
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                    }
                });
            }
        });

    }

    @Override
    public void update(Character item) {
        this.item.set(item);
    }
}
