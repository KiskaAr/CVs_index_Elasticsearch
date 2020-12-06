package com.cv_index;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.cv_index.dao")
@EnableJpaRepositories(basePackages = "com.cv_index.dao")
public class CVsIndexElasticsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CVsIndexElasticsearchApplication.class, args);
	}

}
