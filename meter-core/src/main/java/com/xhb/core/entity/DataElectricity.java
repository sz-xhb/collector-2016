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
 * The persistent class for the data_electricity database table.
 * 
 */
@Entity
@Table(name="data_electricity")
@NamedQuery(name="DataElectricity.findAll", query="SELECT d FROM DataElectricity d")
public class DataElectricity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 678148467221607350L;

	@Id
	@GeneratedValue
	private Long id;

	private Double current;

	@Column(name="current_a")
	private Double currentA;

	@Column(name="current_b")
	private Double currentB;

	@Column(name="current_c")
	private Double currentC;

	@Enumerated(EnumType.STRING)
	@Column(name="electricity_type")
	private ElectricityType electricityType;

	private Double frequency;

	private Double kva;

	@Column(name="kva_a")
	private Double kvaA;

	@Column(name="kva_b")
	private Double kvaB;

	@Column(name="kva_c")
	private Double kvaC;

	private Double kvar;

	@Column(name="kvar_a")
	private Double kvarA;

	@Column(name="kvar_b")
	private Double kvarB;

	@Column(name="kvar_c")
	private Double kvarC;

	private Double kvarh1;

	private Double kvarh2;

	private Double kw;

	@Column(name="kw_a")
	private Double kwA;

	@Column(name="kw_b")
	private Double kwB;

	@Column(name="kw_c")
	private Double kwC;

	private Double kwh;

	@Column(name="kwh_forward")
	private Double kwhForward;

	@Column(name="kwh_reverse")
	private Double kwhReverse;

	@Column(name="power_factor")
	private Double powerFactor;

	@Column(name="power_factor_a")
	private Double powerFactorA;

	@Column(name="power_factor_b")
	private Double powerFactorB;

	@Column(name="power_factor_c")
	private Double powerFactorC;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="read_time")
	private Date readTime;

	private Double voltage;

	@Column(name="voltage_a")
	private Double voltageA;

	@Column(name="voltage_a_b")
	private Double voltageAB;

	@Column(name="voltage_b")
	private Double voltageB;

	@Column(name="voltage_b_c")
	private Double voltageBC;

	@Column(name="voltage_c")
	private Double voltageC;

	@Column(name="voltage_c_a")
	private Double voltageCA;
	
	@Column(name="kwh_forward_rate1")
	private Double kwhForwardRate1;
	
	@Column(name="kwh_forward_rate2")
	private Double kwhForwardRate2;
	
	@Column(name="kwh_forward_rate3")
	private Double kwhForwardRate3;
	
	@Column(name="kwh_forward_rate4")
	private Double kwhForwardRate4;
	
	@Column(name="kwh_reverse_rate1")
	private Double kwhReverseRate1;
	
	@Column(name="kwh_reverse_rate2")
	private Double kwhReverseRate2;
	
	@Column(name="kwh_reverse_rate3")
	private Double kwhReverseRate3;
	
	@Column(name="kwh_reverse_rate4")
	private Double kwhReverseRate4;

	//bi-directional many-to-one association to ReceiptCircuit
	@ManyToOne
	@JoinColumn(name="circuit_id")
	private ReceiptCircuit receiptCircuit;

	public DataElectricity() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getCurrent() {
		return current;
	}

	public void setCurrent(Double current) {
		this.current = current;
	}

	public Double getCurrentA() {
		return this.currentA;
	}

	public void setCurrentA(Double currentA) {
		this.currentA = currentA;
	}

	public Double getCurrentB() {
		return this.currentB;
	}

	public void setCurrentB(Double currentB) {
		this.currentB = currentB;
	}

	public Double getCurrentC() {
		return this.currentC;
	}

	public void setCurrentC(Double currentC) {
		this.currentC = currentC;
	}

	public ElectricityType getElectricityType() {
		return electricityType;
	}

	public void setElectricityType(ElectricityType electricityType) {
		this.electricityType = electricityType;
	}

	public Double getFrequency() {
		return this.frequency;
	}

	public void setFrequency(Double frequency) {
		this.frequency = frequency;
	}

	public Double getKva() {
		return this.kva;
	}

	public void setKva(Double kva) {
		this.kva = kva;
	}

	public Double getKvaA() {
		return this.kvaA;
	}

	public void setKvaA(Double kvaA) {
		this.kvaA = kvaA;
	}

	public Double getKvaB() {
		return this.kvaB;
	}

	public void setKvaB(Double kvaB) {
		this.kvaB = kvaB;
	}

	public Double getKvaC() {
		return this.kvaC;
	}

	public void setKvaC(Double kvaC) {
		this.kvaC = kvaC;
	}

	public Double getKvar() {
		return this.kvar;
	}

	public void setKvar(Double kvar) {
		this.kvar = kvar;
	}

	public Double getKvarA() {
		return this.kvarA;
	}

	public void setKvarA(Double kvarA) {
		this.kvarA = kvarA;
	}

	public Double getKvarB() {
		return this.kvarB;
	}

	public void setKvarB(Double kvarB) {
		this.kvarB = kvarB;
	}

	public Double getKvarC() {
		return this.kvarC;
	}

	public void setKvarC(Double kvarC) {
		this.kvarC = kvarC;
	}

	public Double getKvarh1() {
		return this.kvarh1;
	}

	public void setKvarh1(Double kvarh1) {
		this.kvarh1 = kvarh1;
	}

	public Double getKvarh2() {
		return this.kvarh2;
	}

	public void setKvarh2(Double kvarh2) {
		this.kvarh2 = kvarh2;
	}

	public Double getKw() {
		return this.kw;
	}

	public void setKw(Double kw) {
		this.kw = kw;
	}

	public Double getKwA() {
		return this.kwA;
	}

	public void setKwA(Double kwA) {
		this.kwA = kwA;
	}

	public Double getKwB() {
		return this.kwB;
	}

	public void setKwB(Double kwB) {
		this.kwB = kwB;
	}

	public Double getKwC() {
		return this.kwC;
	}

	public void setKwC(Double kwC) {
		this.kwC = kwC;
	}

	public Double getKwh() {
		return this.kwh;
	}

	public void setKwh(Double kwh) {
		this.kwh = kwh;
	}

	public Double getKwhForward() {
		return this.kwhForward;
	}

	public void setKwhForward(Double kwhForward) {
		this.kwhForward = kwhForward;
	}

	public Double getKwhReverse() {
		return this.kwhReverse;
	}

	public void setKwhReverse(Double kwhReverse) {
		this.kwhReverse = kwhReverse;
	}

	public Double getPowerFactor() {
		return this.powerFactor;
	}

	public void setPowerFactor(Double powerFactor) {
		this.powerFactor = powerFactor;
	}

	public Double getPowerFactorA() {
		return this.powerFactorA;
	}

	public void setPowerFactorA(Double powerFactorA) {
		this.powerFactorA = powerFactorA;
	}

	public Double getPowerFactorB() {
		return this.powerFactorB;
	}

	public void setPowerFactorB(Double powerFactorB) {
		this.powerFactorB = powerFactorB;
	}

	public Double getPowerFactorC() {
		return this.powerFactorC;
	}

	public void setPowerFactorC(Double powerFactorC) {
		this.powerFactorC = powerFactorC;
	}

	public Date getReadTime() {
		return this.readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Double getVoltage() {
		return voltage;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}

	public Double getVoltageA() {
		return this.voltageA;
	}

	public void setVoltageA(Double voltageA) {
		this.voltageA = voltageA;
	}

	public Double getVoltageAB() {
		return this.voltageAB;
	}

	public void setVoltageAB(Double voltageAB) {
		this.voltageAB = voltageAB;
	}

	public Double getVoltageB() {
		return this.voltageB;
	}

	public void setVoltageB(Double voltageB) {
		this.voltageB = voltageB;
	}

	public Double getVoltageBC() {
		return this.voltageBC;
	}

	public void setVoltageBC(Double voltageBC) {
		this.voltageBC = voltageBC;
	}

	public Double getVoltageC() {
		return this.voltageC;
	}

	public void setVoltageC(Double voltageC) {
		this.voltageC = voltageC;
	}

	public Double getVoltageCA() {
		return this.voltageCA;
	}

	public void setVoltageCA(Double voltageCA) {
		this.voltageCA = voltageCA;
	}

	public ReceiptCircuit getReceiptCircuit() {
		return this.receiptCircuit;
	}

	public void setReceiptCircuit(ReceiptCircuit receiptCircuit) {
		this.receiptCircuit = receiptCircuit;
	}

	public Double getKwhForwardRate1() {
		return kwhForwardRate1;
	}

	public void setKwhForwardRate1(Double kwhForwardRate1) {
		this.kwhForwardRate1 = kwhForwardRate1;
	}

	public Double getKwhForwardRate2() {
		return kwhForwardRate2;
	}

	public void setKwhForwardRate2(Double kwhForwardRate2) {
		this.kwhForwardRate2 = kwhForwardRate2;
	}

	public Double getKwhForwardRate3() {
		return kwhForwardRate3;
	}

	public void setKwhForwardRate3(Double kwhForwardRate3) {
		this.kwhForwardRate3 = kwhForwardRate3;
	}

	public Double getKwhForwardRate4() {
		return kwhForwardRate4;
	}

	public void setKwhForwardRate4(Double kwhForwardRate4) {
		this.kwhForwardRate4 = kwhForwardRate4;
	}

	public Double getKwhReverseRate1() {
		return kwhReverseRate1;
	}

	public void setKwhReverseRate1(Double kwhReverseRate1) {
		this.kwhReverseRate1 = kwhReverseRate1;
	}

	public Double getKwhReverseRate2() {
		return kwhReverseRate2;
	}

	public void setKwhReverseRate2(Double kwhReverseRate2) {
		this.kwhReverseRate2 = kwhReverseRate2;
	}

	public Double getKwhReverseRate3() {
		return kwhReverseRate3;
	}

	public void setKwhReverseRate3(Double kwhReverseRate3) {
		this.kwhReverseRate3 = kwhReverseRate3;
	}

	public Double getKwhReverseRate4() {
		return kwhReverseRate4;
	}

	public void setKwhReverseRate4(Double kwhReverseRate4) {
		this.kwhReverseRate4 = kwhReverseRate4;
	}
	

}