package com.springboot.camel.basic.route;

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
