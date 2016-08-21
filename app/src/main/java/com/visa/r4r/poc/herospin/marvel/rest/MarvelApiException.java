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

public class MarvelApiException extends Exception {
  private final int httpCode;
  private final String marvelCode;

  public MarvelApiException(int httpCode, String marvelCode, String description, Throwable cause) {
    super(description, cause);
    this.httpCode = httpCode;
    this.marvelCode = marvelCode;
  }

  public MarvelApiException(String message, Throwable cause) {
    this(-1, "", message, cause);
  }

  public int getHttpCode() {
    return httpCode;
  }

  public String getMarvelCode() {
    return marvelCode;
  }
}
