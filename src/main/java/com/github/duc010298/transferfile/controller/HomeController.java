package com.github.duc010298.transferfile.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.duc010298.transferfile.entity.AppUserEntity;
import com.github.duc010298.transferfile.entity.FileEntity;
import com.github.duc010298.transferfile.repository.AppUserRepository;
import com.github.duc010298.transferfile.repository.FileRepository;
import com.github.duc010298.transferfile.service.FileStorageService;

@Controller
@RequestMapping(path = "/")
public class HomeController {

	private FileStorageService fileStorageService;
	private AppUserRepository appUserRepository;
	private FileRepository fileRepository;

	@Autowired
	public HomeController(FileStorageService fileStorageService, AppUserRepository appUserRepository,
			FileRepository fileRepository) {
		this.fileStorageService = fileStorageService;
		this.appUserRepository = appUserRepository;
		this.fileRepository = fileRepository;
	}

	@GetMapping
	public String homePage() {
		return "index";
	}

	@GetMapping("/list-file")
	public @ResponseBody List<FileEntity> getListFile(Principal principal) {
		String userName = principal.getName();
		AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);
		List<FileEntity> listFile = fileRepository.findAllByAppUserEntity(appUserEntity);
		return listFile;
	}

	@PostMapping
	public @ResponseBody ResponseEntity<?> uploadMultipleFiles(@RequestParam("file") MultipartFile file,
			Principal principal) {
		String userName = principal.getName();
		AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);

		UUID fileNameUUID = UUID.randomUUID();
		String fileName = fileStorageService.storeFile(file, appUserEntity.getUserName(), fileNameUUID.toString());

		if (fileNameUUID.toString().equals(fileName)) {
			FileEntity fileEntity = new FileEntity();
			fileEntity.setFileId(fileNameUUID);
			fileEntity.setFileName(file.getOriginalFilename());
			fileEntity.setFileSize(file.getSize());
			fileEntity.setDateUpload(new Date());
			fileEntity.setAppUserEntity(appUserEntity);
			fileRepository.save(fileEntity);
			return new ResponseEntity<Object>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping
	public @ResponseBody ResponseEntity<?> deleteAllFile(Principal principal) {
		String userName = principal.getName();
		AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);
		List<FileEntity> listFile = fileRepository.findAllByAppUserEntity(appUserEntity);

		for (FileEntity file : listFile) {
			fileStorageService.deleteFile(file.getFileId().toString(), appUserEntity.getUserName());
			fileRepository.delete(file);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@GetMapping("/download-file/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, HttpServletRequest request,
			Principal principal) {
		try {
			String userName = principal.getName();
			AppUserEntity appUserEntity = appUserRepository.findByUserName(userName);

			Optional<FileEntity> file = fileRepository.findById(UUID.fromString(fileId));
			String originFileName = file.get().getFileName();

			Resource resource = fileStorageService.loadFileAsResource(appUserEntity.getUserName(), fileId);
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
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
	}
}