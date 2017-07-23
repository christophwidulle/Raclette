package de.chefkoch.raclette.routing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by christophwidulle on 24.06.16.
 */
public class RequestForResultManager {

    private static final Map<Integer, ResultCallback> openForResultRequests = new ConcurrentHashMap<>();
    private static final AtomicInteger RequestCodeCounter = new AtomicInteger();


    int register(final ResultCallback resultCallback) {
        final int requestCode = getNextNextResultCode();
        openForResultRequests.put(requestCode, wrap(requestCode, resultCallback));
        return requestCode;
    }

    public void onDestroy(Integer requestCode) {
        if (requestCode != null) {
            ResultCallback callback = getCallback(requestCode);
            if (callback != null) {
                callback.onCancel();
            }
        }
    }

    void cancel(Integer requestCode) {
        if (requestCode != null) {
            remove(requestCode);
        }
    }

    void returnResult(Integer requestCode, Result result) {
        ResultCallback callback = getCallback(requestCode);
        if (callback != null) {
            callback.onResult(result);
        }
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        ResultCallback callback = getCallback(requestCode);
        if (callback != null) {
            if (resultCode == Activity.RESULT_CANCELED) {
                callback.onCancel();
            } else if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    callback.onResult(new Result(data.getExtras(), data.getData()));
                } else {
                    callback.onResult(new Result(null, null));
                }
            }
        }
    }

    private ResultCallback wrap(final int requestCode, final ResultCallback resultCallback) {

        return new ResultCallback() {
            @Override
            public void onResult(Result result) {
                remove(requestCode);
                resultCallback.onResult(result);
            }

            @Override
            public void onCancel() {
                remove(requestCode);
                resultCallback.onCancel();
            }
        };
    }

    private int getNextNextResultCode() {
        return RequestCodeCounter.addAndGet(1);
    }


    private ResultCallback getCallback(Integer resultCode) {
        if (resultCode == null) {
            return null;
        } else {
            return openForResultRequests.get(resultCode);
        }
    }

    private void remove(int requestCode) {
        openForResultRequests.remove(requestCode);
    }


}
