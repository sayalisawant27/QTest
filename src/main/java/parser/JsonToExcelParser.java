package parser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonToExcelParser {

    public static Workbook workbook = new HSSFWorkbook();
    public static Sheet sheet;
    public static int rowCount = 0;
    public static int requirementRowCount=0;
    public static int defectRowCount = 0;
    public static File file = new File("src/main/resources/QTestApi_Log.xls");


    public void createLog(JSONObject jsonObject, String message) throws Exception {
        sheet = workbook.createSheet("Authentication");
        OutputStream fileOut = new FileOutputStream(file);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        JSONArray keys = jsonObject.names();
        Row row1 = sheet.createRow(0);
        Row row2 = sheet.createRow(1);
        for (int i = 0; i < keys.length(); i++) {
            String key = keys.get(i).toString();
            String value = jsonObject.get(key).toString();

            row1.createCell(i).setCellValue(key);
            row2.createCell(i).setCellValue(value);
        }


        workbook.write(fileOut);
    }

    public void createLog(JSONArray jsonArray, String message) throws Exception {

        OutputStream fileOut = new FileOutputStream(file);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Row row1 = sheet.createRow(rowCount);
        row1.createCell(0).setCellValue(timestamp + "  " + message);
        rowCount += 2;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Iterator<String> keys = json.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                String value = json.get(key).toString();
                Row row = sheet.createRow(rowCount);
                row.createCell(0).setCellValue(key);
                row.createCell(1).setCellValue(value);

                rowCount++;
            }

        }
        workbook.write(fileOut);
        rowCount += 4;
    }

    public void requirementLog(List<String> columnKeys, JSONArray jsonArray) throws Exception {
        OutputStream fileOut = new FileOutputStream(file);
        String[] keys = {"web_url", "parent_id", "name", "last_modified_date", "rel:self", "rel:module", "pid", "id", "created_date", "field:status", "field:priority", "field:type", "field:assignedTo", "field:description", "order"};
        if (workbook.getSheetIndex("Requirements") == -1) {
            sheet = workbook.createSheet("Requirements");
            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < keys.length; i++) {
                titleRow.createCell(i).setCellValue(keys[i]);
            }
            requirementRowCount++;
        } else {
            sheet = workbook.getSheet("Requirements");
            int count = sheet.getLastRowNum() +1 ;
            System.out.println("count======="+count);
            requirementRowCount=count;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            Row valueRow = sheet.createRow(requirementRowCount++);
            int columnCount = 0;
            for (int j = 0; j < columnKeys.size(); j++) {
                String key = columnKeys.get(j);
                if (key.equals("links")) {
                    JSONArray linkArray = new JSONArray(jsonArray.getJSONObject(i).get("links").toString());
                    for (int k = 0; k < linkArray.length(); k++) {
                        JSONObject linkObject = linkArray.getJSONObject(k);
                        valueRow.createCell(columnCount++).setCellValue(linkObject.getString("href"));
                    }
                } else if (key.equals("properties")) {
                    JSONArray propertiesArray = new JSONArray(jsonArray.getJSONObject(i).get("properties").toString());
                    for (int k = 0; k < propertiesArray.length(); k++) {
                        JSONObject propertiesObject = propertiesArray.getJSONObject(k);
                        String field_id = propertiesObject.get("field_id").toString();
                        String field_value = propertiesObject.get("field_value").toString();
                        if (propertiesObject.has("field_value_name")) {
                            String field_value_name = propertiesObject.get("field_value_name").toString();
                            valueRow.createCell(columnCount++).setCellValue("[" + field_id + "] " + field_value + ", " + field_value_name);
                        } else {
                            valueRow.createCell(columnCount++).setCellValue("[" + field_id + "] " + field_value);
                        }
                    }
                } else {
                    // System.out.println(key+"========"+jsonArray.getJSONObject(i).get(key).toString());
                    valueRow.createCell(columnCount).setCellValue(jsonArray.getJSONObject(i).get(key).toString());
                    columnCount++;
                }
            }
        }
        workbook.write(fileOut);
    }

    public void defectLog(List<String> columnKeys, JSONArray jsonArray) throws Exception{
        OutputStream fileOut = new FileOutputStream(file);
        String[] keys = {"submitted_date", "web_url", "last_modified_date", "submitter_id", "last_modified_user_id", "rel:self", "pid",
                "id", "field:Summary", "field:Description", "field:Submitter", "field:Affected Release/Build",
                "field:Severity", "field:Fixed Release/Build", "field:Submitted Date", "field:Priority", "field:Root Cause",
                "field:Module", "field:Assigned To", "field:Status", "field:Type", "field:Target Release/Build", "field:Reason",
                "field:Category", "field:Target Date", "field:Closed Date", "field:Environment"};
        if (workbook.getSheetIndex("Defects") == -1) {
            sheet = workbook.createSheet("Defects");
            Row titleRow = sheet.createRow(0);
            for (int i = 0; i < keys.length; i++) {
                titleRow.createCell(i).setCellValue(keys[i]);
            }
            defectRowCount++;
        } else {
            sheet = workbook.getSheet("Defects");
            int count = sheet.getLastRowNum() +1 ;
            System.out.println("count======="+count);
            defectRowCount=count;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            Row valueRow = sheet.createRow(defectRowCount++);
            int columnCount = 0;
            for (int j = 0; j < columnKeys.size(); j++) {
                String key = columnKeys.get(j);
                if (key.equals("links")) {
                    JSONArray linkArray = new JSONArray(jsonArray.getJSONObject(i).get("links").toString());
                    for (int k = 0; k < linkArray.length(); k++) {
                        JSONObject linkObject = linkArray.getJSONObject(k);
                        valueRow.createCell(columnCount++).setCellValue(linkObject.getString("href"));
                    }
                } else if (key.equals("properties")) {
                    JSONArray propertiesArray = new JSONArray(jsonArray.getJSONObject(i).get("properties").toString());
                    for (int k = 0; k < propertiesArray.length(); k++) {
                        JSONObject propertiesObject = propertiesArray.getJSONObject(k);
                        String field_id = propertiesObject.get("field_id").toString();
                        String field_value = propertiesObject.get("field_value").toString();
                        if (propertiesObject.has("field_value_name")) {
                            String field_value_name = propertiesObject.get("field_value_name").toString();
                            valueRow.createCell(columnCount++).setCellValue("[" + field_id + "] " + field_value + ", " + field_value_name);
                        } else {
                            valueRow.createCell(columnCount++).setCellValue("[" + field_id + "] " + field_value);
                        }
                    }
                } else {
                    // System.out.println(key+"========"+jsonArray.getJSONObject(i).get(key).toString());
                    valueRow.createCell(columnCount).setCellValue(jsonArray.getJSONObject(i).get(key).toString());
                    columnCount++;
                }
            }
        }
        workbook.write(fileOut);
    }

    public void defectCommentLog(List<String> columnkeys, JSONObject jsonObject) throws Exception {

        OutputStream fileOut = new FileOutputStream(file);
        int commentCount=0;
        sheet = workbook.createSheet("Defect Comments");
        Row titleRow= sheet.createRow(0);
        String[] keys={"total", "links", "page", "item_created", "item_links:self", "item_links:defect", "item_id",
                "item_updated", "item_userId", "item_content", "page_size"};
        for(int i=0;i< keys.length;i++){
            titleRow.createCell(i).setCellValue(keys[i]);
        }commentCount++;
        JSONArray itemsArray = jsonObject.getJSONArray("items");
        Row valueRow;
        if(itemsArray.isEmpty()){
            valueRow= sheet.createRow(commentCount++);
            for(int i=0; i<columnkeys.size();i++){
                valueRow.createCell(i).setCellValue(jsonObject.get(columnkeys.get(i)).toString());
            }
        }
        else{
            for(int i=0; i<itemsArray.length();i++){
                valueRow=sheet.createRow(commentCount++);
                int columnCount=0;
                for(int j=0;j<columnkeys.size();j++){
                    String key = columnkeys.get(j);
                    if(key.equals("items")){
                        JSONObject itemsObject = itemsArray.getJSONObject(i);
                        JSONArray linksArray = new JSONArray(itemsArray.getJSONObject(i).get("links").toString());
                        Iterator<String> itemKey = itemsObject.keys();
                        if(itemsObject.has("updated")){
                            for (int k = 0; k < itemsObject.length(); k++) {
                                String item=itemKey.next();
                                if(item.equals("links")){
                                    for (int l = 0; l < linksArray.length(); l++) {
                                        JSONObject linkObject = linksArray.getJSONObject(l);
                                        valueRow.createCell(columnCount++).setCellValue(linkObject.getString("href"));
                                    }
                                } else{
                                    valueRow.createCell(columnCount).setCellValue(itemsObject.get(item).toString());
                                    columnCount++;
                                }
                            }
                        } else{
                            for (int k = 0; k < itemsObject.length(); k++) {
                                String item=itemKey.next();
                                if(item.equals("links")){
                                    for (int l = 0; l < linksArray.length(); l++) {
                                        JSONObject linkObject = linksArray.getJSONObject(l);
                                        valueRow.createCell(columnCount++).setCellValue(linkObject.getString("href"));
                                    }
                                } else if (titleRow.getCell(columnCount).getStringCellValue().equals("item_updated")){
                                    valueRow.createCell(columnCount++).setCellValue("-");
                                }
                                else{
                                    valueRow.createCell(columnCount++).setCellValue(itemsObject.get(item).toString());
                                }
                            }
                        }
                    } else{
                        valueRow.createCell(columnCount).setCellValue(jsonObject.get(key).toString());
                        columnCount++;
                    }
                }
            }
        }
        workbook.write(fileOut);
    }
}
