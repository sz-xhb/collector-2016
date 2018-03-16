package com.xhb.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="data_water")
public class DataWater implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3799990190889975163L;
	@Id
	@GeneratedValue
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="read_time")
	private Date readTime;
	@Column(name="consumption")
	private Double consumption;
	
	
	@ManyToOne
	@JoinColumn(name="circuit_id")
	private ReceiptCircuit receiptCircuit;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	public Double getConsumption() {
		return consumption;
	}
	public void setConsumption(Double consumption) {
		this.consumption = consumption;
	}
	public ReceiptCircuit getReceiptCircuit() {
		return receiptCircuit;
	}
	public void setReceiptCircuit(ReceiptCircuit receiptCircuit) {
		this.receiptCircuit = receiptCircuit;
	}
	
	

}