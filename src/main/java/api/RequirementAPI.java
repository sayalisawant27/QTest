package api;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import parser.JsonToExcelParser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RequirementAPI {

    public JsonToExcelParser jsonToExcelParser;
    public static List<String> columnKeys = new ArrayList<String>();
    public static List<String> linkKeys = new ArrayList<String>();
    public static List<String> propertiesKeys = new ArrayList<String>();

    public void initRequirementAPI(JSONArray jsonArray) {
        Iterator<String> keyNames = jsonArray.getJSONObject(0).keys();
        while (keyNames.hasNext()) {
            String key= keyNames.next();
            columnKeys.add(key);
        }



        /*for (int i = 0; i < jsonArray.length(); i++) {
            for (int j = 0; j < ((JSONArray) jsonArray.getJSONObject(i).get("links")).length(); j++) {
                JSONArray linkArray = new JSONArray(jsonArray.getJSONObject(i).get("links").toString());
                Iterator<String> linkKeyNames = linkArray.getJSONObject(0).keys();
                while (linkKeyNames.hasNext()) {
                    linkKeys.add(linkKeyNames.next());
                }
            }

            for (int j = 0; j < ((JSONArray) jsonArray.getJSONObject(i).get("properties")).length(); j++) {
                JSONArray propertiesArray = new JSONArray(jsonArray.getJSONObject(i).get("properties").toString());
                Iterator<String> propertyKeyNames = propertiesArray.getJSONObject(0).keys();
                while (propertyKeyNames.hasNext()) {
                    propertiesKeys.add(propertyKeyNames.next());
                }
            }
        }*/
    }

    public void getMultipleRequirements(String domain, String tokenType, String accessToken, String projectId) throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://" + domain + "/api/v3/projects/" + projectId + "/requirements")
                .get()
                .addHeader("authorization", tokenType + " " + accessToken)
                .build();

        Response response = client.newCall(request).execute();
        JSONArray jsonArray = new JSONArray(response.body().string());
        // System.out.println(jsonArray.toString());
        initRequirementAPI(jsonArray);

        jsonToExcelParser = new JsonToExcelParser();
        jsonToExcelParser.requirementLog(columnKeys,jsonArray);

    }



    public void deleteRequirement(String domain, String tokenType, String accessToken, String projectId, String requirementId) throws Exception{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://" + domain + "/api/v3/projects/"+ projectId +"/requirements/"+ requirementId)
                .delete(null)
                .addHeader("authorization", tokenType+" "+accessToken)
                .build();

        Response response = client.newCall(request).execute();

    }


}
