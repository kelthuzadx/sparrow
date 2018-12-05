package mvc;

import core.Sparrow;
import org.apache.log4j.Logger;
import org.thymeleaf.context.WebContext;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static thirdparty.ThymeleafEngine.getTemplateEngine;

@SuppressWarnings("unused")
public class Router {
    // Don't change these variable names since we use their names to access theirs values via reflection
    private static final Router $r = new Router();
    private static final Logger logger = Logger.getLogger(Sparrow.class);

    private Map<String, Servlet> $servletMap = new HashMap<>();

    private Router() {
    }

    public static Router get(String urlPattern, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        $r.addRouter(urlPattern, new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
                resp.setCharacterEncoding("UTF-8");
                handler.accept(req, resp);
            }
        });
        return $r;
    }

    public static Router post(String urlPattern, BiConsumer<HttpServletRequest, HttpServletResponse> handler) {
        $r.addRouter(urlPattern, new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
                resp.setCharacterEncoding("UTF-8");
                handler.accept(req, resp);
            }
        });
        return $r;
    }

    private static void modelRouterImpl(Function<Model, View> handler, HttpServletRequest req, HttpServletResponse resp) {
        Model dataModel = new Model(req.getParameterMap());
        View view = handler.apply(dataModel);

        if (view.getModel() != null) {
            view.getModel().getWholeModel().forEach(req::setAttribute);
        }

        if (view.getViewPath().endsWith(".html")) {
            WebContext ctx =
                    new WebContext(req, resp, req.getServletContext(), req.getLocale());
            try {
                getTemplateEngine().process(view.getViewPath(), ctx, resp.getWriter());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (req.getMethod().equals("GET")) {
                    req.getRequestDispatcher(view.getViewPath()).forward(req, resp);
                } else {
                    resp.sendRedirect(view.getViewPath());
                }
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Router get(String urlPattern, Function<Model, View> handler) {
        $r.addRouter(urlPattern, new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
                resp.setCharacterEncoding("UTF-8");
                modelRouterImpl(handler, req, resp);
            }
        });
        return $r;
    }

    public static Router post(String urlPattern, Function<Model, View> handler) {
        $r.addRouter(urlPattern, new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
                resp.setCharacterEncoding("UTF-8");
                modelRouterImpl(handler, req, resp);
            }
        });
        return $r;
    }

    private void addRouter(String urlPattern, Servlet servlet) {
        $servletMap.put(urlPattern, servlet);
    }
}
