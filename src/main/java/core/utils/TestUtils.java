package core.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static Object[][] getTestData(String fileName, String sheetName) {

        String path = "test-data/" + fileName;

        try (InputStream is = TestUtils.class
                .getClassLoader()
                .getResourceAsStream(path);
             Workbook workbook = new XSSFWorkbook(is)) {

            if (is == null) {
                throw new RuntimeException("File not found: " + path);
            }

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            int rows = sheet.getPhysicalNumberOfRows();
            int cols = sheet.getRow(0).getPhysicalNumberOfCells();

            List<Object[]> dataList = new ArrayList<>();

            for (int i = 1; i < rows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Object[] rowData = new Object[cols];

                for (int j = 0; j < cols; j++) {
                    Cell cell = row.getCell(j);
                    rowData[j] = getCellValue(cell);
                }

                dataList.add(rowData);
            }

            return dataList.toArray(new Object[0][]);

        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel", e);
        }
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getDateCellValue()
                    : (cell.getNumericCellValue() % 1 == 0)
                    ? (int) cell.getNumericCellValue()
                    : cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            default -> "";
        };
    }
}