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

package com.visa.r4r.poc.herospin.marvel.rest;

import com.visa.r4r.poc.herospin.marvel.model.Characters;
import com.visa.r4r.poc.herospin.marvel.model.MarvelResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface CharacterApiRest {
  @GET("characters") Call<MarvelResponse<Characters>> getCharacters(
          @QueryMap Map<String, Object> characterFilter);

  @GET("characters/{id}") Call<MarvelResponse<Characters>> getCharacter(
          @Path("id") String characterId);
}
