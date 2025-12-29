package com.lcwd.rating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RaringServuiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaringServuiceApplication.class, args);
	}

}
