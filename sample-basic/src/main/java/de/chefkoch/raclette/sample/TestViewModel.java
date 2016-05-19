package de.chefkoch.raclette.sample;

import android.databinding.ObservableField;
import android.os.Bundle;
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
public class TestViewModel extends ViewModel {



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
    protected void onViewModelCreate(Bundle viewModelParams) {
        load(characterIndex);


    }


    private void load(String id) {

    }

}
