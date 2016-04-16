package de.chefkoch.raclette.sample;

import android.databinding.ObservableField;
import android.os.Bundle;
import com.jakewharton.rxrelay.ReplayRelay;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.rx.RxUtil;
import de.chefkoch.raclette.sample.rest.Character;
import de.chefkoch.raclette.sample.rest.CharactersResponse;
import de.chefkoch.raclette.sample.rest.SWApiClient;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.List;

/**
 * Created by christophwidulle on 18.10.15.
 */
public class CharacterListViewModel extends ViewModel {

    ReplayRelay<List<Character>> charactersSubject = ReplayRelay.create();


    @Override
    protected void onViewModelCreated(Bundle viewModelParams) {
        load();
    }

    Observable<List<Character>> characters() {
        return charactersSubject.asObservable();
    }

    private void load() {
        new SWApiClient().people().list()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<CharactersResponse, Observable<Character>>() {
                    @Override
                    public Observable<Character> call(CharactersResponse charactersResponse) {
                        return Observable.from(charactersResponse.getResults());
                    }
                })
                .toList()
                .subscribe(new Action1<List<Character>>() {
                    @Override
                    public void call(List<Character> characters) {
                        charactersSubject.call(characters);
                    }
                });
    }

}
