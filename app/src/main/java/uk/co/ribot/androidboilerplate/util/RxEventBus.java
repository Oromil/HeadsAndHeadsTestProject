package uk.co.ribot.androidboilerplate.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * A simple event bus built with RxJava
 */
@Singleton
public class RxEventBus {

    private final BackpressureStrategy mBackpressureStrategy = BackpressureStrategy.BUFFER;
    private final BehaviorSubject<Object> mBusSubject;

    @Inject
    public RxEventBus() {
        mBusSubject = BehaviorSubject.create();
        mBusSubject.onNext("");
    }

    /**
     * Posts an object (usually an Event) to the bus
     */
    public void post(Object event) {
        mBusSubject.onNext(event);
    }
    public void signInEvent(String username) {
        mBusSubject.onNext(username);
    }

    /**
     * Observable that will emmit everything posted to the event bus.
     */
    public Flowable<Object> observable() {
        return mBusSubject.toFlowable(mBackpressureStrategy);
    }

    /**
     * Observable that only emits events of a specific class.
     * Use this if you only want to subscribe to one type of events.
     */
    public <T> Flowable<T> filteredObservable(final Class<T> eventClass) {
        return mBusSubject.ofType(eventClass).toFlowable(mBackpressureStrategy);
    }

}
