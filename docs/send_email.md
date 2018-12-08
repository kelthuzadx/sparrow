# Send email
It's simple and easy. First configure your sender email and authentication code in `sparrow.properties`:
```properties
email.sender=admin@gmail.com
email.authcode=11mc8235nv
```
Now instantiate an object and call `sendEmailAsync`, that's all we need:
```java
import tool.MailWorker;

new MailWorker().sendMailAsync("hello world", "this mail was sent by java program", "1948638989@qq.com");
System.out.println("done");
}
```
As name described, it's an asynchronous method so it would not block current execution flow.