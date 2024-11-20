package com.teamr.runardo.uaapstatsdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UaapstatsdataApplication {

	public static void main(String[] args) {
		SpringApplication.run(UaapstatsdataApplication.class, args);
	}

}
