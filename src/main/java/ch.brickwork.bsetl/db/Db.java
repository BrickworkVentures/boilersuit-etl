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

import ch.brickwork.bsetl.db.exception.MapValueException;
import ch.brickwork.bsetl.db.exception.OperationNotAllowedException;
import ch.brickwork.bsetl.db.exception.TableNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

/**
 * A connection to a database
 */
@Getter
public class Db {

  private static String DEFAULT_DRIVER_CLASS_NAME = "org.h2.Driver";

  private static String DEFAULT_URL = "jdbc:h2:file:~/default.h2";

  private static String DEFAULT_SCHEMA_NAME = "";

  private static String DEFAULT_USER_NAME = "sa";

  private static String DEFAULT_PASSWORD = "";

  private Connection con;

  private FileWriter executeQueryDumpFileWriter;

  private String url, driver, user, password;

  private long statementCount = 0;

  private int maxStatementsPerTransaction;

  private ArrayList<Preprocessor> preprocessors = new ArrayList<>();

  @Getter
  private Map<String, Long> insertCounts = new HashMap<>();

  private IdentityHashMap<String, TableStructure> tables = new IdentityHashMap<>();

  private IdentityHashMap<String, ViewStructure> views = new IdentityHashMap<>();

  /**
   * open connection to default, can be used for testing purposes and opens a H2 file DB
   * connection to file default.h2
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public Db() throws SQLException, ClassNotFoundException, IOException {
    this(DEFAULT_URL, DEFAULT_DRIVER_CLASS_NAME, DEFAULT_USER_NAME, DEFAULT_PASSWORD, null, 0);
  }

  /**
   * open connection to any database. driver must be present on classpath
   * @param url if null, creates connection-less db (makes sense only in combination with executeQueryDumpFile
   * @param driver
   * @param user
   * @param password
   * @param maxStatementsPerTransaction {@literal if > 0}, all statements will be wrapped in BEGIN TRANSACTION..COMMIT; blocks
   * @param executeQueryDumpFile file to dump execute query statements (e.g. inserts), or null
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public Db(String url, String driver, String user, String password, File executeQueryDumpFile,
      int maxStatementsPerTransaction)
      throws ClassNotFoundException, SQLException, IOException {

    this.user = user;
    this.password = password;
    this.driver = driver;
    this.url = url;


    if(url != null) {
      Class.forName(this.driver = driver);
      getConnection();
    } else {
      con = null;
    }

    if(executeQueryDumpFile != null) {
      this.executeQueryDumpFileWriter = new FileWriter(executeQueryDumpFile);
    } else {
      this.executeQueryDumpFileWriter = null;
    }

    this.maxStatementsPerTransaction = maxStatementsPerTransaction;
    if(maxStatementsPerTransaction > 0) {
      if(con != null)
        con.setAutoCommit(false);
      appendToFile(beginTransactionStatement() + "\n");
    }
  }

  private void getConnection() throws SQLException {
    if(con != null && con.isValid(1))
      con.close();
    con = DriverManager
        .getConnection(this.url, this.user,
            this.password);
  }

  /**
   * append to file only
   * @param text
   * @throws IOException
   */
  public void appendToFile(String text) throws IOException {
    this.executeQueryDumpFileWriter.append(text);
  }

  /**
   * add preprocessor which will be applied to any row being inserted on this connection
   * @param preprocessor
   */
  public void addPreprocessor(Preprocessor preprocessor) {
    preprocessors.add(preprocessor);
  }

  /**
   * run a SQL query where you know it generates a result set
   * @param query
   * @return
   * @throws SQLException
   */
  public Result query(String query) throws SQLException {
    System.out.println(query);
    final ResultSet rs;
    if(con != null) {
      Statement statement = con.createStatement();
      rs = statement.executeQuery(query);
      commitIfNecessary();
    } else {
      rs = null;
    }
    statementCount++;
    return new Result(rs, this);
  }

  /**
   * run a SQL query without a result
   * @param query
   * @return
   * @throws SQLException
   */
  public boolean execute(String query) throws SQLException, IOException {
    System.out.println(query);
    dumpQuery(query);
    return executeQuery(query);
  }

  protected boolean executeQuery(String query) throws SQLException {
    final boolean result;
    if(con != null) {
      Statement statement = con.createStatement();
      result = statement.execute(query);
      commitIfNecessary();
    } else {
      result = false;
    }
    statementCount++;
    return result;
  }

  protected void dumpQuery(String query) throws IOException {
    if(executeQueryDumpFileWriter != null) {
      appendToFile("\n");

      if(maxStatementsPerTransaction > 0 && isCommitNecessary()) {
        appendToFile(commitStatement() + "\n" + beginTransactionStatement() + "\n");
      }

      appendToFile(query);
      if(!query.trim().endsWith(";"))
        appendToFile(";");
    }
  }

