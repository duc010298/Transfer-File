package com.github.duc010298.transferfile.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "file")
public class FilePathEntity {
    private UUID fileId;
    private String keyJoin;
    private Integer total;
    private Integer indexFile;
    private String fileName;
    private Long fileSize;
    private Date dateUpload;
    private AppUserEntity appUserEntity;

    @Id
    @Column(name = "file_id")
    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    @Basic
    @Column(name = "total")
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Basic
    @Column(name = "index_file")
    public Integer getIndexFile() {
        return indexFile;
    }

    public void setIndexFile(Integer indexFile) {
        this.indexFile = indexFile;
    }


    @Basic
    @Column(name = "key_join")
    public String getKeyJoin() {
        return keyJoin;
    }

    public void setKeyJoin(String keyJoin) {
        this.keyJoin = keyJoin;
    }

    @Basic
    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Basic
    @Column(name = "file_size")
    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_upload")
    public Date getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(Date dateUpload) {
        this.dateUpload = dateUpload;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public AppUserEntity getAppUserEntity() {
        return this.appUserEntity;
    }

    public void setAppUserEntity(AppUserEntity appUserEntity) {
        this.appUserEntity = appUserEntity;
    }
}
