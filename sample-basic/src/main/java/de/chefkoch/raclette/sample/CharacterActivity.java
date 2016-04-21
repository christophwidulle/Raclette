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

import de.chefkoch.raclette.Bind;
import de.chefkoch.raclette.routing.Nav;
import de.chefkoch.raclette.routing.NavRequest;
import de.chefkoch.raclette.routing.NavRouteHandler;
import de.chefkoch.raclette.rx.android.support.RacletteRxAppCompatActivity;
import de.chefkoch.raclette.sample.databinding.CharacterActivityBinding;


@Nav.Route(value = "/character", navParams = CharacterParams.class)
@Nav.Dispatch({
        @Nav.Route(value = "/character/info", navParams = CharacterParams.class),
        @Nav.Route(value = "/character/bla", navParams = CharacterParams.class)
}
)
@Bind(viewModel = CharacterViewModel.class, layoutResource = R.layout.character_activity)
public class CharacterActivity extends RacletteRxAppCompatActivity<CharacterViewModel, CharacterActivityBinding> implements NavRouteHandler{


    @Override
    public void onHandle(NavRequest navRequest) {
        System.out.println();
    }
}
