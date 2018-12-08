# Demos
## 1：Hello Sparrow

Create a class named `HelloSparrow.java`:
```java
import java.io.IOException;

public class HelloSparrow {
    public static void main(String[] args) {
        Router.get("/hi", (req, resp) -> {
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
That's all we need to do if we use sparrow! We no longer need to download `Tomcat server`, check its version with your local jdk, configure it, write `web.xml` or use `@WebX` annotations, and so on.
That's so damn work for novice. Here we just open browser and access `localhost:8080/hi` we will see our works, that is, a simple greeting message:)
And more interesting, sparrow pre-defines a `/hello` page, it shows a friendly message for those who are new to use it:

![](hello.png)

Moreover, sparrow internally supports `jsp`+`servlet` mixed mode.
The default `jsp` base directory is `src/main/resources`, which is relative to your project root path.
We need to create that directory if we want to use jsp. Then access `localhost:8080/index.jsp`, your newly created `jsp` page would be rendered immediately.

You can also specify a different jsp base before starting sparrow, it merely requires you to call a method and pass your favorite path argument:
```java
Configurator.setJspBase("my_favorite_path/");
```
For more details about various configurations and their explanations, see sparrow's corresponding documentation.

## 2：User login
Writing hello world tend to be somewhat boring, let's take this "login page" for example:

As I said before, create a directory `src/main/resources` and put `login.jsp` into that directory firstly：
```java
    <form action="/loginCheck" method="post">
        Username: <label><input name="username" type="text"/></label><br/>
        Email: <label><input name="email" type="email"/></label><br/>
        Password: <label><input name="password" type="password" /></label><br/>
        <label><input name="submit" type="submit"/> </label>
    </form>
```
Sparrow would "forward" user's request to `/loginCheck` as long as the submit button was clicked.
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
The method `model.get()` retrives form data user submitted, match them with given username and password,
then redirect to a new age according to matching result. In a real-world practise, it's more likely to query
username and password from remote database. So let's move on to next section, we will demonstrate how to use `sparrow`
database template to facilitate our redundant CRUD works.

## 3: Facilitate CRUD by using sparrow database template
First, you need to specify your database connection information in `src/main/resources/sparrow.properties`.
Configuration items are self-explanatory:
```properties
database.username=root
database.password=root
database.url=jdbc:mysql://localhost:3306/videohub?serverTimezone=GMT
database.driver-class=com.mysql.jdbc.Driver
```
When you configured above properties, you can use Sparrow database template to query
by both concatenated SQL or parameterized SQL, they're pretty easy to use,
and thanks to Java 8 lambda we have even more concise code:
```java
DBTemplate.query("select * from videohub_user where id=1", result->{
    System.out.println(result.getString("username"));
    System.out.println(result.getString("email"));
    System.out.println(result.getString("password"));
});

// parameterized sql is also supported
DBTemplate.query("select * from videohub_user where id=? and username=? and password=?", new Object[]{1, "yang", "123"},
        result -> {
            System.out.println(result.getString("username"));
            System.out.println(result.getString("email"));
            System.out.println(result.getString("password"));
        });
```
In fact, `OrmTemplate` could do better. If you've used some ORM frameworks before, you will be familiar with the following style.
To illustrate that we should define a domain class like:
```java
public class User {
    private long id;
    private String username;
    private String password;
    private String avatar_url;
}
```
Then passing the `Class` object of that type:
```java
User user = OrmTemplate.queryOne("select * from videohub_user where id=1", User.class);
System.out.println(user);
List<User> userList = OrmTemplate.queryList("select * from videohub_user", User.class);
System.out.println(userList);
```
Sparrow will map database columns in result set to corresponding fields of given type.

As client programmer(I means, those who are using libraries to code awesome products), we don't need to
manipulate connections, create statement, execute staetment and release resources and so on,
all we need is to code SQL and retrieve useful column from result set.