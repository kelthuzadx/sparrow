package mvc;

public class View {
    private Model model;
    private String viewPath;

    private View(String viewPath) {
        this.viewPath = viewPath;
    }

    private View(String viewPath, Model model) {
        this.viewPath = viewPath;
        this.model = model;
    }

    public static View create(String viewPath) {
        return new View(viewPath);
    }

    public static View create(String viewPath, Model model) {
        return new View(viewPath, model);
    }

    public static View ok() {
        return new View("/ok");
    }

    public static View error() {
        return new View("/error");
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }
}
