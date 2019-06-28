package com.github.duc010298.transferfile.repository;

import com.github.duc010298.transferfile.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {

    AppUserEntity findByUserName(String userName);
}