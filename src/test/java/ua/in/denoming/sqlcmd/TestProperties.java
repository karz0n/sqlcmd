package ua.in.denoming.sqlcmd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestProperties {
    private static final String PATH = "test.properties";
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost/sqlcmd";
    private static final String DEFAULT_USERNAME = "postgresql";
    private static final String DEFAULT_PASSWORD = "postgresql";

    private Properties properties;

    public TestProperties() {
        properties = TestProperties.getProperties();
    }

    public String getDatabaseUrl() {
        return properties.getProperty("jdbc.url");
    }

    public String getDatabaseUserName() {
        return properties.getProperty("jdbc.username");
    }

    public String getDatabasePassword() {
        return properties.getProperty("jdbc.password");
    }

    public String getDatabaseConnectionString() {
        return getDatabaseUrl() + " " + getDatabaseUserName() + " " + getDatabasePassword();
    }

    private static Properties getProperties() {
        Properties properties = new Properties(TestProperties.getDefaultProperties());
        try (
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PATH)
        ) {
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private static Properties getDefaultProperties() {
        Properties defaults = new Properties();
        defaults.setProperty("jdbc.url", DEFAULT_URL);
        defaults.setProperty("jdbc.username", DEFAULT_USERNAME);
        defaults.setProperty("jdbc.password", DEFAULT_PASSWORD);
        return defaults;
    }
}
