package ru.ibs.gasu.gchp.util;

import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

// костыль для сдачи, не стал тащить в api
public class OdsConverter {

    @SneakyThrows
    public static byte[] convertXlsxToOds(byte[] xlsx) {
        ByteArrayInputStream bis = new ByteArrayInputStream(xlsx);
        XSSFWorkbook workbook = new XSSFWorkbook(bis);

        XSSFSheet sheet = workbook.getSheet("Export");

        List<String> columns = new ArrayList<>();
        for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
            columns.add(sheet.getRow(0).getCell(j).getStringCellValue());
        }
        String[] arrayColumns = columns.toArray(new String[0]);

        final Object[][] dataArray = new Object[sheet.getLastRowNum() + 1][sheet.getRow(0).getLastCellNum() + 1];

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++) {
                if (sheet.getRow(i).getCell(j).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
                    if (HSSFDateUtil.isCellDateFormatted(sheet.getRow(i).getCell(j))) {
                        dataArray[i - 1][j] = dateFormatter.format(sheet.getRow(i).getCell(j).getDateCellValue());
                    } else {
                        double value = sheet.getRow(i).getCell(j).getNumericCellValue();
                        if (value == (long) value)
                            dataArray[i - 1][j] = String.format("%d", (long) value);
                        else if (value - (long) value > 0) {
                            DecimalFormat format = new DecimalFormat("0.#");
                            dataArray[i - 1][j] = format.format(value);
                        } else
                            dataArray[i - 1][j] = String.format("%s", value);
                    }
                }
                if (sheet.getRow(i).getCell(j).getCellType() == Cell.CELL_TYPE_STRING)
                    dataArray[i - 1][j] = sheet.getRow(i).getCell(j).getStringCellValue();
            }
        }

        Path temp = Files.createTempFile("report_", ".ods");
        File file = temp.toFile();

        TableModel model = new DefaultTableModel(dataArray, arrayColumns);
        SpreadSheet odsSheet = SpreadSheet.createEmpty(model);
        for (int j = 0; j < odsSheet.getFirstSheet().getColumnCount(); j++) {
            odsSheet.getFirstSheet().getColumn(j).setWidth(50);
        }

        odsSheet.saveAs(file);
        byte[] result = Files.readAllBytes(file.toPath());
        file.delete();
        return result;
    }

}
