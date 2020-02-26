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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ValueSanitizer for 1..n columns where values must be built consistently accross within a row
 * context
 */
public class CorrelatedColumnsSanitizer implements ValueSanitizer {

  /**
   * if same sanitized result for same row id has already been picked (which is likely if there are
   * only a small number of permutations available), we repeat the process until we find one; if the
   * attempts all fail, we add a random number to the result
   */
  private static final int MAX_ATTEMPTS_TO_FIND_NEW_VALUE = 10;

  private final Object[][] components;

  private final Map<String, Function<Object[], Object>> valueBuilders = new HashMap<>();

  private final Function<Object[], Object> defaultValueBuilder;

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
   *
   * @param components array of component arrays
   * @param defaultValueBuilder result builder function for columns not specifically added via
   * addColumn, can be null
   */
  public CorrelatedColumnsSanitizer(String[][] components,
      Function<Object[], Object> defaultValueBuilder) {
    this.components = components;
    this.defaultValueBuilder = defaultValueBuilder == null ?
        // if null, add builder to concatenate components, separated by whitespace
        comps -> Stream.of(comps)
            .map(comp -> String.valueOf(comp))
            .collect(Collectors.joining(" "))
        : defaultValueBuilder;
  }

  /**
   * add a column, along with a rule how to build its value, given random components. for example,
   * if components are firstname, lastname and mail domain, a column addition could read
   * <pre>
   *   // chuck.norris@mymail.com, elvis.presley@music.com, ...
   *   myCorrelatatedColSan.addColumn("email", c -> c[0] + "." + c[1] + "@" + c[2]
   * </pre>
   *
   * @param columnName column name
   * @param valueBuilder function to build the column value given n random components; use null for
   * default whitespace-separated concatenation
   */
  public void addColumn(String columnName, Function<Object[], Object> valueBuilder) {
    valueBuilders.put(columnName, valueBuilder);
  }

  /**
   * @param originalValue original value, which may be sensitive
   * @param rowId if available, unique id identifying the row on which this value sits
   * @param propertyName name of the property
   * @return result, built according valueBuilder for this property (cf. addColumn), or using
   * default value builder (if available), or simply concatenating whitespace separated as a
   * default
   */
  @Override
  public Object sanitize(Object originalValue, String rowId, String propertyName) {
    if (originalValue == null) {
      return null;
    }

    if (rowId != null) {
      if (!contextCache.containsKey(rowId)) {
        Object[] permutationOfComponentElements = null;
        for(int attempt = 0; attempt < MAX_ATTEMPTS_TO_FIND_NEW_VALUE; attempt++) {
          permutationOfComponentElements = Stream.of(components)
              .map(RandomElementFromSetSanitizer::pickAny)
              .toArray();

          // found new one, great
          if(!contains(contextCache, permutationOfComponentElements))
            break;

          // all attempts were for nothing:
          if(attempt == MAX_ATTEMPTS_TO_FIND_NEW_VALUE - 1) {
                permutationOfComponentElements[permutationOfComponentElements.length - 1] =
                    String.valueOf(permutationOfComponentElements[permutationOfComponentElements.length - 1])
                        .concat(" ").concat(String.valueOf(RandomSanitizer.random(String.class)));
          }
        }

        contextCache.put(rowId, permutationOfComponentElements);
      }

      final Object[] componentValues = contextCache.get(rowId);

      if (valueBuilders.containsKey(propertyName)) {
        return valueBuilders.get(propertyName).apply(componentValues);
      } else {
        return defaultValueBuilder.apply(componentValues);
      }
    }

    return RandomSanitizer.random(originalValue.getClass());
  }

  /**
   * @return true if and only if an array is contained in set where all element values are equal
   *  to the corresponding element values of candidate
   */
  private boolean contains(Map<?, Object[]> set, Object[] candidate) {
    for(Object[] element : set.values()) {
      if(Arrays.equals(element, candidate))
        return true;
    }
    return false;
  }

}