package de.chefkoch.raclette.rx.event;

import com.jakewharton.rxrelay.BehaviorRelay;
import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.Relay;
import rx.Observable;

/**
 * Created by christophwidulle on 17.04.16.
 */
public class RxEventBus<T> {

    private final Relay<T, T> bus;

    private RxEventBus(Relay<T, T> bus) {
        this.bus = bus.toSerialized();
    }

    void fire(T event) {
        bus.call(event);
    }

    Observable<T> subscripe(Class<T> eventType) {
        return bus.asObservable().ofType(eventType);
    }

    Observable<T> subscripe() {
        return bus.asObservable();

    }

    static <T> RxEventBus<T> createPublish() {
        return new RxEventBus<>(PublishRelay.<T>create());
    }

    static <T> RxEventBus<T> createBehavior() {
        return new RxEventBus<>(BehaviorRelay.<T>create());
    }
}
