package com.brightcode.mediadeck;

import org.springframework.boot.SpringApplication;

public class TestMediadeckApplication {

	public static void main(String[] args) {
		SpringApplication.from(MediadeckApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
