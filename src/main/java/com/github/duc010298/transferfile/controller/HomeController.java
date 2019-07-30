package com.github.duc010298.transferfile.controller;

import com.github.duc010298.transferfile.configuration.FileStorageConfig;
import com.github.duc010298.transferfile.entity.AppUserEntity;
import com.github.duc010298.transferfile.entity.FileEntity;
import com.github.duc010298.transferfile.entity.FilePathEntity;
import com.github.duc010298.transferfile.repository.AppUserRepository;
import com.github.duc010298.transferfile.repository.FilePathRepository;
import com.github.duc010298.transferfile.repository.FileRepository;
import com.github.duc010298.transferfile.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(path = "/")
public class HomeController {

    private AppUserRepository appUserRepository;
    private FileRepository fileRepository;
    private FilePathRepository filePathRepository;
    private FileStorageService fileStorageService;
    private FileStorageConfig fileStorageConfig;

    @Autowired
    public HomeController(AppUserRepository appUserRepository, FileRepository fileRepository,
                          FilePathRepository filePathRepository, FileStorageService fileStorageService,
                          FileStorageConfig fileStorageConfig) {
        this.appUserRepository = appUserRepository;
        this.fileRepository = fileRepository;
        this.filePathRepository = filePathRepository;
        this.fileStorageService = fileStorageService;
        this.fileStorageConfig = fileStorageConfig;
    }

    @GetMapping
    public String homePage() {
        return "index";
    }

    @GetMapping("/list-file")
    public @ResponseBody
    List<FileEntity> getListFile(Principal principal) {
        String userName = principal.getName();
        AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);
        return fileRepository.findAllByAppUserEntity(appUserEntity);
    }

    @GetMapping("/list-file-path")
    public @ResponseBody
    List<FilePathEntity> getListFilePath(Principal principal) {
        String userName = principal.getName();
        AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);
        return filePathRepository.findAllByAppUserEntity(appUserEntity);
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<?> uploadMultipleFiles(@RequestParam("file") MultipartFile file, Principal principal) {
        String userName = principal.getName();
        AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);

        UUID fileNameUUID = UUID.randomUUID();
        String fileNameOrigin = file.getOriginalFilename();

        //pattern: [origin file name].[key prefix server]_[key join]_[total]_[index part]
        String regexFileName = "^.+\\.[a-zA-Z]{5}_[a-zA-Z]{5}_[0-9]+\\.[0-9]+$";
        if (fileNameOrigin != null && fileNameOrigin.matches(regexFileName)) {
            String[] parts = fileNameOrigin.split("\\.");
            String indexFile = parts[parts.length - 1];
            String content = parts[parts.length - 2];
            parts = content.split("_");
            if (parts[0].equals(fileStorageConfig.getPrefix())) {
                String keyJoin = parts[1];
                Integer total = Integer.parseInt(parts[2]);
                String fileName = fileStorageService.storeFileTemp(file, userName, fileNameUUID.toString(), keyJoin);

                if (fileNameUUID.toString().equals(fileName)) {
                    FilePathEntity filePathEntity = new FilePathEntity();
                    filePathEntity.setFileId(fileNameUUID);
                    filePathEntity.setKeyJoin(keyJoin);
                    filePathEntity.setTotal(total);
                    filePathEntity.setIndexFile(Integer.parseInt(indexFile));
                    filePathEntity.setFileName(fileNameOrigin);
                    filePathEntity.setFileSize(file.getSize());
                    filePathEntity.setDateUpload(new Date());
                    filePathEntity.setAppUserEntity(appUserEntity);
                    filePathRepository.save(filePathEntity);

                    List<FilePathEntity> listFileWithKeyJoin = filePathRepository.findAllByKeyJoinOrderByFileNameAsc(keyJoin);
                    if (listFileWithKeyJoin.size() == total) {
                        fileStorageService.joinFile(fileNameOrigin, keyJoin, userName);
                    }
                    return new ResponseEntity<>(HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        }

        String fileName = fileStorageService.storeFile(file, appUserEntity.getUserName(), fileNameUUID.toString());
        if (fileNameUUID.toString().equals(fileName)) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileId(fileNameUUID);
            fileEntity.setFileName(fileNameOrigin);
            fileEntity.setFileSize(file.getSize());
            fileEntity.setDateUpload(new Date());
            fileEntity.setAppUserEntity(appUserEntity);
            fileRepository.save(fileEntity);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public @ResponseBody
    ResponseEntity<?> deleteAllFile(Principal principal) {
        String userName = principal.getName();
        AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);
        List<FileEntity> listFile = fileRepository.findAllByAppUserEntity(appUserEntity);

        for (FileEntity file : listFile) {
            fileStorageService.deleteFile(file.getFileId().toString(), appUserEntity.getUserName());
            fileRepository.delete(file);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/temp")
    public @ResponseBody
    ResponseEntity<?> deleteAllFileTemp(Principal principal) {
        String userName = principal.getName();
        AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);
        List<FilePathEntity> listFile = filePathRepository.findAllByAppUserEntity(appUserEntity);

        for (FilePathEntity file : listFile) {
            fileStorageService.deleteFileTemp(file.getFileId().toString(), appUserEntity.getUserName(), file.getKeyJoin());
            filePathRepository.delete(file);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{fileId}")
    public @ResponseBody
    ResponseEntity<?> deleteFile(@PathVariable String fileId, Principal principal) {
        try {
            String userName = principal.getName();
            AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);
            Optional<FileEntity> fileOptional = fileRepository.findById(UUID.fromString(fileId));
            if (fileOptional.isPresent()) {
                FileEntity fileEntity = fileOptional.get();
                if (fileEntity.getAppUserEntity().equals(appUserEntity)) {
                    fileStorageService.deleteFile(fileId, appUserEntity.getUserName());
                    fileRepository.delete(fileEntity);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                }
            }
            Optional<FilePathEntity> filePathOptional = filePathRepository.findById(UUID.fromString(fileId));
            if (filePathOptional.isPresent()) {
                FilePathEntity filePathEntity = filePathOptional.get();
                if (filePathEntity.getAppUserEntity().equals(appUserEntity)) {
                    fileStorageService.deleteFileTemp(fileId, appUserEntity.getUserName(), filePathEntity.getKeyJoin());
                    filePathRepository.delete(filePathEntity);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/download-file/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, HttpServletRequest request, Principal principal) {
        try {
            String userName = principal.getName();
            AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);
            Resource resource;
            String originFileName;

            Optional<FileEntity> file = fileRepository.findById(UUID.fromString(fileId));
            if (file.isPresent()) {
                originFileName = file.get().getFileName();
                resource = fileStorageService.loadFileAsResource(appUserEntity.getUserName(), fileId);
            } else {
                Optional<FilePathEntity> filePath = filePathRepository.findById(UUID.fromString(fileId));
                if (filePath.isPresent()) {
                    originFileName = filePath.get().getFileName();
                    resource = fileStorageService.loadTempFileAsResource(appUserEntity.getUserName(), fileId, filePath.get().getKeyJoin());
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }

            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originFileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
