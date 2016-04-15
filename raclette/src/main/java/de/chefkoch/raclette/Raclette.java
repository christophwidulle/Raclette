package de.chefkoch.raclette;

/**
 * Created by christophwidulle on 12.04.16.
 */
public class Raclette {

    public static final int NO_VIEWMODEL_BINDING_ID = -1;

    private static Raclette INSTANCE;
    private static final Object LOCK = new Object();

    int viewModelBindingId;
    ViewModelInjector viewModelInjector;
    ViewModelManager viewModelManager;

    private Raclette(int viewModelBindingId, ViewModelInjector viewModelInjector, ViewModelManager viewModelManager) {
        this.viewModelBindingId = viewModelBindingId;
        this.viewModelInjector = viewModelInjector;
        this.viewModelManager = viewModelManager;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Raclette get() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            throw new RacletteException("Raclette not build asSingelton.");
        }
    }


    public int getViewModelBindingId() {
        return viewModelBindingId;
    }

    public ViewModelInjector getViewModelInjector() {
        return viewModelInjector;
    }

    public ViewModelManager getViewModelManager() {
        return viewModelManager;
    }

    public interface ViewModelInjector<T extends ViewModel> {
        void inject(T viewModel);
    }


    public static class Builder {
        int viewModelBindingId = NO_VIEWMODEL_BINDING_ID;
        ViewModelInjector viewModelInjector;

        public Builder viewModelInjector(ViewModelInjector injector) {
            this.viewModelInjector = injector;
            return this;
        }

        public Builder viewModelBindingId(int viewModelBindingId) {
            this.viewModelBindingId = viewModelBindingId;
            return this;
        }

        public Raclette build() {
            if (viewModelBindingId == NO_VIEWMODEL_BINDING_ID) {
                throw new RacletteException("ViewModelBindingId not set.");
            }
            return new Raclette(viewModelBindingId, viewModelInjector, new ViewModelManager(viewModelInjector));
        }

        public Raclette buildAsSingelton() {
            if (Raclette.INSTANCE == null) {
                synchronized (LOCK) {
                    if (Raclette.INSTANCE == null) {
                        Raclette.INSTANCE = build();
                    }
                }
            }
            return Raclette.INSTANCE;
        }

    }

}
