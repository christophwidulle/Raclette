package de.chefkoch.raclette;

import java.util.UUID;

/**
 * Created by christophwidulle on 17.03.17.
 */
public interface ViewModelIdGenerator {

    String createId();

    public static class Default implements ViewModelIdGenerator {

        @Override
        public String createId() {
            return UUID.randomUUID().toString();
        }
    }
}
