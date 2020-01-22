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

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class PDFBinarySanitizer implements ValueSanitizer {

  private static final String TEMP_FILE_NAME = "TemporaryFileForTestDataCreator.pdf";

  /**
   *
   * @param originalValue original value, which may be sensitive
   * @param rowId if available, unique id identifying the row on which this value sits
   * @param propertyName name of the property
   * @return
   */
  @Override
  public Object sanitize(Object originalValue, String rowId, String propertyName) {
    if(originalValue == null)
      return null;

    final PdfDocument pdfDoc;
    try {
      pdfDoc = new PdfDocument(new PdfWriter(TEMP_FILE_NAME));
      final Document document = new Document(pdfDoc, pdfDoc.getDefaultPageSize(), true);
      document.add(new Paragraph("rowId: " + rowId + ", propertyName: " + propertyName));
      document.close();
      pdfDoc.close();
      return FileUtils.readFileToByteArray(new File(TEMP_FILE_NAME));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
