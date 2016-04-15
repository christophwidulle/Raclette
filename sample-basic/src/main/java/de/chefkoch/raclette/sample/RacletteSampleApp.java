package de.chefkoch.raclette.sample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import de.chefkoch.raclette.Raclette;

/**
 * Created by christophwidulle on 01.11.15.
 */
public class RacletteSampleApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Raclette.builder().viewModelBindingId(BR.viewModel).buildAsSingelton();

        LeakCanary.install(this);

    }

}
