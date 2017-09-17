package twitter;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TwitterParserTests {

    private TwitterParser parser;

    @BeforeEach
    public void init(){
        parser = new TwitterParser();
    }

    @Test
    public void Retweeted_GetEssentials_Fetched() throws IOException {
        String json = getJsonFromResources("/twitter/retweeted.json");
        JSONObject result = new JSONObject(parser.Parse(json, "test"));

        assertNotNull(result.get("text"));
        assertNotNull(result.get("twitter_id"));
        assertNotNull(result.get("twitter_nick"));
        assertNotNull(result.get("followers"));
        assertNotNull(result.get("keyword"));
        assertNotNull(result.get("created_at"));
        assertNotNull(result.get("lang"));

        assertEquals("_Bshadanti", result.get("twitter_nick"));
    }

    @Test
    public void Quoted_GetEssentials_Fetched() throws IOException {
        String json = getJsonFromResources("/twitter/quoted_retweeted.json");
        JSONObject result = new JSONObject(parser.Parse(json, "test"));

        assertNotNull(result.get("text"));
        assertNotNull(result.get("twitter_id"));
        assertNotNull(result.get("twitter_nick"));
        assertNotNull(result.get("followers"));
        assertNotNull(result.get("keyword"));
        assertNotNull(result.get("created_at"));
        assertNotNull(result.get("lang"));

        assertEquals("Berlustinho", result.get("twitter_nick"));
    }

    @Test
    public void Regular_GetEssentials_Fetched() throws IOException {
        String json = getJsonFromResources("/twitter/regular.json");
        JSONObject result = new JSONObject(parser.Parse(json, "test"));

        assertNotNull(result.get("text"));
        assertNotNull(result.get("twitter_id"));
        assertNotNull(result.get("twitter_nick"));
        assertNotNull(result.get("followers"));
        assertNotNull(result.get("keyword"));
        assertNotNull(result.get("created_at"));
        assertNotNull(result.get("lang"));

        assertEquals("HerminioAndujar", result.get("twitter_nick"));
    }

    private String getJsonFromResources(String path) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(path);
        return IOUtils.toString(inputStream);
    }
}
