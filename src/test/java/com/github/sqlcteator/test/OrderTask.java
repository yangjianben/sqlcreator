package com.github.sqlcteator.test;

import java.io.Serializable;
import java.util.Date;

public class OrderTask implements Serializable {

	private static final long serialVersionUID = -9219153310444883120L;
	
	private Long taskId;// '定时id';
	private String taskDate;// '执行时间';
	private Integer orderAmount;// '订单数量';
	private Date ds;// '更新时间';
	private Date ts;// '时间';
	private Date startTs;// 起始时间
	private Date endTs;// 结束时间
	private String batchNumbers;// 批次号
	private String userName;// 用户名字
	private String userId;// 用户id
	
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	public Integer getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Date getDs() {
		return ds;
	}
	public void setDs(Date ds) {
		this.ds = ds;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public Date getStartTs() {
		return startTs;
	}
	public void setStartTs(Date startTs) {
		this.startTs = startTs;
	}
	public Date getEndTs() {
		return endTs;
	}
	public void setEndTs(Date endTs) {
		this.endTs = endTs;
	}
	public String getBatchNumbers() {
		return batchNumbers;
	}
	public void setBatchNumbers(String batchNumbers) {
		this.batchNumbers = batchNumbers;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
