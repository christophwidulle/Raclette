package de.chefkoch.raclette.sample.comp;

import android.content.Context;
import android.util.AttributeSet;
import de.chefkoch.raclette.Bind;
import de.chefkoch.raclette.android.ViewComposition;
import de.chefkoch.raclette.routing.Nav;
import de.chefkoch.raclette.sample.R;

/**
 * Created by christophwidulle on 21.05.16.
 */
@Bind(viewModel = TestCompViewModel.class, layoutResource = R.layout.test_comp_view)
public class TestComp extends ViewComposition {

    public TestComp(Context context) {
        super(context);
    }

    public TestComp(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestComp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Nav.Param
    public String id;
}
