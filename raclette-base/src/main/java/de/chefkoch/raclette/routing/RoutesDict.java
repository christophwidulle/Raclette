package de.chefkoch.raclette.routing;

import de.chefkoch.raclette.RacletteException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by christophwidulle on 21.04.16.
 */
public class RoutesDict {

    private static Map<String, Route> ALL_ROUTES = new HashMap<>();

    public static void register(Route route) {
        if (ALL_ROUTES.containsKey(route.getPath())) {
            throw new RacletteException("ambiguous navigation path declared: " + route.getPath());
        } else {
            ALL_ROUTES.put(route.getPath(), route);
        }
    }

    public static Route findBy(String path) {
        return ALL_ROUTES.get(path);
    }
}
