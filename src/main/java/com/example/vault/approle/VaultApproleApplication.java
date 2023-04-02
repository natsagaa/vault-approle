package com.example.vault.approle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class VaultApproleApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaultApproleApplication.class, args);
	}

	@Value("${username}")
	private String username;

	@Value("${password}")
	private String password;

	@PostConstruct
	public void print(){
		System.out.println("Username is: " + username);
		System.out.println("Password is: " + password);
	}

}
