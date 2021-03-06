/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.picasa.v2.atom;

import com.google.api.client.xml.XmlNamespaceDictionary;

import java.util.Map;

/**
 * Utilities for the Atom XML format of the Picasa Web Albums Data API.
 *
 * @since 2.2
 */
public final class PicasaWebAlbumsAtom {

  /** XML namespace dictionary. */
  public static final XmlNamespaceDictionary NAMESPACE_DICTIONARY = new
      XmlNamespaceDictionary();
  static {
    Map<String, String> map = NAMESPACE_DICTIONARY.namespaceAliasToUriMap;
    map.put("", "http://www.w3.org/2005/Atom");
    map.put("atom", "http://www.w3.org/2005/Atom");
    map.put("exif", "http://schemas.google.com/photos/exif/2007");
    map.put("gd", "http://schemas.google.com/g/2005");
    map.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
    map.put("georss", "http://www.georss.org/georss");
    map.put("gml", "http://www.opengis.net/gml");
    map.put("gphoto", "http://schemas.google.com/photos/2007");
    map.put("media", "http://search.yahoo.com/mrss/");
    map.put("openSearch", "http://a9.com/-/spec/opensearch/1.1/");
    map.put("xml", "http://www.w3.org/XML/1998/namespace");
  }

  private PicasaWebAlbumsAtom() {
  }
}
