package de.chefkoch.raclette.sample.inject;

import android.content.Context;


import javax.inject.Singleton;

import dagger.Component;
import de.chefkoch.raclette.sample.RacletteSampleApp;
import de.chefkoch.raclette.ViewModel;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    @AppContext
    Context appContext();

    void inject(ViewModel viewModel);

    final class Initializer {
        private Initializer() {
        }

        public static AppComponent create(RacletteSampleApp app) {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(app))
                    .build();
        }
    }

    final class Locator {
        private Locator() {
        }
        public static AppComponent get(Context context) {
            return ((RacletteSampleApp) context.getApplicationContext()).getAppComponent();
        }
    }
}
