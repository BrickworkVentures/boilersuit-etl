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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.commons.codec.binary.Hex;

public class PostgresDB extends Db {

  public PostgresDB() throws SQLException, ClassNotFoundException, IOException {
    super();
  }

  public PostgresDB(String url, String driver, String user, String password, File executeQueryFile,
      int maxStatementsPerTransaction)
      throws ClassNotFoundException, SQLException, IOException {
    super(url, driver, user, password, executeQueryFile, maxStatementsPerTransaction);
  }

  @Override
  protected String encode(String s) {
    return s;
  }

  @Override
  protected String mapDataType(String originalDataType) {
    if(originalDataType.equals("boolean"))
      return "boolean";
    else if(originalDataType.equals("varbinary") || originalDataType.contains("char"))
      return "varchar";
    else if(originalDataType.equals("datetime"))
      return "timestamp";
    else if(originalDataType.equals("uniqueidentifier"))
      return "varchar";
    else if(originalDataType.equals("bit"))
      return "boolean";
    else if(originalDataType.equals("image"))
      return "bytea";
    else
      return super.mapDataType(originalDataType);
  }

  @Override
  protected Object mapValue(Object o) {
    if(o instanceof String)
      return ((String) o).replaceAll("'", escapedQuoteLiteral());
    else if(o instanceof byte[])
      return "\\x" + Hex.encodeHexString((byte[]) o);
    else
      return super.mapValue(o);
  }

  @Override
  protected String beginTransactionStatement() {
      return "BEGIN;";
  }

  @Override
  protected String quoteIdentifierIfNecessary(String unquotedTableName) {
      if("to".equalsIgnoreCase(unquotedTableName))
          return "\"" + unquotedTableName + "\"";
      return unquotedTableName;
  }
}
