package de.chefkoch.raclette.sample.rest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by christophwidulle on 25.09.15.
 */
public class SWApiClient {

    private Retrofit retrofit;
    private PeopleModule peopleModule;

    public SWApiClient() {
        initWithBaseUrl("http://swapi.co/api/");
    }

    private void initWithBaseUrl(String url) {

        OkHttpClient client = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        peopleModule = retrofit.create(PeopleModule.class);
    }

    public PeopleModule people() {
        return peopleModule;
    }

}
