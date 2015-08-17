package com.github.sqlcteator.test.po;

import java.util.Date;


/**
 * 实体的基类，每一个有 ID(long型)的，都有一个 UUID(String(32))的字段
 * @author Wangshubing
 */

public interface BaseEntity extends java.io.Serializable{
	
	
	
	public Long getId();
	public void setId(Long id);	
	
	public void setCreatedDate(Date createdDate);
	public Date getCreatedDate();
	public Long getCreatorId() ;
	public void setCreatorId(Long creatorId);
	public String getCreatorName();
	public void setCreatorName(String creatorName);
	
	public Date getModiDate() ;
	public void setModiDate(Date modiDate) ;
	public Long getModifierId();
	public void setModifierId(Long modifierId);
	public String getModifierName() ;
	public void setModifierName(String modifierName);
	
}
