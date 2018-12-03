# ![](docs/logo.png) sparrow: A pretty lightweight java web framework

![](https://img.shields.io/badge/project--status-under%20developing-yellow.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.racaljk/sparrow.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.racaljk%22%20AND%20a:%22sparrow%22)

[中文](README.md) |
[English](README_EN.md)

**sparrow** is a lightweight java web framework. It's tiny yet but actually tend to be contained almost all infrastructural tools and utilities for web developing. Besides, sparrow integrates some full tested third-party libraries/frameworks such as thymeleaf template engine and redis in-memory database
so that we don't need to invent everything we use.

To use sparrow, we need add relative dependencies in `pom.xml`and import it:
```xml
<dependency>
    <groupId>com.github.racaljk</groupId>
    <artifactId>sparrow</artifactId>
    <version>1.0.1-snapshot</version>
</dependency>
```
Or you can download `sparraw.jar` and append relative dependencies into your IDE's buildpath (Not recommend).


# Integration
By default, sparrow integrates some widely used third-party frameworks.
You can disable them or change predefined configurations of them.

| Name | Status |
| :---: | ------ |
|![](docs/thymeleaf_logo.png) | Integrated |
|![](docs/mysql_logo.png) | Integrated |


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
That's all we need to do if we use sparrow! We are no longer need to download tomcat server, check its version with your local jdk, configure it, write web.xml or use @WebX annotations, and so on...
That's so damn work for novice. Here we just open browser and access `localhost:8080/hello` we will see our works, that is, a simple greeting message:)

Moreover, sparrow internally supports `jsp`+`servlet` mixed mode.
The default `jsp` base directory is `src/main/webapp`, which is relative to your project path.
We need to create that directory if we want to use jsp:

![](docs/jsp_docbase.png)

Now access `localhost:8080/index.jsp`, your newly created `jsp` page would be rendered soon.

You can also specify a different jsp base before starting sparrow, it barely requires you to call a method and pass your favorite path argument:
```java
Configurator.setJspBase("my_favorite_path/");
```
For more details about various configurations and their explanations, see sparrow's corresponding documentation.

## 2：User login
Writing hello world tend to be somewhat boring, so here we will illustrate a useful login page:

As I said before, createing a directory `src/main/webapp` and putting `login.jsp` into that dir firstly：
```java
    <form action="/loginCheck" method="post">
        Username: <label><input name="username" type="text"/></label><br/>
        Email: <label><input name="email" type="email"/></label><br/>
        Password: <label><input name="password" type="password" /></label><br/>
        <label><input name="submit" type="submit"/> </label>
    </form>
```
Sparrow would forward user's request to `/loginCheck` as long as submit button was be clicked.
Now we need to write corresponding login checking logic:

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
The method `model.get()` retrives user submitted form data and match them with given username and password,
then redirect to new age according to matching result. In a real world product, it's more likely to query
username and password from remote database. Here we use hard code approach since we don't want to introduce
complexity. This demo is subject to change since sparrow database template is under developing.

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
You can change sparrow's intrinsic behaviors by calling `Configurator` methods, these methods are as follows：

| Item | Default | Option |
| :-----: | ----- | ----- |
| jsp base dir | `src/main/webapp/` | Configurator.setJspBase(docPath) |
| integrate thymeleaf | Yes | Configurator.disableThymeleaf() |
| sparrow properties namr | `sparrow.properties` | Configurator.setSparrowProperties(propertiesFileName) |


# License
`Sparrow` was licensed under the [MIT](LICENSE) license.