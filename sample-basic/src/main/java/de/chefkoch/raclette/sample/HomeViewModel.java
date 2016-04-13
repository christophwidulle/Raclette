package de.chefkoch.raclette.sample;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.util.Log;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.sample.rest.Character;
import de.chefkoch.raclette.sample.rest.SWApiClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by christophwidulle on 18.10.15.
 */
public class HomeViewModel extends ViewModel {


    public ObservableField<Character> personField = new ObservableField<>();

    @Override
    protected void onCreate(final Bundle params) {
        super.onCreate(params);
        Log.d("ViewModel", "onCreate");
        new SWApiClient().people().get("1").subscribeOn(Schedulers.io()).subscribe(new Subscriber<Character>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable);
            }

            @Override
            public void onNext(Character character) {
                personField.set(character);
            }
        });

    }


    @Override
    protected void onViewModelCreated(Bundle viewModelParams) {
        super.onViewModelCreated(viewModelParams);
        Log.d("ViewModel", "onViewModelCreated");
    }

    @Override
    protected void onViewModelDestroyed() {
        super.onViewModelDestroyed();
        Log.d("ViewModel", "onViewModelDestroyed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ViewModel", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ViewModel", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ViewModel", "onDestroy");
    }
}
