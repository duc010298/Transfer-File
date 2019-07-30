package com.github.duc010298.transferfile.repository;

import java.util.List;
import java.util.UUID;

import com.github.duc010298.transferfile.entity.FilePathEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.duc010298.transferfile.entity.AppUserEntity;

public interface FilePathRepository extends JpaRepository<FilePathEntity, UUID> {

    List<FilePathEntity> findAllByAppUserEntity(AppUserEntity appUserEntity);
}
