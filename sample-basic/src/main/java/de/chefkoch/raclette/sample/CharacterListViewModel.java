package de.chefkoch.raclette.sample;

import android.os.Bundle;
import com.jakewharton.rxrelay.ReplayRelay;
import de.chefkoch.raclette.ViewModel;
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

    private ReplayRelay<List<Character>> charactersSubject = ReplayRelay.create();

    Observable<List<Character>> characters() {
        return charactersSubject.asObservable();
    }

    private int page = 1;

    @Override
    protected void onViewModelCreated(Bundle viewModelParams) {
        loadNext();
    }

    private void loadNext() {
        new SWApiClient().people().list(page++)
                .subscribeOn(Schedulers.io())
                .map(new Func1<CharactersResponse, List<Character>>() {
                    @Override
                    public List<Character> call(CharactersResponse charactersResponse) {
                        if (charactersResponse.getNext() != null) {
                            loadNext();
                        }
                        return charactersResponse.getResults();
                    }
                })
                .subscribe(new Action1<List<Character>>() {
                    @Override
                    public void call(List<Character> characters) {
                        charactersSubject.call(characters);
                    }
                });
    }

}
