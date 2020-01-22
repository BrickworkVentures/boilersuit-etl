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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;

public class RandomSanitizer implements ValueSanitizer {

  private static final long DEFAULT_NUMERIC_RANGE_START_INCLUSIVE = 0;

  private static final long DEFAULT_NUMERIC_RANGE_END_INCLUSIVE = +10000000l;

  private final long numericRangeStartInclusive;

  private final long numericRangeEndInclusive;

  private final String hint;

  private final Integer maxLength;

  public RandomSanitizer() {
    this.numericRangeStartInclusive = DEFAULT_NUMERIC_RANGE_START_INCLUSIVE;
    this.numericRangeEndInclusive = DEFAULT_NUMERIC_RANGE_END_INCLUSIVE;
    this.hint = null;
    this.maxLength = null;
  }

  /**
   * if original value is String, the hint will be included in the sanitized
   * result, making the random code readable in context,
   * e.g. 'Client cd45-cdkj2-dkckd-dcjig'
   * @param hint
   */
  public RandomSanitizer(String hint) {
    this.numericRangeStartInclusive = DEFAULT_NUMERIC_RANGE_START_INCLUSIVE;
    this.numericRangeEndInclusive = DEFAULT_NUMERIC_RANGE_END_INCLUSIVE;
    this.hint = hint;
    this.maxLength = null;
  }

  public RandomSanitizer(long integerRangeStartInclusive, long numericRangeEndInclusive) {
    this.numericRangeStartInclusive = integerRangeStartInclusive;
    this.numericRangeEndInclusive = numericRangeEndInclusive;
    this.hint = null;
    this.maxLength = null;
  }

  public RandomSanitizer(String hint, int maxLength) {
    this.numericRangeStartInclusive = DEFAULT_NUMERIC_RANGE_START_INCLUSIVE;
    this.numericRangeEndInclusive = DEFAULT_NUMERIC_RANGE_END_INCLUSIVE;
    this.hint = hint;
    this.maxLength = maxLength;
  }

  /**
   * sanitizing a value by replacing Strings by random MD5-Hex token, numeric values (non-decimal)
   * by random integer value, decimal values by random float
   * @param originalValue
   * @return
   */
  @Override
  public Object sanitize(Object originalValue, String rowId, String propertyName) {
    if(originalValue == null)
      return null;
    else
      return random(originalValue.getClass(), numericRangeStartInclusive, numericRangeEndInclusive, hint, maxLength);
  }

  public static Object random(Class<?> clazz) {
    return random(clazz, DEFAULT_NUMERIC_RANGE_START_INCLUSIVE, DEFAULT_NUMERIC_RANGE_END_INCLUSIVE, null, null);
  }

  public static Object random(Class<?> clazz, long numericRangeStartInclusive, long numericRangeEndInclusive, String hint, Integer maxLength) {
    if(clazz == String.class) {
      final String text = (hint == null ? "" : (hint + "/")) +
          DigestUtils.md5Hex(String.valueOf(Math.random() * DEFAULT_NUMERIC_RANGE_END_INCLUSIVE));
      if(maxLength == null)
        return text;
      else
        return text.substring(0, Math.min(maxLength, text.length()));
    }
    else if(clazz == Long.class)
      return RandomUtils.nextLong(numericRangeStartInclusive, numericRangeEndInclusive);
    else if(clazz == Integer.class)
      return RandomUtils.nextInt((int) numericRangeStartInclusive, (int) numericRangeEndInclusive);
    else if(clazz == Float.class)
      return RandomUtils.nextFloat((float) numericRangeStartInclusive, (float) numericRangeEndInclusive);
    else if(clazz == Double.class)
      return RandomUtils.nextDouble((double) numericRangeStartInclusive, (double) numericRangeEndInclusive);
    else if(clazz == Date.class)
      return Date.from(LocalDateTime.now().plusDays(RandomUtils.nextInt(0, 100)).toInstant(
          ZoneOffset.UTC));
    else if(clazz == Timestamp.class)
      return Timestamp.valueOf(LocalDateTime.now().plusDays(RandomUtils.nextInt(0, 100)));
    else {
      System.out.println("Not sanitizable exception will be thrown; parameters are: numericRange " +
          numericRangeStartInclusive + " - " +
          numericRangeEndInclusive + ", hint: " + hint + ", maxLength: " + maxLength);
      throw new TypeNotSanitizableException(clazz.toString());
    }
  }

}
