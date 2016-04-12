package de.chefkoch.raclette.sample;

import android.app.Application;

import de.chefkoch.raclette.Raclette;
import de.chefkoch.raclette.sample.inject.AppComponent;

/**
 * Created by christophwidulle on 01.11.15.
 */
public class RacletteSampleApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Raclette.builder(BR.viewModel).buildAsSingelton();

        appComponent = AppComponent.Initializer.create(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