  public void closeConnection() throws SQLException, IOException {
    if(con != null)
      con.close();
    if(executeQueryDumpFileWriter != null) {
      if(maxStatementsPerTransaction > 0)
        appendToFile("\n" + commitStatement());
      executeQueryDumpFileWriter.flush();
    }
  }

  public void switchExecuteQueryFile(File newFile) throws IOException {
    if(executeQueryDumpFileWriter != null)
      executeQueryDumpFileWriter.flush();
    executeQueryDumpFileWriter = new FileWriter(newFile);
  }

  /**
   * insert a result into table "targetTableName" on this connection. rows will be preprocessed
   * (if preprocessors available). rows will be truncated in too long for target structure.
   * @param rowsToInsert
   * @param targetTableName
   * @throws SQLException
   */
  public void insert(Result rowsToInsert, String targetTableName)
      throws SQLException, IOException, OperationNotAllowedException {
    insert(rowsToInsert.getRowsAsMap(), targetTableName);
  }

  /**
   * insert a result into table "targetTableName" on this connection. rows will be preprocessed
   * (if preprocessors available). rows will be truncated in too long for target structure.
   * @param rowsToInsert
   * @param targetTableName
   * @throws SQLException
   */
  public void insert(List<Map<String, Object>> rowsToInsert, String targetTableName)
      throws SQLException, IOException, OperationNotAllowedException {
    System.out.println("Inserting " + rowsToInsert.size() + " into " + targetTableName);

    if(rowsToInsert.size() == 0) {
      System.out.println("...table is empty");
      return;
    }

    final ArrayList<String> keys = new ArrayList(rowsToInsert.get(0).keySet());
    for(Map<String, Object> unprocessedRow : rowsToInsert) {
      final Map<String, ?> row = truncate(preprocess(targetTableName, unprocessedRow), targetTableName);
      final String sql = "INSERT INTO " + quoteIdentifierIfNecessary(targetTableName) +
          // comma-separated column names
          "(" + keys.stream()
          .map(key -> quoteIdentifierIfNecessary(key))
          .collect(Collectors.joining(",")) + ")" +

          // values keyword
          " VALUES " +

          // comma-separated values, quoted if necessary
          "(" + row.keySet().stream()
          .map(key -> wrap(row.get(key))).collect(Collectors.joining(",")) + ")";

      execute(sql);
      insertCounts.compute(targetTableName, (k, v) -> v == null ? 1 : v + 1);
    }

    System.out.println("...insert done");
  }

  public void copyTableFrom(String tableName, Db sourceDb)
      throws SQLException, IOException, OperationNotAllowedException {
    final TableStructure tableStructure = sourceDb.getTableStructure(tableName);
    if(tableStructure == null)
      throw new TableNotFoundException(tableName);
    createOrReplaceTable(tableStructure);
    insert(sourceDb.query("SELECT * FROM " + tableName), tableName);
  }

  public void copyView(String viewName, Db sourceDb)
      throws SQLException, IOException, OperationNotAllowedException {
    final ViewStructure viewStructure = sourceDb.getViewStructure(viewName);
    if(viewStructure == null)
      throw new TableNotFoundException(viewName);
    createView(viewStructure);
  }

  protected String beginTransactionStatement() {
    return "BEGIN TRANSACTION";
  }

  protected String commitStatement() {
    return "COMMIT;";
  }

  /**
   * if property has length in target and value is to long, truncate
   * @param row
   * @param tableName
   * @return
   * @throws SQLException
   */
  private Map<String, ?> truncate(Map<String, Object> row, String tableName)
      throws SQLException, OperationNotAllowedException {
    final TableStructure tableStructure = getTableStructure(tableName);
    if(tableStructure == null) {
      System.out.println("Could not find table structure for " + tableName + " in \n" + tables.toString());
    }
    final Iterator propertyNameIterator = row.keySet().iterator();
    while(propertyNameIterator.hasNext()) {
      final String propertyName = Objects.toString(propertyNameIterator.next());
      final Integer length = tableStructure.getColumns().get(propertyName).getLength();
      if(isCharacterLike(tableStructure.getColumns().get(propertyName).getDataType()) && length != null) {
        final Object currentValue = encode(Objects.toString(mapValue(Objects.toString(row.get(propertyName)))));
        if(currentValue != null && currentValue.toString().length() > length) {
          row.put(propertyName, currentValue.toString().substring(0, length - 1));
        }
      }
    }
    return row;
  }

