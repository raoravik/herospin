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

package com.visa.r4r.poc.herospin.marvel.model;

import com.google.gson.annotations.SerializedName;

public class StoryResource extends MarvelResource {
  @SerializedName("type") private String type;

  public String getType() {
    return type;
  }

  @Override public String toString() {
    return "StoryResource{"
           + "name="
           + super.getName()
           + "resourceUri="
           + super.getResourceUri()
           + "type='"
           + type
           + '\''
           +
           '}';
  }
}
