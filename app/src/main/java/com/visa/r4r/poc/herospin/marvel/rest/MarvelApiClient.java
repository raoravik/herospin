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

import com.google.gson.Gson;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

public class MarvelApiClient {
  private static final int INVALID_AUTH_CODE = 401;

  private final MarvelApiConfig marvelApiConfig;

  public MarvelApiClient(MarvelApiConfig marvelApiConfig) {
    this.marvelApiConfig = marvelApiConfig;
  }

  public <T> T getApi(Class<T> apiRest) {
    return marvelApiConfig.getRetrofit().create(apiRest);
  }

  public <T> T execute(Call<T> call) throws MarvelApiException {
    Response<T> response = null;
    try {
      response = call.execute();
    } catch (IOException e) {
      throw new MarvelApiException("Network error", e);
    }
    if (response.isSuccessful()) {
      return response.body();
    } else {
      parseError(response);
      return null;
    }
  }

  private void parseError(Response execute) throws MarvelApiException {
    String marvelCode = "";
    String marvelDescription = "";
    if (execute.errorBody() != null) {
      Gson gson = new Gson();
      try {
        String errorBody = execute.errorBody().string();
        MarvelError marvelError = gson.fromJson(errorBody, MarvelError.class);
        marvelCode = marvelError.getCode();
        marvelDescription = marvelError.getMessage();
        if (marvelDescription == null || "".equals(marvelDescription)) {
          marvelDescription = marvelError.getStatus();
        }
      } catch (IOException e) {
        //Error parsing marvelError. Ignoring...
      }
    }

    if(execute.code() == INVALID_AUTH_CODE) {
      throw new MarvelAuthApiException(execute.code(), marvelCode, marvelDescription, null);
    } else {
      throw new MarvelApiException(execute.code(), marvelCode, marvelDescription, null);
    }
  }
}
