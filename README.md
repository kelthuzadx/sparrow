# ![](docs/sparrow.png) Sparrow: A Pretty Lightweight Java Web framework

![](https://img.shields.io/badge/project--status-under%20developing-yellow.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.racaljk/sparrow.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.racaljk%22%20AND%20a:%22sparrow%22)


**Sparrow** is a lightweight Java Web framework. 
Lightweight as it is, it contains tools and utilities you need in development. Besides, Sparrow also integrates some fully-tested third-party frameworks to let it work in more backgrounds.

# Usage
To use Sparrow, add its dependencies and import it.
```xml
<dependency>
    <groupId>com.github.racaljk</groupId>
    <artifactId>sparrow</artifactId>
    <version>1.1.0-snapshot</version>
</dependency>
```
Or you can download sparraw.jar and append relevent dependencies into your IDE's buildpath (Not recommend since there are many dependencies and we have to download them manually and check their version one by one... that's definitely inefficient and error-prone).

![](docs/split.png)

# Integration
By default, sparrow integrates some popular third-party frameworks.
You can disable them or change predefined configurations of them.

| Logo | Name | Status |
| :---: | ----- | ------ |
|![](docs/thymeleaf_logo.png) | Thymeleaf template engine | Integrated |
|![](docs/mysql_logo.png) | Mysql connector | Integrated |


# Documentation
+ [0. Demos(recommend)](docs/demos.md)
+ [1. Router](docs/router.md)
+ [2. Datebase template and orm template](docs/database_template.md)
+ [3. Configuration](docs/configuration.md)
+ [4. Utilities]()
    + [generate captcha](docs/generate_captcha.md)
    + [send email](docs/send_email.md)

# Contribute
Please feel free to pull your patchs and enhancements, I will accept them unless they have some fatal problems.
Any questions/proposals which related to `sparrow` are welcomed, I will reply them in free time.

# License
`Sparrow` was licensed under the [MIT](LICENSE) license.
