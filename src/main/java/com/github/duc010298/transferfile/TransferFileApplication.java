package com.github.duc010298.transferfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.github.duc010298.transferfile.configuration.FileStorageConfig;

@SpringBootApplication
@EnableConfigurationProperties({
	FileStorageConfig.class
})
public class TransferFileApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransferFileApplication.class, args);
	}

}
