/*
 * Copyright (c) 2019 Brickwork Ventures GmbH, CH-8400 Winterthur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ch.brickwork.bsetl.sanitize;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * ValueSanitizer for 1..n columns where values must be built consistently accross within
 * a row context
 */
public class CorrelatedColumnsSanitizer implements ValueSanitizer {
  private final Object[][] components;
  private final Map<String, Function<Object[], Object>> valueBuilders = new HashMap<>();
  private Map<String, Object[]> contextCache = new HashMap<>();

  /**
   * <pre>
   *   {
   *     { "elvis", "chuck" },            // component 1
   *     { "norris", "presely" },         // component 2
   *     { "mymail.com", "music.com" },   // component 3
   *     ...
   *   }
   * </pre>
   */
  public CorrelatedColumnsSanitizer(String[][] components) {
    this.components = components;
  }

  /**
   * add a column, along with a rule how to build its value, given random components.
   * for example, if components are firstname, lastname and mail domain, a column addition
   * could read
   * <pre>
   *   // chuck.norris@mymail.com, elvis.presley@music.com, ...
   *   myCorrelatatedColSan.addColumn("email", c -> c[0] + "." + c[1] + "@" + c[2]
   * </pre>
   * @param columnName column name
   * @param valueBuilder function to build the column value given n random components
   */
  public void addColumn(String columnName, Function<Object[], Object> valueBuilder) {
    valueBuilders.put(columnName, valueBuilder);
  }

  @Override
  public Object sanitize(Object originalValue, String rowId, String propertyName) {
    if (originalValue == null) {
      return null;
    }

    if (rowId != null) {
      if (!contextCache.containsKey(rowId)) {
        contextCache.put(rowId, Stream.of(components)
            .map(RandomElementFromSetSanitizer::pickAny)
            .toArray());
      }

      final Object[] componentValues = contextCache.get(rowId);

      if (valueBuilders.containsKey(propertyName)) {
        return valueBuilders.get(propertyName).apply(componentValues);
      }
    }

    return RandomSanitizer.random(originalValue.getClass());
  }
}