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
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
import com.visa.r4r.poc.herospin.utils.AsyncTaskResult;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CharacterListFragment extends BaseFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "CharactersList";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private Snackbar snackbar;
    private View.OnClickListener onClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (snackbar!=null) {
                snackbar.dismiss();
            }
            snackbar = Snackbar.make(view, "Loading Marvel Characters...", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Action", null).show();
            new GetAllCharactersTask().execute();
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharacterListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CharacterListFragment newInstance(int columnCount) {
        CharacterListFragment fragment = new CharacterListFragment();
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
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        // Set the adapter
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(mColumnCount, OrientationHelper.VERTICAL));
            }
        snackbar = Snackbar.make(view, "Loading Marvel Characters...", Snackbar.LENGTH_INDEFINITE);
        new GetAllCharactersTask().execute();
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

    class GetAllCharactersTask extends AsyncTask<Void,Long,AsyncTaskResult<List<Character>>> {

        @Override
        protected AsyncTaskResult<List<Character>> doInBackground(Void... params) {
            MarvelApiConfig marvelApiConfig =
                    new MarvelApiConfig.Builder(Constants.MARVEL_PUBLIC_KEY, Constants.MARVEL_PRIVATE_KEY).debug().build();
            CharacterApiClient characterApiClient = new CharacterApiClient(marvelApiConfig);
            MarvelResponse<Characters> charactersMarvelResponse = null;
            try {
                charactersMarvelResponse = characterApiClient.getAll(0, 100);
            } catch (MarvelApiException e) {
                return new AsyncTaskResult<>(e);
            }
            return new AsyncTaskResult<>(charactersMarvelResponse.getResponse().getCharacters());
        }

        @Override
        protected void onPostExecute(AsyncTaskResult<List<Character>> asyncTaskResult) {
            if ( asyncTaskResult.getError() != null ) {
                // error handling here
                snackbar.dismiss();
                snackbar = Snackbar.make(getView(), "Oops. Error getting data from Marvel:"+asyncTaskResult.getError().getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", onClickListener).show();
            }  else if ( isCancelled()) {
                // cancel handling here
                snackbar.dismiss();
                snackbar = Snackbar.make(getView(), "Marvel API Query cancelled.", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Retry", onClickListener).show();
            } else {
                List<Character> characters = asyncTaskResult.getResult();
                if(characters==null || characters.size()==0){
                    snackbar.dismiss();
                    snackbar = Snackbar.make(getView(), "No Marvel Characters found... ", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Action", null).show();
                } else {
                    Log.d("MovieAPI","Number of characters: "+characters.size());
                    snackbar.dismiss();
                    recyclerView.setAdapter(new CharacterRecyclerViewAdapter(characters, mListener));
                }
            }
        }
    }

}

