package com.xhb.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the data_switch database table.
 * 
 */
@Entity
@Table(name="data_switch")
@NamedQuery(name="DataSwitch.findAll", query="SELECT d FROM DataSwitch d")
public class DataSwitch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2643984453562028266L;

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="read_time")
	private Date readTime;

	@Enumerated(EnumType.STRING)
	private SwitchStatus status;

	//bi-directional many-to-one association to ReceiptCircuit
	@ManyToOne
	@JoinColumn(name="circuit_id")
	private ReceiptCircuit receiptCircuit;

	public DataSwitch() {
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

	public SwitchStatus getStatus() {
		return status;
	}

	public void setStatus(SwitchStatus status) {
		this.status = status;
	}

	public ReceiptCircuit getReceiptCircuit() {
		return this.receiptCircuit;
	}

	public void setReceiptCircuit(ReceiptCircuit receiptCircuit) {
		this.receiptCircuit = receiptCircuit;
	}

}