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
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static thirdparty.ThymeleafEngine.getTemplateEngine;

@SuppressWarnings("unused")
public class Router {
    // Don't change these variable names since we use their names to access theirs values via reflection
    private static final Router $r = new Router();
    private static final Logger logger = Logger.getLogger(Sparrow.class);

    private ArrayList<Pair<String, Servlet>> $servletMap = new ArrayList<>();

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

    private static void modelRouterImpl(ArrayList<String> pathVars, Function<Model, View> handler,
                                        HttpServletRequest req, HttpServletResponse resp) {
        // Prepare model data
        Model dataModel = new Model(req.getParameterMap());
        String uri = req.getRequestURI();
        String[] spans = uri.split("/");
        for (int i = spans.length - 1, k = pathVars.size() - 1; i >= 0 && k >= 0; i--, k--) {
            dataModel.setPathVar(pathVars.get(k), spans[i]);
        }

        // Execute handler
        View view = handler.apply(dataModel);

        // View resolving
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
        Pattern pattern = Pattern.compile(".*?\\{([a-zA-Z]+?)\\}");
        Matcher m = pattern.matcher(urlPattern);
        boolean rewrite = false;
        ArrayList<String> pathVars = new ArrayList<>();
        while (m.find()) {
            if (!rewrite) {
                urlPattern = urlPattern.replaceAll("\\{[a-zA-Z]+?\\}/?", "*");
                urlPattern = urlPattern.replaceAll("\\*+", "*");
                System.out.println(urlPattern);
                rewrite = true;
            }
            pathVars.add(m.group(1));
        }

        $r.addRouter(urlPattern, new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
                resp.setCharacterEncoding("UTF-8");
                modelRouterImpl(pathVars, handler, req, resp);
            }
        });
        return $r;
    }

    public static Router post(String urlPattern, Function<Model, View> handler) {
        Pattern pattern = Pattern.compile(".*?\\{([a-zA-Z]+?)\\}");
        Matcher m = pattern.matcher(urlPattern);
        boolean rewrite = false;
        ArrayList<String> pathVars = new ArrayList<>();
        while (m.find()) {
            if (!rewrite) {
                urlPattern = urlPattern.replaceAll("\\{[a-zA-Z]+?\\}/?", "*");
                urlPattern = urlPattern.replaceAll("\\*+", "*");
                System.out.println(urlPattern);
                rewrite = true;
            }
            pathVars.add(m.group(1));
        }

        $r.addRouter(urlPattern, new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
                resp.setCharacterEncoding("UTF-8");
                modelRouterImpl(pathVars, handler, req, resp);
            }
        });
        return $r;
    }

    private void addRouter(String urlPattern, Servlet servlet) {
        $servletMap.add(new Pair<>(urlPattern, servlet));
    }
}
