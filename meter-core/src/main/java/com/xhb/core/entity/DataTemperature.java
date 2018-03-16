package com.xhb.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the data_temperature database table.
 * 
 */
@Entity
@Table(name="data_temperature")
@NamedQuery(name="DataTemperature.findAll", query="SELECT d FROM DataTemperature d")
public class DataTemperature implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4359314684183881648L;

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="read_time")
	private Date readTime;

	private Double temperature;

	//bi-directional many-to-one association to ReceiptCircuit
	@ManyToOne
	@JoinColumn(name="circuit_id")
	private ReceiptCircuit receiptCircuit;

	public DataTemperature() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getReadTime() {
		return this.readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Double getTemperature() {
		return this.temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public ReceiptCircuit getReceiptCircuit() {
		return this.receiptCircuit;
	}

	public void setReceiptCircuit(ReceiptCircuit receiptCircuit) {
		this.receiptCircuit = receiptCircuit;
	}

}