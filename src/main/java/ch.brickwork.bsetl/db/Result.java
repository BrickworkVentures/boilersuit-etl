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

package ch.brickwork.bsetl.db;

import ch.brickwork.bsetl.db.exception.MoreThanOneColumnPresentException;
import ch.brickwork.bsetl.db.exception.MoreThanOneRowPresentException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;

/**
 * an abstraction of the JDBC result set
 */
public class Result {

  private ResultSet resultSet;

  /**
   * cached instance of list of rows, represented as maps of String (property name) to object
   * (value)
   */
  private List<Map<String, Object>> listOfRowMaps = null;

  private Db obtainedFrom;

  public Result(ResultSet resultSet, Db obtainedFrom) {
    this.resultSet = resultSet;
    this.obtainedFrom = obtainedFrom;
  }

  /**
   * get a list of maps, where each map represents a row of the result set, where key is the
   * property name, and value is the value
   * @return rows as list of maps
   * @throws SQLException
   */
  public List<Map<String, Object>> getRowsAsMap() throws SQLException {
    if(listOfRowMaps == null) {
      listOfRowMaps = new ArrayList<>();
      while (resultSet.next()) {
        final Map<String, Object> map =  new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (int col = 1; col <= resultSet.getMetaData().getColumnCount(); col++) {
          map.put(resultSet.getMetaData().getColumnName(col),
              resultSet.getObject(col));
        }
        listOfRowMaps.add(map);
      }
    }
    return listOfRowMaps;
  }

  /**
   * if you know there is one single row in the result, gets the value of a property
   * @param propertyName
   * @return value of property on that single row
   * @throws SQLException
   * @throws MoreThanOneRowPresentException if there are more than 1 or less than 1 results
   */
  public Object getSingleRowPropertyValue(String propertyName)
      throws MoreThanOneRowPresentException, SQLException {
    final List<Map<String, Object>> maps = getRowsAsMap();
    if(maps.size() != 1)
      throw new MoreThanOneRowPresentException("You should only use this method for results with one row as a result, this one has " + maps.size());
    else
      return maps.get(0).get(propertyName);
  }

  /**
   * @return get all values found in the one single column of result set
   * @throws SQLException
   * @throws MoreThanOneColumnPresentException
   */
  public Set<Object> singleColumnValues() throws MoreThanOneColumnPresentException, SQLException {
    final List<Map<String, Object>> rows = getRowsAsMap();

    if(CollectionUtils.isEmpty(rows)) {
      return SetUtils.EMPTY_SORTED_SET;
    }

    if(rows.get(0).keySet().size() != 1) {
      throw new MoreThanOneColumnPresentException();
    }

    return rows.stream().map(row -> row.get(row.keySet().iterator().next())).collect(
        Collectors.toSet());
  }

  /**
   * @param columnName
   * @return values within column "columnName" as a set
   * @throws SQLException
   */
  public Set<Object> getColumnValues(String columnName) throws SQLException {
    return getRowsAsMap().stream().map(row -> row.get(columnName))
        .collect(Collectors.toSet());
  }

  /**
   * @param columnName
   * @return values within column "columnName" as a set, not null
   * @throws SQLException
   */
  public Set<Object> getColumnNonNullValues(String columnName) throws SQLException {
    return getRowsAsMap().stream().map(row -> row.get(columnName)).filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  /**
   * convenient method to return the sql set obtained from the DB connection where this result
   * was obtained from (cf. {@link Db#toSQLSet(Set)} toSQLSet}
   * @param columnName
   * @return
   * @throws SQLException
   */
  public String getColumnValuesAsSQLSet(String columnName) throws SQLException {
    return obtainedFrom.toSQLSet(getColumnValues(columnName));
  }

  /**
   * return values of first column suitable for use in SQL IN clause
   * @return for values { "abc", "def" }, returns String like "('abc', 'def')"
   * @throws SQLException
   */
  public String singleColumnValuesAsSQLSet() throws SQLException, MoreThanOneColumnPresentException {
    return obtainedFrom.toSQLSet(singleColumnValues());
  }

  public int countRows() throws SQLException {
    return getRowsAsMap().size();
  }

  public Object vLookUp(String searchProperty, Object valueToMatchWith, String targetProperty)
      throws SQLException {
    for(Map<String, Object> row : getRowsAsMap()) {
      if(Objects.equals(row.get(searchProperty), valueToMatchWith))
        return row.get(targetProperty);
    }
    return null;
  }

  public int size() throws SQLException {
    if(listOfRowMaps == null) getRowsAsMap();     // init first, if necessary
    return CollectionUtils.size(listOfRowMaps);
  }

}
