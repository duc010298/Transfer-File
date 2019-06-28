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

@Entity
@Table(name = "file")
public class FileEntity {
	private UUID fileId;
	private String fileName;
	private String fileUrl;
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
    @Column(name = "file_name")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Basic
    @Column(name = "file_url")
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	public AppUserEntity getAppUserEntity() {
		return this.appUserEntity;
	}

	public void setAppUserEntity(AppUserEntity appUserEntity) {
		this.appUserEntity = appUserEntity;
	}
}
