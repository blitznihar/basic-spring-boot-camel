<script src="https://gist.github.com/andyferra/2554919.js"></script>
# basic-spring-boot-camel
Basics of spring boot application for Apache Camel
## Basic POM file with description:
```
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>basic-spring-boot-camel</groupId>
	<artifactId>com.springboot.camel.basic</artifactId>
	<version>1.0.1</version>
	<packaging>jar</packaging>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<springboot.version>2.0.4.RELEASE</springboot.version>
		<camelspring.version>2.22.0</camelspring.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.apache.camel</groupId>
			<!-- camel-spring-boot jar comes with the spring.factories file, so as 
				soon as you add that dependency into your classpath, Spring Boot will automatically 
				auto-configure Camel for you -->
			<artifactId>camel-spring-boot-starter</artifactId>
			<version>${camelspring.version}</version>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<!-- provides Spring Boot support in Maven, letting you package executable 
					jar or war archives and run an application “in-place” Enables mvn package 
					java -jar target/mymodule-0.0.1-SNAPSHOT.jar Enables mvn spring-boot:run -->
				<artifactId>spring-boot-maven-plugin</artifactId>
				<version>${springboot.version}</version>
				<executions>
					<execution>
						<goals>
							<goal>repackage</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</project>
```
## application.properties file or application.yml file
```
#To keep the main thread blocked so that Camel stays up,
camel.springboot.main-run-controller=true
```

## Application Start would look like
```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

}
```

## Defining a simple route would be something like:
```
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class simplefileroute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("{{routeIn}}").to("{{routeOut}}");		
	}

}
```

#### That is all
### Execute using:
> spring-boot
```
mvn spring-boot:run
```
> execution using jar
```
java -jar target/com.springboot.camel.basic-1.0.1.jar
```
