package de.chefkoch.raclette;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by christophwidulle on 15.04.16.
 */
public class ContextManager implements ContextProvider{

    WeakReference<Context> currentContext;

    void setCurrentContext(Context currentContext) {
        this.currentContext = new WeakReference<Context>(currentContext);
    }

    void clear(){
        currentContext = null;
    }

    @Override
    public Context getCurrentContext() {
        return currentContext != null ? currentContext.get() : null;
    }
}
