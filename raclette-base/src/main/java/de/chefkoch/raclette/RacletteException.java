package de.chefkoch.raclette;

/**
 * Created by Christoph on 05.10.2015.
 */
public class RacletteException extends RuntimeException {


    public RacletteException() {
        super();
    }

    public RacletteException(String detailMessage) {
        super(detailMessage);
    }

    public RacletteException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RacletteException(Throwable throwable) {
        super(throwable);
    }
}
