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

public enum Format {
  COMIC("comic"),
  MAGAZINE("magazine"),
  TRADE_PAPERBACK("trade paperback"),
  HARDCOVER("hardcover"),
  DIGEST("digest"),
  GRAPHIC_NOVEL("graphic novel"),
  DIGITAL_COMIC("digital comic"),
  INFINITE_COMIC("infinite comic");

  private final String format;

  private Format(final String format) {
    this.format = format;
  }

  @Override public String toString() {
    return format;
  }
}
