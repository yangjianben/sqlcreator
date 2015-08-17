package com.github.sqlcteator.mapping.exam;

import java.io.Serializable;

import com.github.sqlcteator.mapping.annotations.Fields;

public class OldObject_2 implements Serializable {

	private static final long serialVersionUID = -4805631877796365597L;

	private Integer cid;

	private String nickName;

	@Fields(name = "description")
	private String desc;

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "OldObject_2 [cid=" + cid + ", nickName=" + nickName + ", desc=" + desc + "]";
	}

}
