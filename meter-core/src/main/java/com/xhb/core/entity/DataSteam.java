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
@Table(name="data_steam")
public class DataSteam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6471960075832339385L;
	@Id
	@GeneratedValue
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="read_time")
	private Date readTime;
	@Column(name="consumption")
	private Double consumption;
	
	@Column(name="momentary_flow")
	private Double momentaryFlow;
	
	@Column(name="pressure")
	private Double pressure;
	
	@Column(name="temperature")
	private Double temperature;
	
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
	public Double getMomentaryFlow() {
		return momentaryFlow;
	}
	public void setMomentaryFlow(Double momentaryFlow) {
		this.momentaryFlow = momentaryFlow;
	}
	public Double getPressure() {
		return pressure;
	}
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
}
