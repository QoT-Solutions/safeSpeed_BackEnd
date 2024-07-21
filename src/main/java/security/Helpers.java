package security;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Helpers {

    public static String getProperty(String name){
        Properties properties = new Properties();
        String property = "";

        try {
            InputStream inputStream = new FileInputStream("../resources/application.properties");
            properties.load(inputStream);

            // Reading each property
            property = properties.getProperty(name);
        } catch (Exception e){

        }
        System.out.println(name+property);
        return property;
    }
}
