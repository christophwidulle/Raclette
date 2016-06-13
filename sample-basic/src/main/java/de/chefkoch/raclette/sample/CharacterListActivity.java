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

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import de.chefkoch.raclette.Bind;
import de.chefkoch.raclette.CharacterTile;
import de.chefkoch.raclette.UpdatableViewModel;
import de.chefkoch.raclette.android.AdapterItemClickListener;
import de.chefkoch.raclette.android.UpdatableViewComposition;
import de.chefkoch.raclette.android.support.BindingAdapter;
import de.chefkoch.raclette.android.support.CompositionBindingAdapter;
import de.chefkoch.raclette.rx.RxUtil;
import de.chefkoch.raclette.rx.android.support.RacletteRxAppCompatActivity;
import de.chefkoch.raclette.sample.databinding.CharacterItemBinding;
import de.chefkoch.raclette.sample.databinding.CharacterlistActivityBinding;
import de.chefkoch.raclette.sample.rest.Character;
import rx.functions.Action1;

import java.util.List;

@Bind(viewModel = CharacterListViewModel.class, layoutResource = R.layout.characterlist_activity)
public class CharacterListActivity extends RacletteRxAppCompatActivity<CharacterListViewModel, CharacterlistActivityBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //todo fixme

        final CompositionBindingAdapter<Character, CharacterItemBinding> adapter = new CompositionBindingAdapter<Character, CharacterItemBinding>() {
            @Override
            protected UpdatableViewComposition<Character, ? extends UpdatableViewModel<Character>, CharacterItemBinding> create() {
                return new CharacterTile(getBaseContext());
            }
        };

       /* final BindingAdapter<Character> adapter = BindingAdapter.builder(R.layout.list_item)
                .withItemBinding(BR.item)
                .withViewModelBinding(getRaclette().getViewModelBindingId(), getViewModel())
                .withItemClickListener(new AdapterItemClickListener<Cha>() {
                })
                .build();
*/

        getBinding().list.setLayoutManager(new LinearLayoutManager(this));
        getBinding().list.setAdapter(adapter);


        getViewModel().characters()
                .compose(RxUtil.<List<Character>>applySchedulers())
                .compose(this.<List<Character>>bindToLifecycle())
                .subscribe(new Action1<List<Character>>() {
                    @Override
                    public void call(List<Character> characters) {
                        adapter.addAll(characters);
                    }
                });

    }
}
