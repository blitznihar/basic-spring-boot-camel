package com.springboot.camel.basic.route;

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

	@Test
	public void testMockMoveFile() throws InterruptedException {
		
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
