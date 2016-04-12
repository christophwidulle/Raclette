package de.chefkoch.raclette.sample.inject;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.chefkoch.raclette.ViewModel;
import de.chefkoch.raclette.ViewModelManager;

@Module()
public class ServicesModule {

    @Provides
    @Singleton
    ViewModelManager provideViewModelManager(@AppContext final Context context) {
        return new ViewModelManager(new ViewModelManager.Injector() {
            @Override
            public void inject(ViewModel viewModel) {
                AppComponent.Locator.get(context).inject(viewModel);
            }
        });
    }
}
