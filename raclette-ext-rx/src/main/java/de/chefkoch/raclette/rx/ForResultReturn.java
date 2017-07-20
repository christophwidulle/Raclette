package de.chefkoch.raclette.rx;

import de.chefkoch.raclette.routing.Result;

/**
 * Created by christophwidulle on 25.06.16.
 */
public class ForResultReturn {

    Result result;

    private ForResultReturn(Result result) {
        this.result = result;
    }

    private ForResultReturn() {

    }

    public static ForResultReturn from(Result result) {
        return new ForResultReturn(result);
    }

    public static ForResultReturn canceled() {
        return new ForResultReturn();
    }

    public Result getResult() {
        return result;
    }

    public boolean isCanceled() {
        return result == null;
    }

    public boolean hasResult() {
        return result != null;
    }

}
