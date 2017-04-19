package de.chefkoch.raclette;

import android.os.Bundle;
import de.chefkoch.raclette.routing.NavigationController;

import java.util.UUID;

/**
 * Created by christophwidulle on 09.05.16.
 */
public class ViewModelTestSupport {

    public static void setRandomId(ViewModel viewModel) {
        setId(viewModel, UUID.randomUUID().toString());
    }

    public static void setId(ViewModel viewModel, String id) {
        viewModel.setId(id);
    }

    public static void injectParams(ViewModel viewModel, Bundle bundle) {
        viewModel.injectParams(bundle);
    }

    public static void setNavController(ViewModel viewModel, NavigationController navController) {
        viewModel.setNavigationController(navController);
    }

    public static void init(ViewModel viewModel, NavigationController navigationController, Bundle params) {
        params = params == null ? new Bundle() : params;
        ViewModelTestSupport.setRandomId(viewModel);
        ViewModelTestSupport.injectParams(viewModel, params);
        ViewModelTestSupport.setNavController(viewModel, navigationController);
    }

}