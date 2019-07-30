package com.github.duc010298.transferfile;

import com.github.duc010298.transferfile.configuration.FileStorageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageConfig.class
})
public class TransferFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransferFileApplication.class, args);
    }

}