  /**
   * turns a set of objects into a set of the form <pre>(o1, o2, ...)</pre> suitable for usage
   * within a WHERE clause. Respects format based on type of object. E.g. strings will deliver
   * ('abc', 'def'), numbers will deliver (1,2,3)
   * @param setOfValues
   * @return SQL set, or "()" if no elements available
   */
  public String toSQLSet(Set setOfValues) {
    return "(" + setOfValues.stream()
        .map(val -> wrap(val))
        .collect(Collectors.joining(",")) + ")";
  }

  /**
   * for a list of elements, returns a clause like
   * <pre>
   *   WHERE ([propertyName] IN (.,.,.) OR [propertyName] IN (.,.,.) OR ...)
   * </pre>
   * clause is partitioned to keep individual sets small. Max. size of a parition is 100
   * @param propertyName
   * @param set
   * @return where clause with partitioned sql sets
   */
  public String wherePropertyIn(String propertyName, Set set) {
    return set.isEmpty() ? "" : " WHERE " + propertyName + " IN " + toSQLSet(set);
  }

  /**
   * simply quote a String-like object, e.g. from "a" make "'a'"
   * @param unquoted
   * @return
   */
  public static String quote(Object unquoted) {
    return "'" + unquoted + "'";
  }

  /**
   * if used with connection-less dbs, returns first ever created definition of table, otherwise
   * current defintion of table
   * @param tableName
   * @return
   * @throws SQLException
   * @throws OperationNotAllowedException
   */
  public TableStructure getTableStructure(String tableName)
      throws SQLException, OperationNotAllowedException {
    if(con == null)
      return tables.get(tableName);

    TableStructure tableStructure = new TableStructure(tableName);
    Statement sProperties = con.createStatement();
    ResultSet rsCols = sProperties.executeQuery(getSourceColumnNamesStatement(tableName));
    while (rsCols.next()) {
      tableStructure.addColumn(
          rsCols.getString("column_name"),
          mapDataType(rsCols.getString("data_type")),
          rsCols.getInt("character_maximum_length"));
    }
    if(tableStructure.getColumns().size() == 0)
      return null;
    else
      return tableStructure;
  }

  public boolean createOrReplaceTable(TableStructure tableStructure)
      throws SQLException, IOException, OperationNotAllowedException {
    if(con != null)
      dropTableIfExists(tableStructure.getTableName());

    final String sql = "CREATE TABLE " + quoteIdentifierIfNecessary(tableStructure.getTableName()) + "(" +
        tableStructure.getColumns().keySet().stream()
            .map(key -> tableStructureEntry(tableStructure.getColumns().get(key)))
            .collect(Collectors.joining(","))
        + ");";

    // remember
    tables.put(tableStructure.getTableName(), tableStructure);

    return execute(sql);
  }

  public void cleanDuplicates(String tableName, String uniqueKeyPropertyName)
      throws SQLException, IOException {
    execute("DELETE FROM " + tableName + " a USING ("
        + "      SELECT MIN(ctid) as ctid, " + uniqueKeyPropertyName
        + "        FROM " + tableName
        + "        GROUP BY " + uniqueKeyPropertyName + " HAVING COUNT(*) > 1"
        + "      ) b"
        + "      WHERE a." + uniqueKeyPropertyName + " = b." + uniqueKeyPropertyName
        + "      AND a.ctid <> b.ctid;");
  }

  protected void dropTableIfExists(String tableName)
      throws SQLException, IOException, OperationNotAllowedException {
    if(getTableStructure(tableName) != null)
      execute("DROP TABLE IF EXISTS " + quoteIdentifierIfNecessary(tableName) + " ;");
  }

  public ViewStructure getViewStructure(String viewName)
      throws SQLException {
    if(con == null)
      return views.get(viewName);

    Statement sProperties = con.createStatement();
    ResultSet rsDefinition = sProperties.executeQuery(getViewDefinitionStatement(viewName));
    if (!rsDefinition.next()) {
      throw new IllegalArgumentException("View with name " + viewName +  " not found");
    }
    ViewStructure viewStructure = new ViewStructure(viewName, rsDefinition.getString("view_definition"));
    if (rsDefinition.next()) {
      throw new IllegalArgumentException("Found more than one view with name " + viewName);
    }

    return viewStructure;
  }

  public boolean createView(ViewStructure viewStructure)
      throws SQLException, IOException, OperationNotAllowedException {
    if (con != null) {
      dropViewIfExists(viewStructure.getViewName());
    }

    final String viewName = quoteIdentifierIfNecessary(viewStructure.getViewName());
    final String viewDefinition = viewStructure.getViewDefinition();
    final String sql = "CREATE VIEW " + viewName + " AS " + viewDefinition;

    // remember
    views.put(viewStructure.getViewName(), viewStructure);

    return execute(sql);
  }

  protected void dropViewIfExists(String viewName)
      throws SQLException, IOException, OperationNotAllowedException {
    if(getTableStructure(viewName) != null)
      execute("DROP VIEW IF EXISTS " + quoteIdentifierIfNecessary(viewName) + " ;");
  }

