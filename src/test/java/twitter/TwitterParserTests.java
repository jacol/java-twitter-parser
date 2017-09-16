package twitter;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class TwitterParserTests {

    private TwitterParser parser;

    @BeforeEach
    public void init(){
        parser = new TwitterParser();
    }

    @Test
    public void Retweeted_GetEssentials_Fetched() throws IOException {
        String json = getJsonFromResources("/twitter/retweeted.json");
        String result = parser.Parse(json);
        System.out.println(result);
    }

    private String getJsonFromResources(String path) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(path);
        return IOUtils.toString(inputStream);
    }
}
