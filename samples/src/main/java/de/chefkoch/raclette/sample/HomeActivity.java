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

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import de.chefkoch.raclette.BaseViewModelActivity;
import de.chefkoch.raclette.Bind;
import de.chefkoch.raclette.sample.databinding.HomeActivityBinding;


@Bind(viewModel = HomeViewModel.class, layoutResource = R.layout.home_activity)
public class HomeActivity extends BaseViewModelActivity<HomeViewModel, HomeActivityBinding> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getBinding().drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

}
