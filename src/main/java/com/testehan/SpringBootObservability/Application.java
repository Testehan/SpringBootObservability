package com.testehan.SpringBootObservability;

import com.testehan.SpringBootObservability.post.JsonPlaceholderService;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

	@Bean
	JsonPlaceholderService jsonPlaceholderService() {
		RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
		return factory.createClient(JsonPlaceholderService.class);
	}

//	@Bean		// this is the non AOP approach
//	CommandLineRunner commandLineRunner(JsonPlaceholderService jsonPlaceholderService, ObservationRegistry observationRegistry) {
//		return args -> {
//			Observation.createNotStarted("posts.load-all-posts",observationRegistry)
//					.lowCardinalityKeyValue("author","Dan")		// will be displayed in zipkin
//					.contextualName("post-service.find-all")
//					.observe(()-> {
//						jsonPlaceholderService.findAll();
//						log.info("Posts : {}",jsonPlaceholderService.findAll());
//					});
//		};
//	}

	@Bean		// AOP approach that needs the aop dependency with the annotation on the method...for some reason the information displayed in zipkin is a bit different than the one from above method
	@Observed(name = "posts.load-all-posts", contextualName = "post.find-all")
	CommandLineRunner commandLineRunner2(JsonPlaceholderService jsonPlaceholderService) {
		return args -> {
			jsonPlaceholderService.findAll();
		};
	}

}
