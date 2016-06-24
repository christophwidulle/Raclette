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
public class ForResultSupport {

    private static final Map<Integer, WeakReference<ResultCallback>> openForResultRequests = new ConcurrentHashMap<>();
    private static final AtomicInteger resultCode = new AtomicInteger();

    private Integer currentResultCode;


    int register(final ResultCallback resultCallback) {
        final int resultcode = getNextNextResultCode();
        final WeakReference<ResultCallback> callback = wrap(resultcode, resultCallback);
        openForResultRequests.put(resultcode, callback);
        return resultcode;
    }

    public void onDestroy() {
        if (hasResultCode()) {
            ResultCallback callback = getCallback(currentResultCode);
            if (callback != null) {
                callback.onCancel();
            }
        }
    }

    void returnResult(Bundle values) {
        if (hasResultCode()) {
            ResultCallback callback = getCallback(currentResultCode);
            if (callback != null) {
                callback.onResult(values);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (hasResultCode()) {
            ResultCallback callback = getCallback(requestCode);
            if (callback != null) {
                if (resultCode == Activity.RESULT_CANCELED) {
                    callback.onCancel();
                } else if (resultCode == Activity.RESULT_OK) {
                    callback.onResult(data.getExtras());
                }
            }
        }
    }

    void setCurrentResultCode(Integer currentResultCode) {
        this.currentResultCode = currentResultCode;
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
        return resultCode.addAndGet(1);
    }

    private boolean hasResultCode() {
        return currentResultCode != null;
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
