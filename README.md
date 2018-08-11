
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

> execution using jar with passing environment profile from yml
```
java -jar -Dspring.profiles.active=stage target/com.springboot.camel.basic-1.0.1.jar
```

## Unit Testing and Mock Testing

### POM file for Unit and Mock Testing:
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<version>${springboot.version}</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.apache.camel</groupId>
			<artifactId>camel-test-spring</artifactId>
			<version>${camelspring.version}</version>
			<scope>test</scope>
		</dependency>
		<!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
		<dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>1.3.2</version>
		</dependency>
```
And yes:
```
	<build>
		<plugins>
			<plugin>
				<artifactId>maven-surefire-plugin</artifactId>
				<configuration>
					<argLine>--add-modules java.xml.bind --add-opens
						java.base/java.lang=ALL-UNNAMED</argLine>
				</configuration>
			</plugin>	
		<plugins>
	<build>
```
### Unit Test:

```
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class BasicRouteTest {

	@Autowired
	ProducerTemplate producerTemplate;
	
	@Autowired
	Environment environment;
	
	@BeforeClass
	public static void startCleanup() throws IOException{
		FileUtils.cleanDirectory(new File("Test/Dev/In"));
		FileUtils.deleteDirectory(new File("Test/Dev/Out"));
	}
	
	@Test
	public void testMoveFile() throws InterruptedException {
		
		String fileName = "testEmployeeSalaryProcessing.txt";
		String routeFrom = "routeIn";
		String message = "employeeId,rates\n"+
							"EM001, 140.00\n"+
							"EM016, 180.00\n"+
							"EM018, 150.00\n"+
							"EM031, 130.00\n"+
							"EM034, 120.00\n";
		producerTemplate.sendBodyAndHeader(environment.getProperty(routeFrom), message,Exchange.FILE_NAME,fileName);
		Thread.sleep(3000);
		String fileOut = "C:/playground/basic-spring-boot-camel/Test/Dev/Out/"+fileName;
		File outFile = new File(fileOut);
		assertTrue(outFile.exists());
	}
}
```
### Mock Testing:

```
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mock")
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@MockEndpoints
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BasicRouteMockTest{

	@Autowired
	ProducerTemplate producerTemplate;
	
	@Autowired
	Environment environment;
	
	@EndpointInject(uri = "{{routeOut}}")
	  private MockEndpoint mockEndpoint;
	
	@Test
	public void testMoveFileMock() throws InterruptedException {
		String message = "employeeId,rates\n"+
							"EM001, 140.00\n"+
							"EM016, 180.00\n"+
							"EM018, 150.00\n"+
							"EM031, 130.00\n"+
							"EM034, 120.00\n";

		mockEndpoint.expectedBodiesReceived(message);
		
		
		producerTemplate.sendBody("{{routeIn}}",message);
		
		mockEndpoint.assertIsSatisfied();
		
	}

}

```
## Adding Bit more to this

### Goal
- [ ] YAML based configuration
- [x] Unit Testing
- [x] Mock Testing
- [ ] Debatching - EIP - Splitter
- [ ] SQL operations CRUD
- [ ] SEDA Read/Write
- [ ] Apache Active MQ Read/Write
- [ ] Apache kafka Read/Write
- [ ] Logging in Camel Spring boot

