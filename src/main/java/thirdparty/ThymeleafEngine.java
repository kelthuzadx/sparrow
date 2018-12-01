package thirdparty;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class ThymeleafEngine {
    private static TemplateEngine templateEngine = new TemplateEngine();

    public static void initializeThymeleaf(ServletContextTemplateResolver templateResolver) {
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCacheable(true);
        templateEngine.setTemplateResolver(templateResolver);
    }

    public static org.thymeleaf.TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
