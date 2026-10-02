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
 * @version 1.0.0
 * @date 2025-12-18
 * @summary The `ModelCallLogger` class is primarily responsible for logging detailed information about model interactions and results.
 */
public class ModelCallLogger {
	 /** Path to the Excel file (will be created if it does not exist). */
	String userHome = System.getProperty("user.home");
	String FILE_PATH = userHome + File.separator + "model_logs.xlsx";

	// --- MODIFICATION: Use an instance variable and constructor injection ---
	private final BackgroundRunner BR;

    /** Sheet name inside the workbook. */
    private final String SHEET_NAME = "Data";
    
    // Constructor to receive the BackgroundRunner instance
/**
 * @param runner The `runner` parameter is an instance of the `BackgroundRunner` class that handles the background execution of model calls within the OSIRIS plugin.
 * @return The return value of the `ModelCallLogger` method, which is an instance of `ModelCallLogger`, represents a configured logger that will use the provided `BackgroundRunner` for executing logging tasks in the background.
 * @summary The `ModelCallLogger` class is responsible for logging model calls within a background runner context, ensuring that the interactions between models and their execution in the background are tracked and recorded efficiently.
 */
    public ModelCallLogger(BackgroundRunner runner) {
        this.BR = runner;
    }

/**
 * @param tryNumber `tryNumber`: The attempt count for generating or refining failure conditions, used to track and log each try within the process.
 * @param modelName The `modelName` parameter in the `log` method specifies the name of the model being processed, which is recorded as part of the log entry for tracking and identification purposes.
 * @param sysMsg The `sysMsg` parameter is a string representing the system message associated with the model's log entry, capturing any pertinent details or status updates relevant to the processing context.
 * @param prompt The `prompt` parameter in the `log` method is a String representing the input prompt message used to generate a response from the model.
 * @param result The `result` parameter is a `String` containing the parsed response data to be logged for the current attempt.
 * @param response The `response` parameter in the `log` method is a String that contains the LLM's generated response to be logged for traceability and debugging purposes.
 * @summary The `log` method records detailed information about a model's execution, including the date, time, parsed response, try number, model name, system message, prompt message, and response, into an Excel workbook for traceability and debugging purposes.
 */
    public void log(int tryNumber, String modelName, String sysMsg, String prompt, String result, String response) throws IOException {
        File file = new File(FILE_PATH);
        Workbook workbook;
        Sheet sheet;

        // Debug helper
        StringBuilder debug = new StringBuilder();
        debug.append("=== Model LOG ===\n");
        debug.append("File path: ").append(FILE_PATH).append("\n");
        debug.append("File exists: ").append(file.exists()).append("\n");

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = WorkbookFactory.create(fis);
                debug.append("Opened existing workbook.\n");
            }
        } else {
            workbook = new XSSFWorkbook();
            debug.append("Created new workbook.\n");
        }

        /* ------------------------------------------------------------------ */
        /* 2. Get or create the sheet -------------------------------------- */
        /* ------------------------------------------------------------------ */

        sheet = workbook.getSheet(SHEET_NAME);
        if (sheet == null) {
            sheet = workbook.createSheet(SHEET_NAME);
            debug.append("Created new sheet: ").append(SHEET_NAME).append("\n");

            // Header row (7 columns)
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Date");
            header.createCell(1).setCellValue("Time");
            header.createCell(2).setCellValue("Parsed Response");
            header.createCell(3).setCellValue("Try Number");
            header.createCell(4).setCellValue("Model Name");
            header.createCell(5).setCellValue("System Message");
            header.createCell(6).setCellValue("Prompt Message");
            header.createCell(7).setCellValue("Response");
        } else {
            debug.append("Found existing sheet: ").append(SHEET_NAME).append("\n");
        }

        /* ------------------------------------------------------------------ */
        /* 3. Append a new row --------------------------------------------- */
        /* ------------------------------------------------------------------ */

        int nextRowNum = sheet.getLastRowNum() + 1;   // Skip header if present
        Row row = sheet.createRow(nextRowNum);
        debug.append("Appending row #").append(nextRowNum).append("\n");

        // Current date & time
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.toLocalDate()
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
        String timeStr = now.toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        row.createCell(0).setCellValue(dateStr);
        row.createCell(1).setCellValue(timeStr);
        row.createCell(2).setCellValue(result);
        row.createCell(3).setCellValue(tryNumber);
        row.createCell(4).setCellValue(modelName);
        row.createCell(5).setCellValue(sysMsg);
        row.createCell(6).setCellValue(prompt);
        row.createCell(7).setCellValue(response);

        /* ------------------------------------------------------------------ */
        /* 4. Autosize columns --------------------------------------------- */
        /* ------------------------------------------------------------------ */

        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        /* ------------------------------------------------------------------ */
        /* 5. Write workbook back to file --------------------------------- */
        /* ------------------------------------------------------------------ */

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
            debug.append("Workbook written successfully.\n");
        }

        // Close workbook to release resources
        workbook.close();

        /* ------------------------------------------------------------------ */
        /* 6. Print the debug output --------------------------------------- */
        /* ------------------------------------------------------------------ */

        BR.Notifyinbackground("IDLog1",debug.toString());
    }
}
