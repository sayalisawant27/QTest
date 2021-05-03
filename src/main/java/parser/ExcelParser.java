package parser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Iterator;

public class ExcelParser {

    public static Workbook workbook = new HSSFWorkbook();
    public static Sheet sheet = workbook.createSheet("API_Log");
    public static int rowCount = 0;
    public static File file = new File("src/main/resources/QTestApi_Log.xls");

    public ExcelParser() throws FileNotFoundException {
    }

    public void createLog(JSONObject jsonObject , String message) throws Exception {

        OutputStream fileOut = new FileOutputStream(file);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        sheet.setColumnWidth(0,20);
        sheet.setColumnWidth(1,40);
        JSONArray keys = jsonObject.names();
        Row row1 = sheet.createRow(rowCount);
        row1.createCell(0).setCellValue(timestamp + "  " + message);
        rowCount+=2;

        for (int i = 0, j = rowCount; i < keys.length(); i++ , j++) {
            String key = keys.get(i).toString();
            String value = jsonObject.get(key).toString();
            Row row = sheet.createRow(j);
            row.createCell(0).setCellValue(key);
            row.createCell(1).setCellValue(value);

            rowCount++;
        }

        workbook.write(fileOut);
        rowCount += 4;
    }

    public void createLog(JSONArray jsonArray, String message) throws Exception{

        OutputStream fileOut = new FileOutputStream(file);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Row row1 = sheet.createRow(rowCount);
        row1.createCell(0).setCellValue(timestamp + "  " + message);
        rowCount+=2;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Iterator<String> keys = json.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                String value= json.get(key).toString();
                Row row = sheet.createRow(rowCount);
                row.createCell(0).setCellValue(key);
                row.createCell(1).setCellValue(value);

                rowCount++;
            }

        }
        workbook.write(fileOut);
        rowCount += 4;
    }

}
