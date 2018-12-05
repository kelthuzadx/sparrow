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
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix(PropertiesHolder.readProp("thymeleaf.prefix"));
        templateResolver.setSuffix(PropertiesHolder.readProp("thymeleaf.suffix"));
        templateResolver.setCacheTTLMs(Long.valueOf(PropertiesHolder.readProp("thymeleaf.cacheTTLMs")));
        templateResolver.setCacheable(Boolean.valueOf(PropertiesHolder.readProp("thymeleaf.enableCache")));
        templateEngine.setTemplateResolver(templateResolver);
        logger.debug("integrated Thymeleaf template engine into sparrow");
    }

    public static org.thymeleaf.TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
