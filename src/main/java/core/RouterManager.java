package core;

import mvc.Router;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.Logger;

import javax.servlet.Servlet;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

class RouterManager {
    private static final Logger logger = Logger.getLogger(Sparrow.class);

    static void registerPredefinedRouter() {
        Router.get("/ok", (req, resp) -> {
            try {
                resp.getWriter().println("OK");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Router.get("/error", (req, resp) -> {
            try {
                resp.getWriter().println("ERROR");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    static void registerUserDefinedRouter(Tomcat tomcat, Context internalContext) {
        try {
            Field rf = Router.class.getDeclaredField("$r");
            rf.setAccessible(true);
            Router r = (Router) rf.get(null);
            Field f = r.getClass().getDeclaredField("$servletMap");
            f.setAccessible(true);
            Map<String, Servlet> mappedServlet = (Map<String, Servlet>) f.get(r);
            mappedServlet.forEach((url, servlet) -> {
                // NOT THAT we must use contextPath("") rather than using DEFAULT_CONTEXT_PATH since
                // default context path "/" in stored contexts of current host is accessed via key ""
                tomcat.addServlet("", String.valueOf(servlet), servlet);
                internalContext.addServletMappingDecoded(url, String.valueOf(servlet));
                logger.debug("register mapping " + url);
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
