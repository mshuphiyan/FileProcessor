package com.file.fileprocessor.service;


import com.file.fileprocessor.model.CSVFileModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@Scope("step")
public class SaveAsXml implements ItemWriter<CSVFileModel> {
    private static final String FILE_NAME = "";
    private static final String[] HEADERS = { "Symbol", "Name", "Last Sale", "Market Cap", "ADR TSO" };

    private String outputFilename;
    private Workbook workbook;
    private CellStyle dataCellStyle;
    private int currRow = 0;

    private void addHeaders(Sheet sheet) {

        Workbook wb = sheet.getWorkbook();

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();

        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font);

        Row row = sheet.createRow(2);
        int col = 0;

        for (String header : HEADERS) {
            Cell cell = row.createCell(col);
            cell.setCellValue(header);
            cell.setCellStyle(style);
            col++;
        }
        currRow++;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Calling beforeStep");

        outputFilename = FILE_NAME + ".xlsx";

        workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("report");
        sheet.setDefaultColumnWidth(20);

        currRow++;
        addHeaders(sheet);

    }

    @AfterStep
    public void afterStep(StepExecution stepExecution) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFilename);
        workbook.write(fos);
        fos.close();
    }

    @Override
    public void write(List<? extends CSVFileModel> list) throws Exception {
        Sheet sheet = workbook.getSheetAt(0);

        for (CSVFileModel data : list) {
            for (int i = 0; i < 300; i++) {
                currRow++;
                Row row = sheet.createRow(currRow);
                createStringCell(row, data.getAnzsic06(), 0);
                createStringCell(row, data.getArea(), 1);
                createStringCell(row, data.getYear(), 2);
                createStringCell(row, data.getGeoCount(), 3);
                createStringCell(row, data.getEcCount(), 4);
            }
        }
    }

    private void createStringCell(Row row, String val, int col) {
        Cell cell = row.createCell(col);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(val);
    }

}
