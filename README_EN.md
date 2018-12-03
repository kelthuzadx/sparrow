# ![](docs/logo.png) sparrow: A pretty lightweight java web framework

![](https://img.shields.io/badge/project--status-under%20developing-yellow.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.racaljk/sparrow.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.racaljk%22%20AND%20a:%22sparrow%22)
**sparrow** is a lightweight java web framework. It's tiny yet but actually tend to be contained almost all infrastructural tools and utilities for web developing. Besides, sparrow integrates some fulltested third party such as thymeleaf engine and redis cache
so that we don't need to invent everything we use.

To use sparrow, we need add its dependency in `pom.xml`and import it:
```xml
<dependency>
    <groupId>com.github.racaljk</groupId>
    <artifactId>sparrow</artifactId>
    <version>1.0.1-snapshot</version>
</dependency>
```
Or yoi cand download sparraw.jar and append it into your IDE's buildpath.


# Integration
By default, sparrow integrates some widely used third party framework. You can disable them or change predefined behaviors

| Name | Status |
| :---: | ------ |
|![](docs/thymeleaf_logo.png) | Integrated |


# Demos
## 1：hello sparrow

Create a file named `HelloSparrow.java`:
```java
import java.io.IOException;

public class HelloSparrow {
    public static void main(String[] args) {
        Router.get("/hello", (req, resp) -> {
            try {
                resp.getWriter().println("hello world");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Sparrow.fly();

    }
}
```
Thats all we need do. Now open your broeser and access `localhost:8080/hello` you will see greeting message.

Moreover, sparrow internally supports jsp+servlet mixed mode. The default jsp dir base dir is `src/main/webapp`，
So we need create that directory if we want use jsp:

![](docs/jsp_docbase.png)

Now access `localhost:8080/index.jsp`, it should be rendered.

You can specify jsp base doc before starting sparrow：
```java
Configurator.setJspBase("my_favorite_path/");
```
For more details about various configurations, see its documentation.

## 2：User login
Writing hell world tend to be somewhat boring, so here we will illustrate a useful login page:

As previous said, create a directory `src/main/webapp` and `login.jsp` into that dir：
```java
    <form action="/loginCheck" method="post">
        Username: <label><input name="username" type="text"/></label><br/>
        Email: <label><input name="email" type="email"/></label><br/>
        Password: <label><input name="password" type="password" /></label><br/>
        <label><input name="submit" type="submit"/> </label>
    </form>
```
Sparrow would forward to /loginCheck user request as long as submit button was be clicked. Now we need to write corresponding login logic:

```java
public class LoginPage {
    private static final String USER_NAME = "yang";
    private static final String PASS_WORD = "400820";

    public static void main(String[] args) {
        Router.post("/loginCheck", model->{
            if(model.get("username").equals(USER_NAME) && model.get("password").equals(PASS_WORD)){
                return View.ok();
            }
            return View.error();
        });
        Sparrow.fly();
    }
}
```
The method `model.get()` retrives user submitted form data and match them with given username and password, then redirect to new age according to matching result.



# Documentation
## 1.Router
There are many ways to define user routers, you can choose your favorite one：
```java
public class DefineRouter {
    public static void main(String[] args) {
        // Route to html
        Router.get("/a",model -> View.create("home.html"));

        // Route to jsp
        Router.get("/b",model-> View.create("index.jsp"));

        // Returm a model bundled view, it will be resolved by sparrow later
        Router.get("/d",model -> {
            model.set("greeting","hi");
            return View.create("home.html",model);
        });

        // Use native servlet and ignore view resolving phase
        Router.get("/c",(req,resp)-> {
            try {
                resp.getWriter().println("<p>rendering page without view resolving</p>");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Sparrow.fly();
    }
}
```

## 2.Configuration
You can change sparrow's internal behaviors by calling Configurator methods, these methods are as follows：

| Item | Default | Option |
| :-----: | ----- | ----- |
| jsp base dir | `src/main/webapp/` | Configurator.setJspBase(docPath) |
| integrate thymeleaf | Yes | Configurator.disableThymeleaf() |
| sparrow properties namr | `sparrow.properties` | Configurator.setSparrowProperties(propertiesFileName) |
