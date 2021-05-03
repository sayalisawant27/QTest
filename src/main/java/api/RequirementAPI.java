package api;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import parser.ExcelParser;
import org.json.JSONObject;

public class RequirementAPI {

    public ExcelParser excelParser;

    public void getMultipleRequirements(String domain, String tokenType, String accessToken, String projectId) throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://"+domain +"/api/v3/projects/"+projectId+"/requirements")
                .get()
                .addHeader("authorization", tokenType+" "+accessToken)
                .build();

        Response response = client.newCall(request).execute();
        JSONArray jsonArray = new JSONArray(response.body().string());

        excelParser = new ExcelParser();
        excelParser.createLog(jsonArray, "Requirements from Project "+projectId+"..");


    }

    public void deleteRequirement(String domain, String tokenType, String accessToken, String projectId, String requirementId) throws Exception{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://" + domain + "/api/v3/projects/"+ projectId +"/requirements/"+ requirementId)
                .delete(null)
                .addHeader("authorization", tokenType+" "+accessToken)
                .build();

        Response response = client.newCall(request).execute();

        getMultipleRequirements(domain, tokenType, accessToken, projectId);

    }

}
