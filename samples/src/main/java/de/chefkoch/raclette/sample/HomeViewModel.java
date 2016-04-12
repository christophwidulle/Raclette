package de.chefkoch.raclette.sample;

import android.os.Bundle;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.sample.rest.Person;
import de.chefkoch.raclette.sample.rest.SWApiClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import java.util.List;

/**
 * Created by christophwidulle on 18.10.15.
 */
public class HomeViewModel extends ViewModel {


    @Override
    protected void onCreate(final Bundle params) {
        super.onCreate(params);

        new SWApiClient().people().get("1").toList().subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Person>>() {
            @Override
            public void call(List<Person> persons) {
                System.out.println(persons);
            }
        });
    }
}
