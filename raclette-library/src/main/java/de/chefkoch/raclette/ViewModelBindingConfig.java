package de.chefkoch.raclette;

import android.support.annotation.LayoutRes;


public class ViewModelBindingConfig<V extends ViewModel> {

    Class<V> viewModelClass;
    @LayoutRes
    int layoutResource;


    public ViewModelBindingConfig(Class<V> viewModelClass, @LayoutRes int layoutResource) {
        this.viewModelClass = viewModelClass;
        this.layoutResource = layoutResource;
    }

    public int getLayoutResource() {
        return layoutResource;
    }


    public Class<V> getViewModelClass() {
        return viewModelClass;
    }


    @SuppressWarnings("unchecked")
    public static <V extends ViewModel> ViewModelBindingConfig<V> of(Class<?> klass) {
        Bind bindAnnotation = klass.getAnnotation(Bind.class);
        if (bindAnnotation != null) {
            Class<? extends ViewModel> viewModelClass = bindAnnotation.viewModel();
            final int layoutResource = bindAnnotation.layoutResource();
            if (viewModelClass == null) {
                throw new RacletteException("No ViewModel specified.");
            }
            return new ViewModelBindingConfig<V>((Class<V>) viewModelClass, layoutResource);
        } else {
            throw new RacletteException("No @Bind annotation found.");
        }
    }


}
