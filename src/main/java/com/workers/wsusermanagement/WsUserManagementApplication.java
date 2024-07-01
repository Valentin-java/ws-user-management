package com.workers.wsusermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WsUserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsUserManagementApplication.class, args);
	}

}
