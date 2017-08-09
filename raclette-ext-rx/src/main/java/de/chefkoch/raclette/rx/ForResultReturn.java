package de.chefkoch.raclette.rx;

import de.chefkoch.raclette.routing.RoutingResult;

/**
 * Created by christophwidulle on 25.06.16.
 */
public class ForResultReturn {

    RoutingResult result;

    private ForResultReturn(RoutingResult result) {
        this.result = result;
    }

    private ForResultReturn() {

    }

    public static ForResultReturn from(RoutingResult result) {
        return new ForResultReturn(result);
    }

    public static ForResultReturn canceled() {
        return new ForResultReturn();
    }

    public RoutingResult getResult() {
        return result;
    }

    public boolean isCanceled() {
        return result == null;
    }

    public boolean hasResult() {
        return result != null;
    }

}
