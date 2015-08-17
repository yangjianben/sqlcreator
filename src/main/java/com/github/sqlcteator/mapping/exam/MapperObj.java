package com.github.sqlcteator.mapping.exam;

import java.io.Serializable;
import java.util.Date;

public class MapperObj implements Serializable {

	private static final long serialVersionUID = 7294679835548504971L;

	private Integer id;

	private String name;

	private String desc;

	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "MapperObj [id=" + id + ", name=" + name + ", desc=" + desc + ", createDate=" + createDate + "]";
	}
}
