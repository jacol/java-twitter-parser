package twitter;

import org.json.JSONObject;

public class TwitterParser {
    public String Parse(String fullJson, String keyword) {
        JSONObject jsonObj = new JSONObject(fullJson);

        JSONObject essentials;

        if(jsonObj.has("quoted_status")){
            essentials = getTweetEssentials(jsonObj.getJSONObject("quoted_status"));
        }
        else if(jsonObj.has("retweeted_status")){
            essentials = getTweetEssentials(jsonObj.getJSONObject("retweeted_status"));
        }
        else {
            essentials = getTweetEssentials(jsonObj);
        }

        essentials.put("keyword", keyword);

        return essentials.toString();
    }

    private JSONObject getTweetEssentials(JSONObject jsonObject){
        JSONObject result = new JSONObject();

        result.put("text", jsonObject.getString("text"));
        result.put("twitter_id", jsonObject.getString("id_str"));

        JSONObject user = jsonObject.getJSONObject("user");

        result.put("twitter_nick", user.getString("screen_name"));
        result.put("followers", user.getInt("followers_count"));

        return result;
    }
}
