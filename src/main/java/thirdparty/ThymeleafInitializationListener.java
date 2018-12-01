package thirdparty;

import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ThymeleafInitializationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ThymeleafEngine.initializeThymeleaf(
                new ServletContextTemplateResolver(servletContextEvent.getServletContext()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
