package core;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHolder {
    private static final Logger logger = Logger.getLogger(Sparrow.class);
    private static final PropertiesHolder propertiesHolder = new PropertiesHolder();

    private Properties props;

    private PropertiesHolder() {
        props = new Properties();

        try (FileInputStream fi = new FileInputStream(Configurator.propertiesFileName);
             InputStream in = new BufferedInputStream(fi)) {
            props.load(in);
            logger.info("found " + Configurator.propertiesFileName + " for sparrow");
        } catch (IOException e) {
            logger.warn("failed to read " + Configurator.propertiesFileName);
        }
    }

    public static String readProp(String key) {
        return propertiesHolder.props.getProperty(key);
    }

}
