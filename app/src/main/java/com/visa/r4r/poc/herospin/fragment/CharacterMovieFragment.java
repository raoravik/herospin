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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.visa.r4r.poc.herospin.R;
import com.visa.r4r.poc.herospin.marvel.utils.Constants;
import com.visa.r4r.poc.herospin.tmdb.model.Movie;
import com.visa.r4r.poc.herospin.tmdb.model.MoviesResponse;
import com.visa.r4r.poc.herospin.tmdb.rest.TmdbApiClient;
import com.visa.r4r.poc.herospin.tmdb.rest.TmdbInterface;
import com.visa.r4r.poc.herospin.utils.AsyncTaskResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CharacterMovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CharacterMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterMovieFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "characterName";

    private String characterName;

    private List<Movie> cachedMovieList= new ArrayList<>();
    private int currentPage=1;
    private int totalPages=1;



    private OnFragmentInteractionListener mListener;
    private Snackbar snackbar;
    private LinearLayout introLayout;
    private LinearLayout movieDetailsLayout;
    private ImageView movieHeaderImage;

    private View.OnClickListener onClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (snackbar!=null) {
                snackbar.dismiss();
            }
            snackbar = Snackbar.make(view, "Finding a "+characterName+" movie for you...", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Action", null).show();
            introLayout.setVisibility(View.GONE);
            new GetCharacterSpecificMovie().execute(characterName);
        }
    };

    public CharacterMovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param characterName Parameter 1.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CharacterMovieFragment newInstance(String characterName) {
        CharacterMovieFragment fragment = new CharacterMovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, characterName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            characterName = getArguments().getString(ARG_PARAM1);
            currentPage=1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_movie, container, false);
        introLayout = (LinearLayout) fragmentView.findViewById(R.id.introLayout);
        movieDetailsLayout = (LinearLayout) fragmentView.findViewById(R.id.movieDetailsLayout);
        movieHeaderImage = (ImageView) fragmentView.findViewById(R.id.movieHeaderImage);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) fragmentView.findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(characterName+" Movies");
        View fabRandomMovie = fragmentView.findViewById(R.id.fabRandomMovie);
        fabRandomMovie.setOnClickListener(onClickListener);
        introLayout.setVisibility(View.GONE);
        movieDetailsLayout.setVisibility(View.VISIBLE);
        cachedMovieList.clear();
        snackbar = Snackbar.make(fragmentView, "Finding a "+characterName+" movie for you...", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Action", null).show();
        new GetCharacterSpecificMovie().execute(characterName);
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int action);
    }

    class GetCharacterSpecificMovie extends AsyncTask<String,Long,AsyncTaskResult<List<Movie>>> {

        @Override
        protected AsyncTaskResult<List<Movie>> doInBackground(String... params) {
            if(cachedMovieList.size()<10) {
                //If Finished through all pages, reset to page 1.
                if(currentPage>totalPages ){
                    currentPage=1;
                }
                TmdbInterface apiService =
                        TmdbApiClient.getClient().create(TmdbInterface.class);

                Call<MoviesResponse> call = apiService.searchMovies(Constants.TMDB_API_KEY,params[0],"en",currentPage,true);
                try {
                    Response<MoviesResponse> response = call.execute();
                    Log.d("SearchMovieAPI", "Response returned: " + response.isSuccessful());
                    if(response.isSuccessful()) {
                        cachedMovieList.addAll(response.body().getResults());
                        totalPages = response.body().getTotalPages();
                        currentPage++;
                    } else {
                        Log.d("SearchMovieAPI", "Response error message returned: " + response.message());
                    }
                } catch (IOException e) {
                    return new AsyncTaskResult<>(e);
                }
            }
            return new AsyncTaskResult<>(cachedMovieList);
        }

        @Override
        protected void onPostExecute(AsyncTaskResult<List<Movie>> asyncTaskResult) {
            List<Movie> movies;
            if ( asyncTaskResult.getError() != null ) {
                // error handling here
                snackbar.dismiss();
                snackbar = Snackbar.make(getView(), "Oops. Error getting data:"+asyncTaskResult.getError().getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", onClickListener).show();
            }  else if ( isCancelled()) {
                // cancel handling here
                snackbar.dismiss();
                snackbar = Snackbar.make(getView(), "The Movie DB API Query cancelled.", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Retry", onClickListener).show();
            } else {
                movies = asyncTaskResult.getResult();
                if(movies==null || movies.size()==0){
                    snackbar.dismiss();
                    snackbar = Snackbar.make(getView(), "No movies found for:"+characterName, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Action", null).show();
                } else {
                    Log.d("MovieAPI","Number of movies: "+movies.size());
                    snackbar.dismiss();
                    int random = new Random().nextInt(movies.size());
                    displayMovie(movies.remove(random));
                }
            }

        }
    }

    private void displayMovie(Movie movie) {
        introLayout.setVisibility(View.GONE);
        movieDetailsLayout.setVisibility(View.VISIBLE);
        Picasso.with(getContext()).load(Constants.TMDB_BASE_IMAGE_URL+movie.getPosterPath()).fit().centerCrop().into(movieHeaderImage);
        TextView tvTitle = (TextView) movieDetailsLayout.findViewById(R.id.tvTitle);
        tvTitle.setText(movie.getTitle());
        TextView tvYear = (TextView) movieDetailsLayout.findViewById(R.id.tvYear);
        tvYear.setText(movie.getReleaseDate());
        TextView tvOverview = (TextView) movieDetailsLayout.findViewById(R.id.tvOverview);
        tvOverview.setText(movie.getOverview());
        TextView tvRating = (TextView) movieDetailsLayout.findViewById(R.id.tvRating);
        tvRating.setText(String.valueOf(movie.getVoteAverage()));
    }
}
