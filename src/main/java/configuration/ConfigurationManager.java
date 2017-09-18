package configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {

    private Properties properties;

    public void Load() {

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File configFile = new File(classLoader.getResource("config.properties").getFile());

            FileReader reader = new FileReader(configFile);
            properties = new Properties();
            properties.load(reader);
            reader.close();
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