  //
  // methods to customize DB specific stuff - overwrite these in your subclasses if you introduce
  // new DB systems
  //

  /**
   * for a dataType, returns true if this is a "String-like" type, typically a VARCHAR. Used to
   * determine whether a column must be truncated. if so, must have a length property in the
   * information table (cf. getSourceColumnNameStatement)
   * @param dataType
   * @return
   */
  protected boolean isCharacterLike(String dataType) {
    return StringUtils.contains(dataType.toLowerCase(), "char");
  }

  /**
   * based on type of object, maps, quotes and encodes properly. Check mapValue, quoteIfNecessary,
   * encode for further information
   * @param value
   * @return
   */
  public String wrap(Object value) {
    return encode(quoteIfNecessary(mapValue(value)));
  }


  /**
   * e.g. in H2, STRINGENCODE('abc\ndef') will convert the String such that steering characters
   * are interpreted correctly; default: no encoding
   * @param value
   * @return
   */
  protected String encode(String value) {
    return value;//value.replaceAll("\\\\u..", "?");
  }

  /**
   * makes an acceptable literal out of object o. If o is null, returns "null" as a string; if
   * non-null character-like, e.g. string, will quote it
   * @param o
   * @return literal representation of object o acceptable in a SQL statement on this connection
   */
  protected String quoteIfNecessary(Object o) {
    if(o == null)
      return "null";

    if(o instanceof String)
      return quote(o.toString());

    return o.toString();
  }

  /**
   * maps value if necessary; e.g. time like values can be converted to strings; binary values
   * can be mapped to hex strings prefixed by some 0x or whatever the DB likes to hear in this
   * case
   * @param o
   * @return
   */
  protected Object mapValue(Object o) {
    if(o == null)
      return null;
    else if(o instanceof String)
      return StringEscapeUtils.escapeJava((String) o).replaceAll("'", escapedQuoteLiteral());
    else if(o instanceof Number || o instanceof Boolean)
      return o;
    else if(o instanceof Timestamp || o instanceof Date)
      return o.toString();
    else if(o instanceof byte[])
      return Hex.encodeHexString((byte[]) o);
    else
      throw new MapValueException("Could not map value of class " + o.getClass());
  }

  /**
   * the escaped literal for the apostrophe used within a string; something like "''" or "\'"
   * @return
   */
  protected String escapedQuoteLiteral() {
    return "''";
  }

  /**
   * if some circumstances, table and column names themselves have to be quoted, e.g. to deal
   * with upper/lower case problems, this is done here
   * @param unquotedTableName
   * @return e.g. makes "\"MYTABLE\"" from "MYTABLE"
   */
  protected String quoteIdentifierIfNecessary(String unquotedTableName) {
    return unquotedTableName;
  }

  /**
   * when copying a table to this connection, the original data type (copied from an other connection
   * with maybe a different DB system) can be mapped to an other datatype. E.g., you would transform
   * a BOOLEAN from Postgres to a bit on MS-SQL
   * @param originalDataType
   * @return mapped type
   */
  protected String mapDataType(String originalDataType) {
    return originalDataType;
  }

  /**
   * generates a "line" entry in a CREATE TABLE statement
   * @param propertyStructure
   * @return
   */
  protected String tableStructureEntry(PropertyStructure propertyStructure) {
    final String lengthOrEmpty;
    if(propertyStructure.getDataType().equalsIgnoreCase("varchar") &&
        propertyStructure.getLength() != null)
      lengthOrEmpty = "(" + propertyStructure.getLength() + ")";
    else
      lengthOrEmpty = "";

    return quoteIdentifierIfNecessary(
        // column name (e.g. mytext)
        propertyStructure.getColumnName()) + " " +

        // datatype, e.g. VARCHAR
        mapDataType(propertyStructure.getDataType()) +

        // if has length, add length in brackets after type
        lengthOrEmpty;
  }

  private Map<String, Object> preprocess(String tableName, Map<String, Object> row) {
    for(Preprocessor preprocessor : preprocessors)
      row = preprocessor.preprocessRow(tableName, row);
    return row;
  }

  private static String getSourceColumnNamesStatement(String name) {
    return "select * from information_schema.columns where lower(table_name)='" + name.toLowerCase() + "'";
  }

  private static String getViewDefinitionStatement(String name) {
    return "select view_definition from information_schema.views where lower(table_name)='" + name.toLowerCase() + "'";
  }

  private void commitIfNecessary() throws SQLException {
    if(con != null && isCommitNecessary())
      con.commit();
  }

  private boolean isCommitNecessary() {
    return maxStatementsPerTransaction > 0 && statementCount % maxStatementsPerTransaction == 0;
  }
}
