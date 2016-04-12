package de.chefkoch.raclette.sample;

import android.os.Bundle;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.sample.rest.Person;
import de.chefkoch.raclette.sample.rest.SWApiClient;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.List;

/**
 * Created by christophwidulle on 18.10.15.
 */
public class HomeViewModel extends ViewModel {


    @Override
    protected void onCreate(final Bundle params) {
        super.onCreate(params);

        new SWApiClient().people().get("1").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Person>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable);
            }

            @Override
            public void onNext(Person person) {
                System.out.println(person);
            }
        });

    }
}
