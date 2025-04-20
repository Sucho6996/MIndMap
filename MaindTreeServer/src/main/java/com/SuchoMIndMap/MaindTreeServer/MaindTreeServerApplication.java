package com.SuchoMIndMap.MaindTreeServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MaindTreeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaindTreeServerApplication.class, args);
	}

}
