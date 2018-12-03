package core;


import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import thirdparty.ThymeleafInitializationListener;

import javax.servlet.ServletException;
import java.io.File;

public final class Sparrow {
    private static final Logger logger = Logger.getLogger(Sparrow.class);
    private static final Sparrow sparrow = new Sparrow();
    private static final String DEFAULT_CONTEXT_PATH = "/";

    static {
        BasicConfigurator.configure();
    }

    private Tomcat tomcat;
    private Context internalContext;

    private Sparrow() {
        tomcat = new Tomcat();
        tomcat.setBaseDir("sparrow");
        tomcat.setPort(8080);
    }


    public static void fly() {
        sparrow.readProperties();
        sparrow.createContext();
        sparrow.integrateThirdparty();
        RouterManager.registerPredefinedRouter();
        RouterManager.registerUserDefinedRouter(sparrow.tomcat, sparrow.internalContext);
        sparrow.serve();
    }

    private void readProperties() {
    }

    private void createContext() {
        try {
            internalContext = tomcat.addWebapp(DEFAULT_CONTEXT_PATH,
                    new File(Configurator.jspDocBase).getAbsolutePath());
        } catch (ServletException e) {
            logger.warn("there is not jsp base dir");
        }
    }

    private void integrateThirdparty() {
        if (Configurator.enableThymeleaf) {
            internalContext.addApplicationListener(ThymeleafInitializationListener.class.getName());
        }
    }


    private void serve() {
        try {
            tomcat.start();
            logger.debug("Sparrow started");
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}

