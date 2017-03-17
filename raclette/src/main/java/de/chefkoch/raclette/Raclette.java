package de.chefkoch.raclette;

import de.chefkoch.raclette.routing.NavigationController;
import de.chefkoch.raclette.routing.NavigationControllerImpl;
import de.chefkoch.raclette.routing.RequestForResultManager;

/**
 * Created by christophwidulle on 12.04.16.
 */
public class Raclette {

    private static final int NO_VIEWMODEL_BINDING_ID = -1;

    private static Raclette INSTANCE;
    private static final Object LOCK = new Object();

    private int viewModelBindingId;
    private final ViewModelInjector viewModelInjector;
    private final ViewModelManager viewModelManager;
    private final ContextManager contextManager;
    private final RequestForResultManager requestForResultManager;

    Raclette(int viewModelBindingId,
             ViewModelInjector viewModelInjector,
             ViewModelManager viewModelManager,
             ContextManager contextManager,
             RequestForResultManager requestForResultManager) {

        this.viewModelBindingId = viewModelBindingId;
        this.viewModelInjector = viewModelInjector;
        this.viewModelManager = viewModelManager;
        this.contextManager = contextManager;
        this.requestForResultManager = requestForResultManager;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Raclette get() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            throw new RacletteException("Raclette not build.");
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

    public NavigationController createNavigationController() {
        return new NavigationControllerImpl(requestForResultManager);
    }


    public static class Builder {
        int viewModelBindingId = NO_VIEWMODEL_BINDING_ID;
        ViewModelInjector viewModelInjector;
        ViewModelFactory factory;
        ViewModelIdGenerator viewModelIdGenerator = new ViewModelIdGenerator.Default();

        public Builder viewModelIdGenerator(ViewModelIdGenerator viewModelIdGenerator) {
            this.viewModelIdGenerator = viewModelIdGenerator;
            return this;
        }

        public Builder viewModelInjector(ViewModelInjector injector) {
            this.viewModelInjector = injector;
            return this;
        }

        public Builder viewModelFactory(ViewModelFactory factory) {
            this.factory = factory;
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
                    new ViewModelManager(viewModelInjector, factory, viewModelIdGenerator),
                    contextManager,
                    new RequestForResultManager());
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
