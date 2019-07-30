package com.github.duc010298.transferfile.service;

import com.github.duc010298.transferfile.configuration.FileStorageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {
    private static String TEMP_FOLDER = "TEMP";

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        String storageConfig = fileStorageConfig.getUploadDir();
        if (storageConfig == null) {
            storageConfig = System.getProperty("user.dir");
        }
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

    public String storeFileTemp(MultipartFile file, String userName, String fileName, String key) {
        try {
            Path directoryKeyPath = this.fileStorageLocation.resolve(userName).resolve(TEMP_FOLDER).resolve(key);
            Files.createDirectories(directoryKeyPath);
            Path targetLocation = this.fileStorageLocation.resolve(userName).resolve(TEMP_FOLDER).resolve(key).resolve(fileName);
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
            Files.deleteIfExists(targetLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFileTemp(String fileName, String userName, String key) {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(userName).resolve(TEMP_FOLDER).resolve(key).resolve(fileName);
            Files.deleteIfExists(targetLocation);
            Path keyFolder = this.fileStorageLocation.resolve(userName).resolve(TEMP_FOLDER).resolve(key);
            if (isDirEmpty(keyFolder)) {
                Files.deleteIfExists(keyFolder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isDirEmpty(final Path directory) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    public Resource loadTempFileAsResource(String userName, String fileName, String key) throws Exception {
        Path filePath = this.fileStorageLocation.resolve(userName).resolve(TEMP_FOLDER).resolve(key).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new Exception("File not found " + fileName);
        }
    }
}
