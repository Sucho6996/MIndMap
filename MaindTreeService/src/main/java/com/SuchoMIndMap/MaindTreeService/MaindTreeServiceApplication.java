package com.SuchoMIndMap.MaindTreeService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MaindTreeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaindTreeServiceApplication.class, args);
	}

}
