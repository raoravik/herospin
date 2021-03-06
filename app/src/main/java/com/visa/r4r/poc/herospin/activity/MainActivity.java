/*
 * Copyright (c) 2016. Ravi Rao.
 *
 * This file is created as part of VISA POC and  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.visa.r4r.poc.herospin.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.visa.r4r.poc.herospin.R;
import com.visa.r4r.poc.herospin.fragment.BaseFragment;
import com.visa.r4r.poc.herospin.fragment.CharacterDetailsFragment;
import com.visa.r4r.poc.herospin.fragment.CharacterListFragment;
import com.visa.r4r.poc.herospin.fragment.CharacterMovieFragment;
import com.visa.r4r.poc.herospin.fragment.MovieFragment;
import com.visa.r4r.poc.herospin.marvel.model.Character;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentNavigation
        ,CharacterListFragment.OnListFragmentInteractionListener
        ,CharacterDetailsFragment.OnFragmentInteractionListener
        ,MovieFragment.OnFragmentInteractionListener
        ,CharacterMovieFragment.OnFragmentInteractionListener
{
    private BottomBar bottomBar;
    private FragNavController fragNavController;

    //Better convention to properly name the indices what they are in your app
    private final int INDEX_SURPRISE_ME = FragNavController.TAB1;
    private final int INDEX_SUPER_HEROES = FragNavController.TAB2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating);
//        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        List<Fragment> fragments = new ArrayList<>(2);

        fragments.add(MovieFragment.newInstance("",""));
        fragments.add(CharacterListFragment.newInstance(2));
        fragNavController =
                new FragNavController(getSupportFragmentManager(), R.id.container, fragments);

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItems(R.menu.bottombar_menu);
        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.bb_menu_surprise_me:
                        fragNavController.switchTab(INDEX_SURPRISE_ME);
                        break;
                    case R.id.bb_menu_super_heroes:
                        fragNavController.switchTab(INDEX_SUPER_HEROES);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                fragNavController.clearStack();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (fragNavController.getCurrentStack().size() > 1) {
            fragNavController.pop();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        fragNavController.push(fragment);
    }


    @Override
    public void onListFragmentInteraction(Character character) {
        fragNavController.push(CharacterDetailsFragment.newInstance(character));
    }

    @Override
    public void onFragmentInteraction(int action) {
        fragNavController.switchTab(action);
    }

    @Override
    public void onFragmentInteraction(String characterName) {
        //Handle Character Details Fragment Action.
        Log.d("CHARACTERMOVIE","Loading character specific movie screen");
        fragNavController.push(CharacterMovieFragment.newInstance(characterName));
    }
}
