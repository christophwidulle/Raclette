package de.chefkoch.raclette;

import de.chefkoch.raclette.routing.NavigationController;

/**
 * Created by christophwidulle on 12.04.16.
 */
public class Raclette {

    private static final int NO_VIEWMODEL_BINDING_ID = -1;
    public static final String BUNDLE_PREFIX = "##RACLETTE_";

    private static Raclette INSTANCE;
    private static final Object LOCK = new Object();

    private int viewModelBindingId;
    private final ViewModelInjector viewModelInjector;
    private final ViewModelManager viewModelManager;
    private final ContextManager contextManager;
    private final NavigationController navigationController;

    Raclette(int viewModelBindingId,
             ViewModelInjector viewModelInjector,
             ViewModelManager viewModelManager,
             ContextManager contextManager,
             NavigationController navigationController) {
        this.viewModelBindingId = viewModelBindingId;
        this.viewModelInjector = viewModelInjector;
        this.viewModelManager = viewModelManager;
        this.contextManager = contextManager;
        this.navigationController = navigationController;
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

    public ContextManager getContextManager() {
        return contextManager;
    }

    public NavigationController getNavigationController() {
        return navigationController;
    }

    public interface ViewModelInjector {
        void inject(ViewModel viewModel);
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
            ContextManager contextManager = new ContextManager();
            return new Raclette(viewModelBindingId,
                    viewModelInjector,
                    new ViewModelManager(viewModelInjector),
                    contextManager,
                    new NavigationController(contextManager));
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
