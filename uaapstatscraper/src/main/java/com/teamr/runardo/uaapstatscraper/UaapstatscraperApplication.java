package com.teamr.runardo.uaapstatscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UaapstatscraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(UaapstatscraperApplication.class, args);
	}

}
