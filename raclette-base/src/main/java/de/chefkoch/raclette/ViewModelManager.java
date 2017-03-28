package de.chefkoch.raclette;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by christophwidulle on 01.10.15.
 */
public class ViewModelManager {

    private final ViewModelInjector injector;
    private final ViewModelFactory factory;
    private final ViewModelIdGenerator viewModelIdGenerator;

    ViewModelManager(ViewModelInjector injector, ViewModelFactory factory, ViewModelIdGenerator viewModelIdGenerator) {
        this.injector = injector;
        this.factory = factory;
        this.viewModelIdGenerator = viewModelIdGenerator;
    }

    private Map<String, ViewModel> registry = new ConcurrentHashMap<>();

    <V extends ViewModel> V createViewModel(Class<V> viewModelClass) {
        try {
            V viewModel = newViewModel(viewModelClass);
            String id = viewModelIdGenerator.createId();
            viewModel.setId(id);
            if (injector != null) {
                injector.inject(viewModel);
            }
            registry.put(id, viewModel);

            return viewModel;
        } catch (Exception e) {
            throw new RacletteException(e);
        }
    }

    private <V extends ViewModel> V newViewModel(Class<V> viewModelClass) {
        try {
            V instance = null;
            if (factory != null) {
                instance = (V) factory.create(viewModelClass);
            }
            if (instance == null) {
                instance = viewModelClass.newInstance();
            }
            return instance;
        } catch (Exception e) {
            throw new RacletteException(e);
        }
    }

    void delete(String id) {
        registry.remove(id);
    }

    @SuppressWarnings("unchecked")
    public <V extends ViewModel> V getViewModel(String id) {
        ViewModel viewModel = registry.get(id);
        return (V) viewModel;
    }

    public List<ViewModel> getAll() {
        return new ArrayList<>(registry.values());
    }


}
