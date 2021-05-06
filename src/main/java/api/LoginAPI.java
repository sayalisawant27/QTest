package api;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import parser.JsonToExcelParser;

public class LoginAPI {

    public JSONObject authenticate(String domain, String username, String password) throws Exception{
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=password&username="+username+"&password="+password);
        Request request = new Request.Builder()
                .url("https://"+domain+"/oauth/token")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("authorization", "Basic c2F5YWxpLnNhd2FudEBjb2duaXphbnQuY29tOg==")
                .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();

        JSONObject jsonObject=new JSONObject(jsonData);
        JsonToExcelParser jsonToExcelParser = new JsonToExcelParser();
        jsonToExcelParser.createLog(jsonObject, "Login Authentication...");


        return jsonObject;
    }

}
