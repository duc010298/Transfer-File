package com.github.duc010298.transferfile.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.duc010298.transferfile.configuration.FileStorageConfig;

@Service
public class FileStorageService {
	
	private final Path fileStorageLocation;
	
	@Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
		String storageConfig = fileStorageConfig.getUploadDir();
		if(storageConfig == null) storageConfig = System.getProperty("user.dir");
        this.fileStorageLocation = Paths.get(storageConfig).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new Exception("Wrong file");
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found " + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
