package de.chefkoch.raclette;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by christophwidulle on 15.04.16.
 */
public class ContextManager implements ContextProvider {

    private WeakReference<Context> currentContext;

    @Override
    public Context getCurrentContext() {
        return currentContext != null ? currentContext.get() : null;
    }

    void setCurrentContext(Context currentContext) {
        final Context previousContext = getCurrentContext();
        if (previousContext != null && previousContext == currentContext) return;

        this.currentContext = new WeakReference<Context>(currentContext);
    }

    void clear() {
        currentContext = null;
    }

}
