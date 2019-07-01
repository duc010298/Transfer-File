package com.github.duc010298.transferfile.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.duc010298.transferfile.configuration.FileStorageConfig;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;

	@Autowired
	public FileStorageService(FileStorageConfig fileStorageConfig) {
		String storageConfig = fileStorageConfig.getUploadDir();
		if (storageConfig == null)
			storageConfig = System.getProperty("user.dir");
		this.fileStorageLocation = Paths.get(storageConfig).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String storeFile(MultipartFile file, String userName, String fileName) {
		try {
			Path directoryPath = this.fileStorageLocation.resolve(userName);
			Files.createDirectories(directoryPath);
			Path targetLocation = this.fileStorageLocation.resolve(userName).resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteFile(String fileName, String userName) {
		try {
			Path targetLocation = this.fileStorageLocation.resolve(userName).resolve(fileName);
			Files.delete(targetLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Resource loadFileAsResource(String userName, String fileName) throws Exception {
		Path filePath = this.fileStorageLocation.resolve(userName).resolve(fileName).normalize();
		Resource resource = new UrlResource(filePath.toUri());
		if (resource.exists()) {
			return resource;
		} else {
			throw new Exception("File not found " + fileName);
		}
	}

}
