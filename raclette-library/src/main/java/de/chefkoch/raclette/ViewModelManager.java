package de.chefkoch.raclette;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by christophwidulle on 01.10.15.
 */
public class ViewModelManager {


    private final Injector injector;

    public ViewModelManager(Injector injector) {
        this.injector = injector;
    }

    Map<String, ViewModel> registry = new ConcurrentHashMap<>();

    public <V extends ViewModel> V createViewModel(Class<V> viewModelClass) {

        String id = UUID.randomUUID().toString();
        try {
            V viewModel = viewModelClass.newInstance();
            viewModel.setId(id);
            injector.inject(viewModel);
            registry.put(id, viewModel);
            return viewModel;
        } catch (Exception e) {
            throw new MvvmException(e);
        }
    }

    public void delete(String id) {
        registry.remove(id);
    }

    @SuppressWarnings("unchecked")
    public <V extends ViewModel> V getViewModel(String id) {
        ViewModel viewModel = registry.get(id);
        return (V) viewModel;
    }

    public interface Injector {
        void inject(ViewModel viewModel);
    }

}
