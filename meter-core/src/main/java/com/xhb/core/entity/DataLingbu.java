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
 * The persistent class for the data_lingbu database table.
 * 
 */
@Entity
@Table(name="data_lingbu")
@NamedQuery(name="DataLingbu.findAll", query="SELECT d FROM DataLingbu d")
public class DataLingbu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9160199718862903615L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name="apf_current_a")
	private Double apfCurrentA;

	@Column(name="apf_current_b")
	private Double apfCurrentB;

	@Column(name="apf_current_c")
	private Double apfCurrentC;

	@Column(name="apf_current_n")
	private Double apfCurrentN;

	@Column(name="apf_voltage_dc")
	private Double apfVoltageDc;

	@Column(name="control_switch")
	private Integer controlSwitch;

	@Column(name="current_protect")
	private Integer currentProtect;

	@Column(name="dc_voltage_protect")
	private Integer dcVoltageProtect;

	@Column(name="distortion_current")
	private Double distortionCurrent;

	private Double kva;

	private Double kvar;

	private Double kw;

	@Column(name="load_current_a")
	private Double loadCurrentA;

	@Column(name="load_current_b")
	private Double loadCurrentB;

	@Column(name="load_current_c")
	private Double loadCurrentC;

	@Column(name="over_voltage_protect")
	private Integer overVoltageProtect;

	@Column(name="power_factor")
	private Double powerFactor;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="read_time")
	private Date readTime;

	@Column(name="reset_switch")
	private Integer resetSwitch;

	@Column(name="start_switch")
	private Integer startSwitch;

	@Column(name="supp_voltage_a")
	private Double suppVoltageA;

	@Column(name="supp_voltage_b")
	private Double suppVoltageB;

	@Column(name="supp_voltage_c")
	private Double suppVoltageC;

	@Column(name="temperature_protect")
	private Integer temperatureProtect;

	@Column(name="under_voltage_protect")
	private Integer underVoltageProtect;

	//bi-directional many-to-one association to ReceiptCircuit
	@ManyToOne
	@JoinColumn(name="circuit_id")
	private ReceiptCircuit receiptCircuit;

	public DataLingbu() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getApfCurrentA() {
		return this.apfCurrentA;
	}

	public void setApfCurrentA(Double apfCurrentA) {
		this.apfCurrentA = apfCurrentA;
	}

	public Double getApfCurrentB() {
		return this.apfCurrentB;
	}

	public void setApfCurrentB(Double apfCurrentB) {
		this.apfCurrentB = apfCurrentB;
	}

	public Double getApfCurrentC() {
		return this.apfCurrentC;
	}

	public void setApfCurrentC(Double apfCurrentC) {
		this.apfCurrentC = apfCurrentC;
	}

	public Double getApfCurrentN() {
		return this.apfCurrentN;
	}

	public void setApfCurrentN(Double apfCurrentN) {
		this.apfCurrentN = apfCurrentN;
	}

	public Double getApfVoltageDc() {
		return this.apfVoltageDc;
	}

	public void setApfVoltageDc(Double apfVoltageDc) {
		this.apfVoltageDc = apfVoltageDc;
	}

	public Integer getControlSwitch() {
		return this.controlSwitch;
	}

	public void setControlSwitch(Integer controlSwitch) {
		this.controlSwitch = controlSwitch;
	}

	public Integer getCurrentProtect() {
		return this.currentProtect;
	}

	public void setCurrentProtect(Integer currentProtect) {
		this.currentProtect = currentProtect;
	}

	public Integer getDcVoltageProtect() {
		return this.dcVoltageProtect;
	}

	public void setDcVoltageProtect(Integer dcVoltageProtect) {
		this.dcVoltageProtect = dcVoltageProtect;
	}

	public Double getDistortionCurrent() {
		return this.distortionCurrent;
	}

	public void setDistortionCurrent(Double distortionCurrent) {
		this.distortionCurrent = distortionCurrent;
	}

	public Double getKva() {
		return this.kva;
	}

	public void setKva(Double kva) {
		this.kva = kva;
	}

	public Double getKvar() {
		return this.kvar;
	}

	public void setKvar(Double kvar) {
		this.kvar = kvar;
	}

	public Double getKw() {
		return this.kw;
	}

	public void setKw(Double kw) {
		this.kw = kw;
	}

	public Double getLoadCurrentA() {
		return this.loadCurrentA;
	}

	public void setLoadCurrentA(Double loadCurrentA) {
		this.loadCurrentA = loadCurrentA;
	}

	public Double getLoadCurrentB() {
		return this.loadCurrentB;
	}

	public void setLoadCurrentB(Double loadCurrentB) {
		this.loadCurrentB = loadCurrentB;
	}

	public Double getLoadCurrentC() {
		return this.loadCurrentC;
	}

	public void setLoadCurrentC(Double loadCurrentC) {
		this.loadCurrentC = loadCurrentC;
	}

	public Integer getOverVoltageProtect() {
		return this.overVoltageProtect;
	}

	public void setOverVoltageProtect(Integer overVoltageProtect) {
		this.overVoltageProtect = overVoltageProtect;
	}

	public Double getPowerFactor() {
		return this.powerFactor;
	}

	public void setPowerFactor(Double powerFactor) {
		this.powerFactor = powerFactor;
	}

	public Date getReadTime() {
		return this.readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Integer getResetSwitch() {
		return this.resetSwitch;
	}

	public void setResetSwitch(Integer resetSwitch) {
		this.resetSwitch = resetSwitch;
	}

	public Integer getStartSwitch() {
		return this.startSwitch;
	}

	public void setStartSwitch(Integer startSwitch) {
		this.startSwitch = startSwitch;
	}

	public Double getSuppVoltageA() {
		return this.suppVoltageA;
	}

	public void setSuppVoltageA(Double suppVoltageA) {
		this.suppVoltageA = suppVoltageA;
	}

	public Double getSuppVoltageB() {
		return this.suppVoltageB;
	}

	public void setSuppVoltageB(Double suppVoltageB) {
		this.suppVoltageB = suppVoltageB;
	}

	public Double getSuppVoltageC() {
		return this.suppVoltageC;
	}

	public void setSuppVoltageC(Double suppVoltageC) {
		this.suppVoltageC = suppVoltageC;
	}

	public Integer getTemperatureProtect() {
		return this.temperatureProtect;
	}

	public void setTemperatureProtect(Integer temperatureProtect) {
		this.temperatureProtect = temperatureProtect;
	}

	public Integer getUnderVoltageProtect() {
		return this.underVoltageProtect;
	}

	public void setUnderVoltageProtect(Integer underVoltageProtect) {
		this.underVoltageProtect = underVoltageProtect;
	}

	public ReceiptCircuit getReceiptCircuit() {
		return this.receiptCircuit;
	}

	public void setReceiptCircuit(ReceiptCircuit receiptCircuit) {
		this.receiptCircuit = receiptCircuit;
	}

}
