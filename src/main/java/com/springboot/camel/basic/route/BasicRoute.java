package com.springboot.camel.basic.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class BasicRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("{{routeIn}}").to("{{routeOut}}").log("${body}");
		
	}

}
