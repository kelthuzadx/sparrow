package thirdparty;

import core.PropertiesHolder;
import core.Sparrow;
import org.apache.log4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class ThymeleafEngine {
    private static TemplateEngine templateEngine = new TemplateEngine();
    private static final Logger logger = Logger.getLogger(Sparrow.class);

    static void initializeThymeleaf(ServletContextTemplateResolver templateResolver) {
        String thymeleafPrefix = PropertiesHolder.readProp("thymeleaf.prefix");
        String thymeleafSuffix = PropertiesHolder.readProp("thymeleaf.suffix");
        String thymeleafCacheTTLMs = PropertiesHolder.readProp("thymeleaf.cacheTTLMs");
        String thymeleafEnableCache = PropertiesHolder.readProp("thymeleaf.enableCache");

        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix(thymeleafPrefix != null ? thymeleafPrefix : "/");
        templateResolver.setSuffix(thymeleafSuffix != null ? thymeleafSuffix : ".html");
        templateResolver.setCacheable(thymeleafEnableCache != null ? Boolean.valueOf(thymeleafEnableCache) : true);
        if (templateResolver.isCacheable()) {
            templateResolver.setCacheTTLMs(thymeleafCacheTTLMs != null ? Long.valueOf(thymeleafCacheTTLMs) : 360000L);
        }
        templateEngine.setTemplateResolver(templateResolver);
        logger.debug("integrated Thymeleaf template engine into sparrow");
    }

    public static org.thymeleaf.TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
