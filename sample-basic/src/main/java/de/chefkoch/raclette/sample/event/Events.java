package de.chefkoch.raclette.sample.event;

import de.chefkoch.raclette.rx.event.RxEventBus;
import rx.Observable;

/**
 * Created by christophwidulle on 27.04.16.
 */
public class Events {


    private RxEventBus<Event> rxEventBus;

    public Events() {
        rxEventBus = RxEventBus.createPublish();
    }

    public <T extends Event> void fire(Event event) {
        rxEventBus.fire(event);
    }

    public Observable<? extends Event> subscripe(Class<? extends Event> eventType) {
        return rxEventBus.observe(eventType);
    }

    public Observable<? extends Event> subscripe() {
        return rxEventBus.observe();

    }

}
