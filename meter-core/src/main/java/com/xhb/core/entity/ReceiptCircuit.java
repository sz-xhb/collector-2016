package com.xhb.core.entity;

import java.io.Serializable;
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

import org.codehaus.jackson.annotate.JsonBackReference;


/**
 * The persistent class for the receipt_circuit database table.
 * 
 */
@Entity
@Table(name="receipt_circuit")
@NamedQuery(name="ReceiptCircuit.findAll", query="SELECT r FROM ReceiptCircuit r")
public class ReceiptCircuit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1736489412882679287L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name="circuit_no")
	private String circuitNo;

	@Enumerated(EnumType.STRING)
	@Column(name="circuit_type")
	private CircuitType circuitType;

	@Column(name="dsm_id")
	private String dsmId;
	
	@Column(name="voltage_ratio")
	private Integer voltageRatio;
	
//	@Column(name="monitor_id")
//	private String monitorId;
//	
//	public String getMonitorId() {
//		return monitorId;
//	}
//
//	public void setMonitorId(String monitorId) {
//		this.monitorId = monitorId;
//	}

	public Integer getVoltageRatio() {
		return voltageRatio;
	}

	public void setVoltageRatio(Integer voltageRatio) {
		this.voltageRatio = voltageRatio;
	}

	public Integer getCurrentRatio() {
		return currentRatio;
	}

	public void setCurrentRatio(Integer currentRatio) {
		this.currentRatio = currentRatio;
	}

	@Column(name="current_ratio")
	private Integer currentRatio;

	private String name;

	@JsonBackReference
	//bi-directional many-to-one association to ReceiptMeter
	@ManyToOne
	@JoinColumn(name="meter_id")
	private ReceiptMeter receiptMeter;
	
	
	public ReceiptCircuit() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCircuitNo() {
		return this.circuitNo;
	}

	public void setCircuitNo(String circuitNo) {
		this.circuitNo = circuitNo;
	}

	public CircuitType getCircuitType() {
		return this.circuitType;
	}

	public void setCircuitType(CircuitType circuitType) {
		this.circuitType = circuitType;
	}

	public String getDsmId() {
		return this.dsmId;
	}

	public void setDsmId(String dsmId) {
		this.dsmId = dsmId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ReceiptMeter getReceiptMeter() {
		return this.receiptMeter;
	}

	public void setReceiptMeter(ReceiptMeter receiptMeter) {
		this.receiptMeter = receiptMeter;
	}
}