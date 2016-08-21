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

import com.visa.r4r.poc.herospin.marvel.model.Character;
import com.visa.r4r.poc.herospin.marvel.model.Characters;
import com.visa.r4r.poc.herospin.marvel.model.CharactersQuery;
import com.visa.r4r.poc.herospin.marvel.model.MarvelResponse;

import java.util.Map;

import retrofit2.Call;


/**
 * Retrieves Character information given a  {@link CharactersQuery} or some simple params like the
 * character id. A valid {@link MarvelApiConfig} is needed.
 */
public final class CharacterApiClient extends MarvelApiClient {

  public CharacterApiClient(MarvelApiConfig marvelApiConfig) {
    super(marvelApiConfig);
  }

  public MarvelResponse<Characters> getAll(int offset, int limit) throws MarvelApiException {
    CharactersQuery query =
        CharactersQuery.Builder.create().withOffset(offset).withLimit(limit).build();
    return getAll(query);
  }

  public MarvelResponse<Characters> getAll(CharactersQuery charactersQuery)
      throws MarvelApiException {
    CharacterApiRest api = getApi(CharacterApiRest.class);

    Map<String, Object> queryAsMap = charactersQuery.toMap();
    Call<MarvelResponse<Characters>> call = api.getCharacters(queryAsMap);
    return execute(call);
  }

  public MarvelResponse<Character> getCharacter(String characterId) throws MarvelApiException {
    if (characterId == null || characterId.isEmpty()) {
      throw new IllegalArgumentException("The CharacterId must not be null or empty");
    }
    CharacterApiRest api = getApi(CharacterApiRest.class);

    Call<MarvelResponse<Characters>> call = api.getCharacter(characterId);
    MarvelResponse<Characters> characters = execute(call);
    Characters charactersDto = characters.getResponse();
    if (charactersDto != null && charactersDto.getCount() > 0) {
      Character character = charactersDto.getCharacters().get(0);
      MarvelResponse<Character> characterResponse = new MarvelResponse<>(characters);
      characterResponse.setResponse(character);
      return characterResponse;
    } else {
      throw new MarvelApiException("Character not found", null);
    }
  }
}
