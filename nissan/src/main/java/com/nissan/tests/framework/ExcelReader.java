package com.nissan.tests.framework;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * Reading the contents of an Excel datasheet in a testNG data provider
 * compatible format
 *
 * @author vlado a.
 *
 */
public class ExcelReader {

  /**
   * Read a worksheet from Excel in a format suitable for the TestNG data
   * provider. The first (header) row is skipped. The first column should
   * contain priorities for the test data combination and it also is skipped.
   * Only test data combinations with priorities <= the system-defined
   * LowestTestPriority are included in the output
   *
   * @param file
   * @param worksheet
   * @return
   * @throws InvalidFormatException
   * @throws IOException
   */
  public static Object[][] getDataFromExcel(String file, String worksheet)
      throws InvalidFormatException, IOException {

    int maxPriority = Integer.parseInt(WdEx.getSystemProperty("LowestTestPriority"));
    boolean defaultBrowser = WdEx.getSystemProperty("RunOnDefaultBrowserProfile").toLowerCase().equals("true");

    Workbook myWorkBook = WorkbookFactory.create(new FileInputStream(file));
    Sheet mySheet = myWorkBook.getSheet(worksheet);

    ArrayList<Object[]> outputRows = new ArrayList<Object[]>();
    for (Row row : mySheet) {
      ArrayList<Object> outputCells = new ArrayList<Object>();
      int column = 0;
      boolean skipThisRow = false;
      for (Cell cell : row) {
        Object cellValue = null;
        switch (cell.getCellType()) {
          case Cell.CELL_TYPE_STRING:
            cellValue = cell.getStringCellValue();
            break;
          case Cell.CELL_TYPE_NUMERIC:
            cellValue = cell.getNumericCellValue();
            break;
          case Cell.CELL_TYPE_BOOLEAN:
            cellValue = cell.getBooleanCellValue();
            break;
          case Cell.CELL_TYPE_FORMULA:
            // Assuming that formulas will produce numbers
            cellValue = cell.getNumericCellValue();
            break;
          default :
        }
        column++;
        if (column == 1) {
          int prio = 0;
          try {
            prio = (new Double(cellValue.toString())).intValue();
          }
          catch (Exception e) {
            // Leave 0
          }
          if (prio > maxPriority) {
            // This data combination has too low priority, skip this row
            // altogether
            skipThisRow = true;
            break;
          }
        } else if (column == 2) {
          // Handle the Default override for the browser profiles
          outputCells.add(defaultBrowser ? "Default" : cellValue);
        } else {
          outputCells.add(cellValue);
        }
      }
      if (!skipThisRow) {
        outputRows.add(outputCells.toArray());
      }
    }

    // Transform the data into the format required by testNG, skipping the
    // header row
    Object[][] result = new Object[outputRows.size() - 1][outputRows.get(0).length];
    for (int i = 0; i < result.length; i++) {
      result[i] = outputRows.get(i + 1);
    }

    return result;
  }
}
