package api;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import parser.JsonToExcelParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefectAPI {

    public static List<String> defectKeys = new ArrayList<>();
    public static List<String> commentKeys = new ArrayList<>();

    private void initDefectAPI(JSONArray jsonArray) {
        Iterator<String> keyNames = jsonArray.getJSONObject(0).keys();
        while (keyNames.hasNext()) {
            String key= keyNames.next();
            defectKeys.add(key);
            System.out.println(key);
        }
    }

    private void initDefectCommentAPI(JSONObject jsonObject){
        Iterator<String> keyNames = jsonObject.keys();
        while (keyNames.hasNext()) {
            String key= keyNames.next();
            commentKeys.add(key);
            System.out.println(key);
        }

    }

    public void getDefect(String domain, String tokenType, String accessToken, String projectId, String defectId) throws Exception {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://" + domain + "/api/v3/projects/" + projectId + "/defects/" + defectId)
                .get()
                .addHeader("authorization", tokenType + " " + accessToken)
                .build();

        Response response = client.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        initDefectAPI(jsonArray);

        JsonToExcelParser jsonToExcelParser = new JsonToExcelParser();
        jsonToExcelParser.defectLog(defectKeys, jsonArray);

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
        JsonToExcelParser jsonToExcelParser = new JsonToExcelParser();
        jsonToExcelParser.createLog(jsonObject, "Comment added to defect Id: "+defectId+"...");
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

        JSONObject jsonObject = new JSONObject(response.body().string());
        initDefectCommentAPI(jsonObject);

        JsonToExcelParser jsonToExcelParser = new JsonToExcelParser();
        jsonToExcelParser.defectCommentLog(commentKeys,jsonObject);
    }

}
