package com.github.sqlcteator.test.po;

import java.util.Date;

public class WhPickingWallDetail implements BaseEntity {
	private static final long serialVersionUID = 5799513390968784339L;
	private Long id;
	private Long wallId;
	private Long wallSetId;
	private String wallNo;
	private String bizNo;
	private String logiNo;
	private String status;
	private Date createdDate;
	private Long creatorId;
	private String creatorName;
	private Date modiDate;
	private Long modifierId;
	private String modifierName;
	private String comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWallId() {
		return wallId;
	}

	public void setWallId(Long wallId) {
		this.wallId = wallId;
	}

	public Long getWallSetId() {
		return wallSetId;
	}

	public void setWallSetId(Long wallSetId) {
		this.wallSetId = wallSetId;
	}

	public String getWallNo() {
		return wallNo;
	}

	public void setWallNo(String wallNo) {
		this.wallNo = wallNo;
	}

	public String getBizNo() {
		return bizNo;
	}

	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}

	public String getLogiNo() {
		return logiNo;
	}

	public void setLogiNo(String logiNo) {
		this.logiNo = logiNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getModiDate() {
		return modiDate;
	}

	public void setModiDate(Date modiDate) {
		this.modiDate = modiDate;
	}

	public Long getModifierId() {
		return modifierId;
	}

	public void setModifierId(Long modifierId) {
		this.modifierId = modifierId;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
