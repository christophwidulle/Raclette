package de.chefkoch.raclette;

/**
 * Created by christophwidulle on 12.04.16.
 */
public class Raclette {

    protected static Raclette INSTANCE;
    protected static final Object LOCK = new Object();

    int viewModelBindingId;
    ViewModelInjector viewModelInjector;
    ViewModelManager viewModelManager;

    Raclette(int viewModelBindingId, ViewModelInjector viewModelInjector, ViewModelManager viewModelManager) {
        this.viewModelBindingId = viewModelBindingId;
        this.viewModelInjector = viewModelInjector;
        this.viewModelManager = viewModelManager;
    }

    public static Builder builder(int viewModelBindingId) {
        return new Builder(viewModelBindingId);
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

    public interface ViewModelInjector {
        void inject(ViewModel viewModel);
    }


    public static class Builder {
        int viewModelBindingId;
        ViewModelInjector viewModelInjector;

        public Builder(int viewModelBindingId) {
            this.viewModelBindingId = viewModelBindingId;
        }

        public Builder viewModelInjector(ViewModelInjector injector) {
            this.viewModelInjector = injector;
            return this;
        }

        public Raclette build() {
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
