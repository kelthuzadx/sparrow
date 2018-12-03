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
        Router.get("/hello", (req, resp) -> {
            try {
                resp.getWriter().println("<!DOCTYPE HTML><html><head><title>Hello Sparrow</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><link href='http://fonts.googleapis.com/css?family=Capriola' rel='stylesheet' type='text/css'><style type=\"text/css\">body{\tfont-family: 'Capriola', sans-serif;}body{\tbackground:#DAD6CC;}\t.wrap{\tmargin:0 auto;\twidth:1000px;}.logo h1{\tfont-size:150px;\tcolor:#FF7A00;\ttext-align:center;\tmargin-bottom:1px;\ttext-shadow:4px 4px 1px white;}\t.logo p{\tcolor:#B1A18D;;\tfont-size:20px;\tmargin-top:1px;\ttext-align:center;}\t.logo p span{\tcolor:lightgreen;}\t.sub a{\tcolor:#ff7a00;\ttext-decoration:none;\tpadding:5px;\tfont-size:13px;\tfont-family: arial, serif;\tfont-weight:bold;}\t.footer{\tcolor:white;\tposition:absolute;\tright:10px;\tbottom:10px;}\t.footer a{\tcolor:#ff7a00;}\t</style></head><body>\t<div class=\"wrap\">\t\t<div class=\"logo\">\t\t\t<h1>Hello sparrow</h1>\t\t\t<p> You know I was predefined by my creater:)</p>\t\t</div>\t</div></body></html>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Router.get("/ok", (req, resp) -> {
            try {
                resp.getWriter().println("<!DOCTYPE HTML><html><head><title>Hello Sparrow</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><link href='http://fonts.googleapis.com/css?family=Capriola' rel='stylesheet' type='text/css'><style type=\"text/css\">body{\tfont-family: 'Capriola', sans-serif;}body{\tbackground:#DAD6CC;}\t.wrap{\tmargin:0 auto;\twidth:1000px;}.logo h1{\tfont-size:150px;\tcolor:#FF7A00;\ttext-align:center;\tmargin-bottom:1px;\ttext-shadow:4px 4px 1px white;}\t.logo p{\tcolor:#B1A18D;;\tfont-size:20px;\tmargin-top:1px;\ttext-align:center;}\t.logo p span{\tcolor:lightgreen;}\t.sub a{\tcolor:#ff7a00;\ttext-decoration:none;\tpadding:5px;\tfont-size:13px;\tfont-family: arial, serif;\tfont-weight:bold;}\t.footer{\tcolor:white;\tposition:absolute;\tright:10px;\tbottom:10px;}\t.footer a{\tcolor:#ff7a00;}\t</style></head><body>\t<div class=\"wrap\">\t\t<div class=\"logo\">\t\t\t<h1>Status OK</h1>\t\t\t<p> Everything is gonna to be fine</p>\t\t</div>\t</div></body></html>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Router.get("/error", (req, resp) -> {
            try {
                resp.getWriter().println("<!DOCTYPE HTML><html><head><title>Hello Sparrow</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><link href='http://fonts.googleapis.com/css?family=Capriola' rel='stylesheet' type='text/css'><style type=\"text/css\">body{\tfont-family: 'Capriola', sans-serif;}body{\tbackground:#DAD6CC;}\t.wrap{\tmargin:0 auto;\twidth:1000px;}.logo h1{\tfont-size:150px;\tcolor:#FF7A00;\ttext-align:center;\tmargin-bottom:1px;\ttext-shadow:4px 4px 1px white;}\t.logo p{\tcolor:#B1A18D;;\tfont-size:20px;\tmargin-top:1px;\ttext-align:center;}\t.logo p span{\tcolor:lightgreen;}\t.sub a{\tcolor:#ff7a00;\ttext-decoration:none;\tpadding:5px;\tfont-size:13px;\tfont-family: arial, serif;\tfont-weight:bold;}\t.footer{\tcolor:white;\tposition:absolute;\tright:10px;\tbottom:10px;}\t.footer a{\tcolor:#ff7a00;}\t</style></head><body>\t<div class=\"wrap\">\t\t<div class=\"logo\">\t\t\t<h1>Status Error</h1>\t\t\t<p> There are some problems....</p>\t\t</div>\t</div></body></html>");
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
