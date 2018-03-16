package com.xhb.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="meter_status")
@NamedQuery(name="MeterStatus.findAll", query="SELECT d FROM MeterStatus d")
public class MeterStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1664588427960170465L;

	@Id
	@Column(name="meter_id")
	private Long meterId;
	
	@OneToOne
	@JoinColumn(name="meter_id")
	private ReceiptMeter receiptMeter;
	
	@Column(name="active_time")
	private Date  activeTime;
	
	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public ReceiptMeter getReceiptMeter() {
		return receiptMeter;
	}

	public void setReceiptMeter(ReceiptMeter receiptMeter) {
		this.receiptMeter = receiptMeter;
	}
}
