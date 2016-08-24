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

package com.visa.r4r.poc.herospin.tmdb.rest;

import com.visa.r4r.poc.herospin.tmdb.model.Movie;
import com.visa.r4r.poc.herospin.tmdb.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ravrarao on 8/21/2016.
 */
public interface TmdbInterface {
    @GET("keyword/{id}/movies")
    Call<MoviesResponse> getMoviesByKeyword(@Path("id") String keywordId, @Query("api_key") String apiKey);

    @GET("keyword/8828-marvel-comic/movies")
    Call<MoviesResponse> getMarvelComicMovies(@Query("api_key") String apiKey,@Query("page") int page);

    @GET("search/movie")
    Call<MoviesResponse> searchMovies(@Query("api_key") String apiKey,
                                      @Query("query") String query,
                                      @Query("language") String language,
                                      @Query("page") int page,
                                      @Query("include_adult") Boolean includeAdult);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey,
                                @Query("append_to_response") String appendToResponse);
}
