## Router
There are many ways to define user routers, you can choose your favorite one：
```java
// Route to html
Router.get("/a",model -> View.create("home.html"));
```
```java
// Route to jsp
Router.get("/b",model-> View.create("index.jsp"));
```
```java
// Returm a model bundled view, it will be resolved by sparrow later
Router.get("/d",model -> {
    model.set("greeting","hi");
    return View.create("home.html",model);
});
```
```java
// Use native servlet and ignore view resolving phase
Router.get("/c",(req,resp)-> {
    try {
        resp.getWriter().println("<p>rendering page without view resolving</p>");
    } catch (IOException e) {
        e.printStackTrace();
    }
});
```
And sparrow also allows you to specify path variables in url patterns, you can get the value from `Model` object by calling `getPathVar()` method:
```java
Router.get("/path/{var}/{name}",model -> {
    System.out.println("var:"+model.getPathVar("var"));
    System.out.println("name:"+model.getPathVar("name"));
    return View.ok();
});
```