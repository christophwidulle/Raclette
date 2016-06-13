package de.chefkoch.raclette;

import android.content.Context;
import android.util.AttributeSet;
import de.chefkoch.raclette.android.UpdatableViewComposition;
import de.chefkoch.raclette.android.ViewComposition;
import de.chefkoch.raclette.sample.CharacterItemViewModel;
import de.chefkoch.raclette.sample.CharacterViewModel;
import de.chefkoch.raclette.sample.R;
import de.chefkoch.raclette.sample.comp.TestCompViewModel;
import de.chefkoch.raclette.sample.databinding.CharacterItemBinding;
import de.chefkoch.raclette.sample.rest.Character;

/**
 * Created by christophwidulle on 21.05.16.
 */
@Bind(viewModel = CharacterItemViewModel.class, layoutResource = R.layout.character_item)
public class CharacterTile extends UpdatableViewComposition<Character, CharacterItemViewModel, CharacterItemBinding> {

    public CharacterTile(Context context) {
        super(context);
    }

    public CharacterTile(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterTile(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
