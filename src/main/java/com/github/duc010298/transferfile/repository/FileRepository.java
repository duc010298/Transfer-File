package com.github.duc010298.transferfile.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.duc010298.transferfile.entity.AppUserEntity;
import com.github.duc010298.transferfile.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

	List<FileEntity> findAllByAppUserEntity(AppUserEntity appUserEntity);
}
