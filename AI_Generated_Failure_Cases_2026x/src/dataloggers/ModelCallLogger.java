package dataloggers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author kata_du
 * @version 1.1.0
 * @date 2025-12-18
 * @summary The `ModelCallLogger` class logs detailed information about model interactions in two phases: request sent and response received.
 */
public class ModelCallLogger {
    
    String userHome = System.getProperty("user.home");
    String FILE_PATH = userHome + File.separator + "model_logs.xlsx";
    private final BackgroundRunner BR;
    private final String SHEET_NAME = "Data";
    
    public ModelCallLogger(BackgroundRunner runner) {
        this.BR = runner;
    }

    /**
     * Phase 1: Logs the request details immediately before calling the LLM.
     * Returns the Excel row index so it can be updated later.
     */
    public synchronized int logRequestSent(int tryNumber, String modelName, String sysMsg, String prompt) throws IOException {
        File file = new File(FILE_PATH);
        Workbook workbook;
        
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = WorkbookFactory.create(fis);
            }
        } else {
            workbook = new XSSFWorkbook();
        }

        Sheet sheet = workbook.getSheet(SHEET_NAME);
        if (sheet == null) {
            sheet = workbook.createSheet(SHEET_NAME);
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Time");
            header.createCell(2).setCellValue("Parsed Response");
            header.createCell(3).setCellValue("Try Number");
            header.createCell(4).setCellValue("Model Name");
            header.createCell(5).setCellValue("System Message");
            header.createCell(6).setCellValue("Prompt Message");
            header.createCell(7).setCellValue("Response");
        }

        int nextRowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(nextRowNum);
        
        LocalDateTime now = LocalDateTime.now();
        row.createCell(0).setCellValue(now.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        row.createCell(1).setCellValue(now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        
        // Write placeholders while waiting for the model
        row.createCell(2).setCellValue("WAITING..."); 
        row.createCell(3).setCellValue(tryNumber);
        row.createCell(4).setCellValue(modelName);
        row.createCell(5).setCellValue(sysMsg);
        row.createCell(6).setCellValue(prompt);
        row.createCell(7).setCellValue("WAITING..."); 

        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();

        BR.Notifyinbackground("IDLog_Req", "Request logged to Excel. Waiting for response...");
        return nextRowNum;
    }

    /**
     * Phase 2: Updates the previously created row with the final response.
     */
    public synchronized void logResponseReceived(int rowIndex, String result, String response) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        Workbook workbook;
        try (FileInputStream fis = new FileInputStream(file)) {
            workbook = WorkbookFactory.create(fis);
        }
        
        Sheet sheet = workbook.getSheet(SHEET_NAME);
        if (sheet != null) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                // Update only the result and response columns
                row.getCell(2).setCellValue(result);
                row.getCell(7).setCellValue(response);
                
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(7);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();

        BR.Notifyinbackground("IDLog_Res", "Excel log updated with response data.");
    }
}