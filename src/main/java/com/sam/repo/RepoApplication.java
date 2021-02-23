package com.sam.repo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RepoApplication {



	public static void main(String[] args) {

		System.setProperty("javax.net.ssl.trustStore", "C:\\mongoBackUp\\certs\\mongotrust.p12");
		System.setProperty("javax.net.ssl.trustStorePassword", "1234qwer");
		System.setProperty("javax.net.ssl.keyStore", "C:\\mongoBackUp\\certs\\mongoKey.p12");
		System.setProperty("javax.net.ssl.keyStorePassword", "1234qwer");
		System.setProperty("jdk.tls.client.protocols", "TLSv1.2");

		SpringApplication.run(RepoApplication.class, args);
	}

}
