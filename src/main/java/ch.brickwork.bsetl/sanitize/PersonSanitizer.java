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
import java.util.Objects;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * ValueSanitizer for human beings, having first and last names, ensuring that names and email
 * addresses etc. are consistently sanitized within a given context
 */
public class PersonSanitizer extends CorrelatedColumnsSanitizer {


  public PersonSanitizer(String[] nameColumns,
      String[] emailColumns, String[] firstNameColumns, String[] lastNameColumns, boolean putFirstnameFirst) {
    this(HumanNames.TOP_SWISS_FIRSTNAMES, HumanNames.TOP_GERMAN_LASTNAMES,
        InternetDomains.RANDOM_MAIL_DOMAINS,
        nameColumns, emailColumns, firstNameColumns, lastNameColumns, putFirstnameFirst);
  }

  public PersonSanitizer(String[] firstNames, String[] lastNames, String[] mailDomains,
      String[] nameColumns,
      String[] emailColumns, String[] firstNameColumns, String[] lastNameColumns, boolean putFirstnameFirst) {

    super(new String[][] {
        firstNames,
        lastNames,
        mailDomains
    }, null);

    for(String firstNameColumn : firstNameColumns) {
      super.addColumn(firstNameColumn,
          components -> capitalizeLowerCase(Objects.toString(components[0])));
    }

    for(String lastNameColumn : lastNameColumns) {
      super.addColumn(lastNameColumn,
          components -> capitalizeLowerCase(Objects.toString(components[1])));
    }

    for(String nameColumn : nameColumns) {
      super.addColumn(nameColumn,
          components ->  new StringJoiner(" ")
              .add(capitalizeLowerCase(Objects.toString(components[putFirstnameFirst ? 0 : 1])))
              .add(capitalizeLowerCase(Objects.toString(components[putFirstnameFirst ? 1 : 0])))
              .toString());
    }

    for(String emailColumn : emailColumns) {
      super.addColumn(emailColumn,
          components ->  new StringJoiner("@")
              .add(
                  new StringJoiner(".")
                      .add(ObjectUtils.firstNonNull(StringUtils.lowerCase(Objects.toString(components[putFirstnameFirst ? 0 : 1])), ""))
                      .add(ObjectUtils.firstNonNull(StringUtils.lowerCase(Objects.toString(components[putFirstnameFirst ? 1 : 0])), ""))
                      .toString())
              .add(ObjectUtils.firstNonNull(Objects.toString(components[2]), ""))
              .toString());
    }
  }

  private String capitalizeLowerCase(String s) {
    return StringUtils.capitalize(StringUtils.lowerCase(s));
  }

}
