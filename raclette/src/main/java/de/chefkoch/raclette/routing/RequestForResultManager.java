package de.chefkoch.raclette.routing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by christophwidulle on 24.06.16.
 */
public class RequestForResultManager {

    private static final Map<Integer, WeakReference<ResultCallback>> openForResultRequests = new ConcurrentHashMap<>();
    private static final AtomicInteger RequestCodeCounter = new AtomicInteger();


    int register(final ResultCallback resultCallback) {
        final int requestCode = getNextNextResultCode();
        final WeakReference<ResultCallback> callback = wrap(requestCode, resultCallback);
        openForResultRequests.put(requestCode, callback);
        return requestCode;
    }

    public void onDestroy(Integer requestCode) {
        ResultCallback callback = getCallback(requestCode);
        if (callback != null) {
            callback.onCancel();
        }
    }

    void returnResult(Integer requestCode, Bundle values) {
        ResultCallback callback = getCallback(requestCode);
        if (callback != null) {
            callback.onResult(values);
        }
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        ResultCallback callback = getCallback(requestCode);
        if (callback != null) {
            if (resultCode == Activity.RESULT_CANCELED) {
                callback.onCancel();
            } else if (resultCode == Activity.RESULT_OK) {
                callback.onResult(data.getExtras());
            }
        }
    }

    private WeakReference<ResultCallback> wrap(final int resultCode, final ResultCallback resultCallback) {

        final ResultCallback innerResultCallback = new ResultCallback() {
            @Override
            public void onResult(Bundle values) {
                remove(resultCode);
                resultCallback.onResult(values);
            }

            @Override
            public void onCancel() {
                remove(resultCode);
                resultCallback.onCancel();
            }
        };
        return new WeakReference<>(innerResultCallback);
    }

    private int getNextNextResultCode() {
        return RequestCodeCounter.addAndGet(1);
    }


    private ResultCallback getCallback(Integer resultCode) {
        if (resultCode == null) return null;
        final WeakReference<ResultCallback> callback = openForResultRequests.get(resultCode);
        if (callback != null) {
            return callback.get();
        } else {
            return null;
        }
    }

    private void remove(int resultCode) {
        openForResultRequests.remove(resultCode);
    }


}
