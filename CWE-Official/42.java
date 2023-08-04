public class SomeAppClass {
    public static String appPropertiesFile = "app/Application.properties";
    
    // Initializing the appProperties object
    public Properties appProperties = new Properties();
    
    // Constructor to load properties from file
    public SomeAppClass() {
        try {
            FileInputStream fis = new FileInputStream(appPropertiesFile);
            appProperties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
