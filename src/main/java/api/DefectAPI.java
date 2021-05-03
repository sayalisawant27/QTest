package api;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import parser.ExcelParser;

public class DefectAPI {

    public void getDefect(String domain, String tokenType, String accessToken, String projectId, String defectId) throws Exception {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://" + domain + "/api/v3/projects/" + projectId + "/defects/" + defectId)
                .get()
                .addHeader("authorization", tokenType + " " + accessToken)
                .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();

        JSONObject jsonObject = new JSONObject(jsonData);
        ExcelParser excelParser = new ExcelParser();
        excelParser.createLog(jsonObject, "Defect details of defect Id: " + defectId + "...");

    }

    public void addCommentToDefect(String domain, String tokenType, String accessToken, String projectId, String defectId, String comment) throws Exception {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"created\": \"2021-05-02T10:09:40.450Z\",\r\n  \"updated\":" +
                " \"2021-05-02T10:09:40.450Z\",\r\n  \"userId\": 5202,\r\n  \"id\": 2602608,\r\n  \"content\": \"" + comment + "\" \r\n}");
        Request request = new Request.Builder()
                .url("https://" + domain + "/api/v3/projects/" + projectId + "/defects/" + defectId + "/comments")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", tokenType + " " + accessToken)
                .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();

        JSONObject jsonObject = new JSONObject(jsonData);
        ExcelParser excelParser = new ExcelParser();
        excelParser.createLog(jsonObject, "Comment added to defect Id: "+defectId+"...");
    }

    public void getAllCommentsOfDefect(String domain, String tokenType, String accessToken, String projectId, String defectId) throws Exception{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://"+domain+"/api/v3/projects/"+projectId+"/defects/"+defectId+"/comments")
                .get()
                .addHeader("authorization", tokenType + " " + accessToken)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();

        JSONObject jsonObject = new JSONObject(jsonData);
        ExcelParser excelParser = new ExcelParser();
        excelParser.createLog(jsonObject, "Comments of defect Id: "+defectId+"...");
    }

}
