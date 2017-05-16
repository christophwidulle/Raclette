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

    public void fire(T event) {
        bus.call(event);
    }

    public Observable<? extends T> observe(Class<? extends T> eventType) {
        return observe().ofType(eventType);
    }

    public Observable<? extends T> observe() {
        return bus.asObservable().onBackpressureBuffer();

    }

    public static <T> RxEventBus<T> createPublish() {
        return new RxEventBus<>(PublishRelay.<T>create());
    }

    //todo not sticky for each event
    public static <T> RxEventBus<T> createBehavior() {
        return new RxEventBus<>(BehaviorRelay.<T>create());
    }
}
