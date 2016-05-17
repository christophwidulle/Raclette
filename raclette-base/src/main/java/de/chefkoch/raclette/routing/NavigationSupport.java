package de.chefkoch.raclette.routing;

/**
 * Created by christophwidulle on 20.12.15.
 */
public interface NavigationSupport {

    boolean onNavigateTo(NavRequest navRequest);

    boolean onBack();
}
