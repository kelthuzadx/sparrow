package core;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHolder {
    private static final Logger logger = Logger.getLogger(Sparrow.class);
    private static final PropertiesHolder propertiesHolder = new PropertiesHolder();

    private Properties props;

    private PropertiesHolder() {
        props = new Properties();
        try {
            props.load(new FileInputStream("src/main/resources/" + Configurator.propertiesFileName));
            logger.info("found " + Configurator.propertiesFileName + " for sparrow");
        } catch (IOException e) {
            logger.warn("failed to read " + Configurator.propertiesFileName);
        }
    }

    public static String readProp(String key) {
        return propertiesHolder.props.getProperty(key);
    }

}
