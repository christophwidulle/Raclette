/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.chefkoch.raclette.sample;

import android.content.Intent;
import de.chefkoch.raclette.Bind;
import de.chefkoch.raclette.R;
import de.chefkoch.raclette.TestParameter;
import de.chefkoch.raclette.android.support.BindingAdapter;
import de.chefkoch.raclette.routing.Nav;
import de.chefkoch.raclette.routing.NavRequest;
import de.chefkoch.raclette.routing.NavRequestInterceptor;
import de.chefkoch.raclette.rx.android.support.RacletteRxAppCompatActivity;
import de.chefkoch.raclette.sample.databinding.CharacterActivityBinding;


@Nav.Route(value = "/character", returns = @Nav.Result(value = "selectedCharacterTest", type = TestParameter.class))

/*
@Nav.Dispatch({
        @Nav.Route(value = "/character/info", navParams = CharacterParams.class),
        @Nav.Route(value = "/character/bla", navParams = CharacterParams.class)
}
)*/
@Bind(viewModel = CharacterViewModel.class, layoutResource = R.layout.character_activity)
public class CharacterActivity extends RacletteRxAppCompatActivity<CharacterViewModel, CharacterActivityBinding> implements NavRequestInterceptor {





    @Override
    public boolean onHandle(NavRequest navRequest) {





        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
