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

import ch.brickwork.bsetl.sanitize.constant.InternetDomains;
import ch.brickwork.bsetl.sanitize.constant.de.HumanNames;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * ValueSanitizer for human beings, having first and last names, ensuring that names and email
 * addresses etc. are consistently sanitized within a given context
 */
public class PersonSanitizer implements ValueSanitizer {

  private final String[] firstNames, lastNames, domains;

  private final String[] nameColumns, emailColumns, firstNameColumns, lastNameColumns;

  private Map<String, PersonCacheItem> contextCache = new HashMap<>();

  public PersonSanitizer(String[] nameColumns,
      String[] emailColumns, String[] firstNameColumns, String[] lastNameColumns) {
    this(HumanNames.TOP_SWISS_FIRSTNAMES, HumanNames.TOP_GERMAN_LASTNAMES,
        InternetDomains.RANDOM_MAIL_DOMAINS,
        nameColumns, emailColumns, firstNameColumns, lastNameColumns);
  }

  public PersonSanitizer(String[] firstNames, String[] lastNames, String[] mailDomains,
      String[] nameColumns,
      String[] emailColumns, String[] firstNameColumns, String[] lastNameColumns) {
    this.firstNames = firstNames;
    this.lastNames = lastNames;
    this.domains = mailDomains;
    this.nameColumns = nameColumns;
    this.emailColumns = emailColumns;
    this.firstNameColumns = firstNameColumns;
    this.lastNameColumns = lastNameColumns;
  }

  @Override
  public Object sanitize(Object originalValue, String rowId, String propertyName) {
    if (originalValue == null) {
      return null;
    }
    if (rowId == null) {
      return RandomSanitizer.random(originalValue.getClass());
    }
    if (!contextCache.containsKey(rowId)) {
      contextCache.put(rowId, new PersonCacheItem(
          ArrayUtils.isEmpty(firstNames) ? null : RandomElementFromSetSanitizer.pickAny(firstNames).toString(),
          ArrayUtils.isEmpty(lastNames) ? null : RandomElementFromSetSanitizer.pickAny(lastNames).toString(),
          ArrayUtils.isEmpty(domains) ? null : RandomElementFromSetSanitizer.pickAny(domains).toString(),
          0));
    }
    final PersonCacheItem person = contextCache.get(rowId);
    if (ArrayUtils.contains(nameColumns, propertyName)) {
      return new StringJoiner(" ")
          .add(StringUtils.capitalize(person.firstName.toLowerCase()))
          .add(StringUtils.capitalize(person.lastName.toLowerCase()))
          .toString();
    } else if (ArrayUtils.contains(emailColumns, propertyName)) {
      return new StringJoiner("@")
          .add(
              new StringJoiner(".")
                  .add(ObjectUtils.firstNonNull(StringUtils.lowerCase(person.firstName), ""))
                  .add(ObjectUtils.firstNonNull(StringUtils.lowerCase(person.lastName), ""))
                  .toString())
          .add(ObjectUtils.firstNonNull(person.domain, ""))
          .toString();
    } else if (ArrayUtils.contains(firstNameColumns, propertyName)) {
      return StringUtils.capitalize(person.firstName.toLowerCase());
    } else if (ArrayUtils.contains(lastNameColumns, propertyName)) {
      return StringUtils.capitalize(person.lastName.toLowerCase());
    } else {
      return RandomSanitizer.random(originalValue.getClass());
    }
  }

  @AllArgsConstructor
  private class PersonCacheItem {

    String firstName, lastName, domain;
    int usageCount;
  }
}
