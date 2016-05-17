package de.chefkoch.raclette.routing;

/**
 * Created by christophwidulle on 16.05.16.
 */
public interface UsesNavigationSupport {

    void setActiveNavigationSupport(NavigationSupport navigationSupport);

    void clearActiveNavigationSupport();
}
