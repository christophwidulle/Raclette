package de.chefkoch.raclette;

/**
 * @author Christoph Widulle
 */
public interface ViewModelFactory<T extends ViewModel> {

    T create(Class<T> viewModelType);

}
