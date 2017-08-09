package de.chefkoch.raclette.rx;

import de.chefkoch.raclette.routing.ResultValue;

/**
 * Created by christophwidulle on 25.06.16.
 */
public class ForResultReturn {

    ResultValue result;

    private ForResultReturn(ResultValue result) {
        this.result = result;
    }

    private ForResultReturn() {

    }

    public static ForResultReturn from(ResultValue result) {
        return new ForResultReturn(result);
    }

    public static ForResultReturn canceled() {
        return new ForResultReturn();
    }

    public ResultValue getResult() {
        return result;
    }

    public boolean isCanceled() {
        return result == null;
    }

    public boolean hasResult() {
        return result != null;
    }

}
