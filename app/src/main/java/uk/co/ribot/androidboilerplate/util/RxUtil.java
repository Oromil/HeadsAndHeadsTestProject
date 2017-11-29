package uk.co.ribot.androidboilerplate.util;

import org.reactivestreams.Subscription;

import io.reactivex.disposables.Disposable;

public class RxUtil {

    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public static void cancel(Subscription s){
        if (s !=null){
            s.cancel();
        }
    }

}
