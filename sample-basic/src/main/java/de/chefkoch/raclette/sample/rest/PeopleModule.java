package de.chefkoch.raclette.sample.rest;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by christophwidulle on 25.09.15.
 */
public interface PeopleModule {

    //http://swapi.co/api/
    @GET("people/{id}")
    Observable<Character> get(@Path("id") String id);

    @GET("people")
    Observable<CharactersResponse> list();


}
