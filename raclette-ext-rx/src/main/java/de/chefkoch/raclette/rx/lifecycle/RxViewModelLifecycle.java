package de.chefkoch.raclette.rx.lifecycle;

import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * thx to https://github.com/trello/RxLifecycle
 *
 * Created by christophwidulle on 15.04.16.
 */
public class RxViewModelLifecycle {

    public static <T> Observable.Transformer<T, T> bindUntilEvent(final Observable<ViewModelLivecycleEvent> lifecycle, final ViewModelLivecycleEvent event) {
        return bind(lifecycle, event);
    }

    private static <T, R> Observable.Transformer<T, T> bind(final Observable<R> lifecycle, final R event) {
        if (lifecycle == null) {
            throw new IllegalArgumentException("Lifecycle must be given");
        } else if (event == null) {
            throw new IllegalArgumentException("Event must be given");
        }

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> source) {
                return source.takeUntil(
                        lifecycle.takeFirst(new Func1<R, Boolean>() {
                            @Override
                            public Boolean call(R lifecycleEvent) {
                                return lifecycleEvent == event;
                            }
                        })
                );
            }
        };
    }

    public static <T> Observable.Transformer<T, T> bind(Observable<ViewModelLivecycleEvent> lifecycle) {
        return bind(lifecycle, VIEWMODEL_LIFECYCLE);
    }

    private static <T, R> Observable.Transformer<T, T> bind(Observable<R> lifecycle,
                                                            final Func1<R, R> correspondingEvents) {
        if (lifecycle == null) {
            throw new IllegalArgumentException("Lifecycle must be given");
        }

        // Make sure we're truly comparing a single stream to itself
        final Observable<R> sharedLifecycle = lifecycle.share();

        // Keep emitting from source until the corresponding event occurs in the lifecycle
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> source) {
                return source.takeUntil(
                        Observable.combineLatest(
                                sharedLifecycle.take(1).map(correspondingEvents),
                                sharedLifecycle.skip(1),
                                new Func2<R, R, Boolean>() {
                                    @Override
                                    public Boolean call(R bindUntilEvent, R lifecycleEvent) {
                                        return lifecycleEvent == bindUntilEvent;
                                    }
                                })
                                .onErrorReturn(RESUME_FUNCTION)
                                .takeFirst(SHOULD_COMPLETE)
                );
            }
        };
    }

    private static final Func1<Throwable, Boolean> RESUME_FUNCTION = new Func1<Throwable, Boolean>() {
        @Override
        public Boolean call(Throwable throwable) {
            if (throwable instanceof OutsideLifecycleException) {
                return true;
            }

            Exceptions.propagate(throwable);
            return false;
        }
    };

    private static final Func1<Boolean, Boolean> SHOULD_COMPLETE = new Func1<Boolean, Boolean>() {
        @Override
        public Boolean call(Boolean shouldComplete) {
            return shouldComplete;
        }
    };

    // Figures out which corresponding next lifecycle event in which to unsubscribe
    private static final Func1<ViewModelLivecycleEvent, ViewModelLivecycleEvent> VIEWMODEL_LIFECYCLE =
            new Func1<ViewModelLivecycleEvent, ViewModelLivecycleEvent>() {
                @Override
                public ViewModelLivecycleEvent call(ViewModelLivecycleEvent lastEvent) {
                    switch (lastEvent) {
                        case VIEWMODEL_CREATE:
                            return ViewModelLivecycleEvent.VIEWMODEL_DESTROY;
                        case CREATE:
                            return ViewModelLivecycleEvent.DESTROY;
                        case START:
                            return ViewModelLivecycleEvent.STOP;
                        case RESUME:
                            return ViewModelLivecycleEvent.PAUSE;
                        case PAUSE:
                            return ViewModelLivecycleEvent.STOP;
                        case STOP:
                            return ViewModelLivecycleEvent.DESTROY;
                        case DESTROY:
                            return ViewModelLivecycleEvent.VIEWMODEL_DESTROY;
                        case VIEWMODEL_DESTROY:
                            throw new OutsideLifecycleException("Cannot bind to ViewModel lifecycle when outside of it.");
                        default:
                            throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
                    }
                }
            };


    private static class OutsideLifecycleException extends IllegalStateException {
        public OutsideLifecycleException(String detailMessage) {
            super(detailMessage);
        }
    }

}
