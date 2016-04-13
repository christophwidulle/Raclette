package de.chefkoch.raclette.sample;

import android.app.Application;

import de.chefkoch.raclette.Raclette;

/**
 * Created by christophwidulle on 01.11.15.
 */
public class RacletteSampleApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Raclette.builder(BR.viewModel).buildAsSingelton();

    }

}
