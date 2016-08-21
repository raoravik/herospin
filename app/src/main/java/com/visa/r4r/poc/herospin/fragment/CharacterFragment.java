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

package com.visa.r4r.poc.herospin.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visa.r4r.poc.herospin.R;
import com.visa.r4r.poc.herospin.marvel.model.Character;
import com.visa.r4r.poc.herospin.marvel.model.Characters;
import com.visa.r4r.poc.herospin.marvel.model.MarvelResponse;
import com.visa.r4r.poc.herospin.marvel.rest.CharacterApiClient;
import com.visa.r4r.poc.herospin.marvel.rest.MarvelApiConfig;
import com.visa.r4r.poc.herospin.marvel.rest.MarvelApiException;
import com.visa.r4r.poc.herospin.marvel.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CharacterFragment extends BaseFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "CharactersList";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharacterFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CharacterFragment newInstance(int columnCount) {
        CharacterFragment fragment = new CharacterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            new GetAllCharactersTask().execute();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Character character);
    }

    class GetAllCharactersTask extends AsyncTask<Void,Long,List<Character>> {

        @Override
        protected List<Character> doInBackground(Void... params) {
            MarvelApiConfig marvelApiConfig =
                    new MarvelApiConfig.Builder(Constants.MARVEL_PUBLIC_KEY, Constants.MARVEL_PRIVATE_KEY).debug().build();
            CharacterApiClient characterApiClient = new CharacterApiClient(marvelApiConfig);
            MarvelResponse<Characters> charactersMarvelResponse = null;
            try {
                charactersMarvelResponse = characterApiClient.getAll(0, 100);
            } catch (MarvelApiException e) {
                e.printStackTrace();
            }

            if (charactersMarvelResponse != null) {
                return charactersMarvelResponse.getResponse().getCharacters();
            } else {
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(List<Character> characters) {
            recyclerView.setAdapter(new CharacterRecyclerViewAdapter(characters, mListener));
        }
    }

}

