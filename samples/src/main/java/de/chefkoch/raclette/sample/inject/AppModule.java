package de.chefkoch.raclette.sample.inject;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.chefkoch.raclette.sample.RacletteSampleApp;

@Module
public class AppModule {

    private final RacletteSampleApp application;

    public AppModule(RacletteSampleApp application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @AppContext
    Context provideApplicationContext() {
        return this.application;
    }
}
