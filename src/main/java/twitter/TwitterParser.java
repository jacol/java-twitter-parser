package twitter;

import org.json.JSONObject;

public class TwitterParser {
    public String Parse(String fullJson) {
        JSONObject jsonObj = new JSONObject(fullJson);

        JSONObject essentials;

        if(jsonObj.has("retweeted_status")){
            essentials = getTweetEssentials(jsonObj.getJSONObject("retweeted_status"));
        }
        else if(jsonObj.has("quoted_status")){
            essentials = getTweetEssentials(jsonObj.getJSONObject("quoted_status"));
        }
        else {
            essentials = getTweetEssentials(jsonObj);
        }

        return essentials.toString();
    }

    private JSONObject getTweetEssentials(JSONObject jsonObject){
        JSONObject result = new JSONObject();

        result.put("text", jsonObject.getString("text"));

        return result;
    }
}
