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

import ch.brickwork.bsetl.sanitize.exception.TypeNotSanitizableException;
import java.util.Objects;
import java.util.StringJoiner;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class CompositeSanitizer implements ValueSanitizer {

  private static final Object NULL_TOKEN_VALUE = "-";

  private static final ValueSanitizer DEFAULT_VALUE_SANITIZER = new RandomSanitizer();

  private final String divider;

  private final ValueSanitizer[] sanitizers;

  public CompositeSanitizer(String divider,
      ValueSanitizer... sanitizers) {
    this.divider = divider;
    this.sanitizers = sanitizers;
  }

  @Override
  public Object sanitize(Object originalValue, String rowId, String propertyName) {
    if(originalValue == null) {
      return null;
    }

    if(originalValue instanceof String) {
      final String[] tokens = StringUtils.split(originalValue.toString(), divider);
      StringJoiner stringJoiner = new StringJoiner(divider);
      for(int i = 0; i < tokens.length; i++) {
        final ValueSanitizer sanitizer = i < sanitizers.length ? sanitizers[i] : DEFAULT_VALUE_SANITIZER;
        stringJoiner.add(Objects.toString(
            ObjectUtils.firstNonNull(sanitizer.sanitize(tokens[i], rowId, propertyName), NULL_TOKEN_VALUE)));
      }
      return stringJoiner.toString();
    } else {
      throw new TypeNotSanitizableException("You can only use Strings in composite sanitizers");
    }
  }
}
