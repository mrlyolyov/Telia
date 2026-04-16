package com.optima.cms.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class StartupRoute extends RouteBuilder {

	@Override
	public void configure() {
		from("timer:startup?period=60000")
				.routeId("startup")
				.log("Camel route startup has begun");
	}
}
