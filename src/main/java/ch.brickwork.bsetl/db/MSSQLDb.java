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
import java.sql.Timestamp;

public class MSSQLDb extends Db {

  private final boolean addGoAfterTransactionCommit;

  public MSSQLDb() throws SQLException, ClassNotFoundException, IOException {
    super();
    this.addGoAfterTransactionCommit = true;
  }

  /**
   *
   * @param url
   * @param driver
   * @param user
   * @param password
   * @param executeQueryFile
   * @param maxStatementsPerTransaction
   * @param addGoAfterTransactionCommit will add \nGO\n after each commit to signal batch end to sqlcmd
   * @throws ClassNotFoundException
   * @throws SQLException
   * @throws IOException
   */
  public MSSQLDb(String url, String driver, String user, String password, File executeQueryFile,
      int maxStatementsPerTransaction, boolean addGoAfterTransactionCommit)
      throws ClassNotFoundException, SQLException, IOException {
    super(url, driver, user, password, executeQueryFile, maxStatementsPerTransaction);
    this.addGoAfterTransactionCommit = addGoAfterTransactionCommit;
  }


  @Override
  protected String mapDataType(String originalDataType) {
    if (originalDataType.equalsIgnoreCase("TIMESTAMP")) {
      return "datetime";
    } else if(originalDataType.equalsIgnoreCase("boolean")) {
      return "short";
    } else if(originalDataType.toLowerCase().contains("char")) {
      return "VARCHAR";
    }
    else {
      return super.mapDataType(originalDataType);
    }
  }

  protected Object mapValue(Object o) {
    if (o == null) {
      return null;
    }
    if (o instanceof String) {
      return ((String) o).replaceAll("'", escapedQuoteLiteral());
    } else if (o instanceof Timestamp) {
      final String result = o.toString().replaceAll("\\..+", "");  // replace trailing millisecs
      return result.trim().equals("") ? null : result;
    } else if (o instanceof byte[] && ((byte[]) o).length == 8) {
      return null;    // probably a ROWVERSION TIMESTAMP; we ignore these
    } else if (o instanceof byte[]) {
      return new BinaryString("0x" + super.mapValue(o));
    } else if (o instanceof Boolean) {
      return ((boolean) o) ? 1 : 0;
    } else {
      return super.mapValue(o);
    }
  }

  protected String tableStructureEntry(PropertyStructure propertyStructure) {
    final String result = super.tableStructureEntry(propertyStructure);
    if(result.toLowerCase().endsWith("varchar"))
      return result + "(MAX)";
    else
      return result;
  }

  @Override
  protected String commitStatement() {
    return "COMMIT;" + (addGoAfterTransactionCommit ? "\nGO\n" : "");
  }
}
