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

import ch.brickwork.bsetl.db.Preprocessor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *   A Preprocessor tailored to sanitize values within rows. The preprocessor can be added to the
 *   target database, causing inserts of any value covered in the sanitizer configuration to be
 *   obfuscated or replaced
 * </p>
 * <p>
 *   Via the configuration, the DBSanitizer applies a series of defined @see ch.brickwork.bsetl.sanitize.ValueSanitizer
 *   to do the job
 * </p>
 * <p>
 *   Some ValueSanitizers have a row context allowing for various properties (e.g. name, email) to
 *   be populated with different but connected values (e.g. marcel - marcel@camporelli.ch instead of
 *   marcel - foo@foo.com; to this end, a mapping function must be provided returning the value of
 *   a unique id for the row, like so:
 *   <pre>
 *     this.obTarget.addPreprocessor(new DbSanitizer(sanitizerConfig, map ->
 *         Objects.toString(map.get("Id")).replaceAll("null", "")
 *             .concat(Objects.toString(map.get("id")).replaceAll("null", ""))));
 *   </pre>
 *   The id map is per Db, not per table. However, if different tables have different ids,
 *   concatenation of any available/required id property will usually work (all "unused" id properties
 *   will deliver null).
 * </p>
 */
public class DbSanitizer implements Preprocessor {

  private SanitizerConfiguration sanitizerConfiguration;

  private Set<String> touchedColumns = new HashSet<>();

  private Function<Map<String, Object>, String> getIdFromMap = map -> null;

  /**
   * If rows can't be identified; means you will
   * not be able to use contextual value sanitizers; if each row in your table has a unique
   * key (may be composed), it is preferrable to use DbSanitizer(config, function)
   * @param sanitizerConfiguration
   */
  public DbSanitizer(SanitizerConfiguration sanitizerConfiguration) {
    this.sanitizerConfiguration = sanitizerConfiguration;
  }

  /**
   * If each of your rows can be identified with a unique identifier (may be composed), this
   * is the best constructor to use; if not, use the simple constructor. If your table has a
   * column which is unique, the getIdFromMap function is as simple as
   * <pre>map -> map.get("id")</pre>
   * @param sanitizerConfiguration
   * @param getIdFromMap function mapping a row (Map of properties) to a String which is unique across all rows
   */
  public DbSanitizer(SanitizerConfiguration sanitizerConfiguration, Function<Map<String, Object>, String> getIdFromMap) {
    this.sanitizerConfiguration = sanitizerConfiguration;
    this.getIdFromMap = getIdFromMap;
  }

  @Override
  public Map<String, Object> preprocessRow(String tableName, Map<String, Object> inputRow) {
    for(String propertyName : inputRow.keySet()) {
      if(sanitizerConfiguration.shouldSanitize(tableName, propertyName)) {
        inputRow.put(propertyName, sanitizerConfiguration.getSanitizer(tableName, propertyName)
            .sanitize(inputRow.get(propertyName), getIdFromMap.apply(inputRow), propertyName));
        touchedColumns.add(SanitizerConfiguration.key(tableName, propertyName));
      }
    }
    return inputRow;
  }

  public Set<String> getUntouchedColumns() {
    return sanitizerConfiguration.getSanitizedColumns().stream()
        .filter(col -> !touchedColumns.contains(col))
        .collect(Collectors.toSet());
  }

}
