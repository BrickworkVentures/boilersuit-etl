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

package ch.brickwork.bsetl.sanitize.constant.ch;

import ch.brickwork.bsetl.sanitize.ValueSanitizer;
import java.util.Objects;
import java.util.StringJoiner;
import org.apache.commons.lang3.RandomUtils;

/**
 * returns a random Swiss social security number
 * TODO: generate valid ones (last number is a checksum), cf. https://www.sozialversicherungsnummer.ch/aufbau-neu.htm
 */
public class AhvNummerSanitizer implements ValueSanitizer {

  @Override
  public Object sanitize(Object originalValue, String rowId, String propertyName) {
    return new StringJoiner(".")
        .add("756")
        .add(Objects.toString(RandomUtils.nextInt(1000, 9999)))
        .add(Objects.toString(RandomUtils.nextInt(1000, 9999)))
        .add(Objects.toString(RandomUtils.nextInt(1, 99)))
        .toString();
  }

}
