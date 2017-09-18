package configuration;

import java.io.*;
import java.util.Properties;

public class ConfigurationManager {

    private Properties properties;

    public void Load() {

        try {
            properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public String GetProperty(String key){
        if(properties == null) Load();
        return properties.getProperty(key);
    }
}
