package de.chefkoch.raclette;

/**
 * Created by Christoph on 05.10.2015.
 */
public class MvvmException extends RuntimeException {


    public MvvmException() {
        super();
    }

    public MvvmException(String detailMessage) {
        super(detailMessage);
    }

    public MvvmException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MvvmException(Throwable throwable) {
        super(throwable);
    }
}
