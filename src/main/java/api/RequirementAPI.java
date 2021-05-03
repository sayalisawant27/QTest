package api;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import parser.ExcelParser;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class RequirementAPI {

    public ExcelParser excelParser;
    public List<String> columnKeys;
    public List<String> linkKeys;
    public List<String> propertiesKeys;

    public void initRequirementAPI(JSONArray jsonArray){
        Iterator<String> keyNames= jsonArray.getJSONObject(0).keys();
        while(keyNames.hasNext()) {
            columnKeys.add(keyNames.next());
        }
        for(int i=0; i<jsonArray.length();i++) {
            for (int j = 0; j < ((JSONArray) jsonArray.getJSONObject(i).get("links")).length(); j++) {
                JSONArray linkArray = new JSONArray(jsonArray.getJSONObject(i).get("links").toString());
                Iterator<String> linkKeyNames= linkArray.getJSONObject(0).keys();
                while(linkKeyNames.hasNext()) {
                    linkKeys.add(linkKeyNames.next());
                }
            }

            for (int j = 0; j < ((JSONArray) jsonArray.getJSONObject(i).get("properties")).length(); j++) {
                JSONArray propertiesArray = new JSONArray(jsonArray.getJSONObject(i).get("properties").toString());
                Iterator<String> propertyKeyNames= propertiesArray.getJSONObject(0).keys();
                while(propertyKeyNames.hasNext()) {
                   propertiesKeys.add(propertyKeyNames.next());
                }
            }
        }
    }

    public void getMultipleRequirements(String domain, String tokenType, String accessToken, String projectId) throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://"+domain +"/api/v3/projects/"+projectId+"/requirements")
                .get()
                .addHeader("authorization", tokenType+" "+accessToken)
                .build();

        Response response = client.newCall(request).execute();
        JSONArray jsonArray = new JSONArray(response.body().string());
        initRequirementAPI(jsonArray);

       // JSONArray linkArray= new JSONArray(jsonArray.getJSONObject(0).get("links").toString());
        for(int i=0; i<jsonArray.length();i++){
            for(int j=0; j<((JSONArray) jsonArray.getJSONObject(i).get("links")).length();j++){
                JSONArray linkArray= new JSONArray(jsonArray.getJSONObject(i).get("links").toString());
                linkArray.forEach(linkItem-> {
                    JSONObject linkObject = (JSONObject) linkItem;
                    System.out.println(linkObject);
                });
            }

            for(int j=0; j<((JSONArray) jsonArray.getJSONObject(i).get("properties")).length();j++){
                JSONArray propertiesArray= new JSONArray(jsonArray.getJSONObject(i).get("properties").toString());
                propertiesArray.forEach(propertiesItem-> {
                    JSONObject propertiesObject = (JSONObject) propertiesItem;
                    System.out.println(propertiesObject.get("field_id"));
                });
            }
            }
        }

       // excelParser = new ExcelParser();
       // excelParser.createLog(jsonArray, "Requirements from Project "+projectId+"..");




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
