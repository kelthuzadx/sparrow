package core;

public final class Configurator {
    static String propertiesFileName = "sparrow.properties";

    static String jspDocBase = "src/main/webapp/";
    static boolean enableThymeleaf = true;
    // To support chain-calling
    private static Configurator conf = new Configurator();

    private Configurator() {
    }

    public static Configurator setJspBase(String docPath) {
        jspDocBase = docPath;
        return conf;
    }

    public static Configurator disableThymeleaf() {
        enableThymeleaf = false;
        return conf;
    }

    public static Configurator setSparrowProperties(String propertiesFileName) {
        Configurator.propertiesFileName = propertiesFileName;
        return conf;
    }

}
