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
 * The persistent class for the data_harmonic database table.
 * 
 */
@Entity
@Table(name="data_harmonic")
@NamedQuery(name="DataHarmonic.findAll", query="SELECT d FROM DataHarmonic d")
public class DataHarmonic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7743861092781363706L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name="distortion_current")
	private Double distortionCurrent;

	@Column(name="distortion_current_a")
	private Double distortionCurrentA;

	@Column(name="distortion_current_b")
	private Double distortionCurrentB;

	@Column(name="distortion_current_c")
	private Double distortionCurrentC;

	@Column(name="distortion_voltage")
	private Double distortionVoltage;

	@Column(name="distortion_voltage_a")
	private Double distortionVoltageA;

	@Column(name="distortion_voltage_b")
	private Double distortionVoltageB;

	@Column(name="distortion_voltage_c")
	private Double distortionVoltageC;

	@Column(name="even_distortion_current_a")
	private Double evenDistortionCurrentA;

	@Column(name="even_distortion_current_b")
	private Double evenDistortionCurrentB;

	@Column(name="even_distortion_current_c")
	private Double evenDistortionCurrentC;

	@Column(name="even_distortion_voltage_a")
	private Double evenDistortionVoltageA;

	@Column(name="even_distortion_voltage_b")
	private Double evenDistortionVoltageB;

	@Column(name="even_distortion_voltage_c")
	private Double evenDistortionVoltageC;

	@Column(name="odd_distortion_current_a")
	private Double oddDistortionCurrentA;

	@Column(name="odd_distortion_current_b")
	private Double oddDistortionCurrentB;

	@Column(name="odd_distortion_current_c")
	private Double oddDistortionCurrentC;

	@Column(name="odd_distortion_voltage_a")
	private Double oddDistortionVoltageA;

	@Column(name="odd_distortion_voltage_b")
	private Double oddDistortionVoltageB;

	@Column(name="odd_distortion_voltage_c")
	private Double oddDistortionVoltageC;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="read_time")
	private Date readTime;

	@Column(name="hr02_voltage_a")
	private Double hr02VoltageA;
	@Column(name="hr03_voltage_a")
	private Double hr03VoltageA;
	@Column(name="hr04_voltage_a")
	private Double hr04VoltageA;
	@Column(name="hr05_voltage_a")
	private Double hr05VoltageA;
	@Column(name="hr06_voltage_a")
	private Double hr06VoltageA;
	@Column(name="hr07_voltage_a")
	private Double hr07VoltageA;
	@Column(name="hr08_voltage_a")
	private Double hr08VoltageA;
	@Column(name="hr09_voltage_a")
	private Double hr09VoltageA;
	@Column(name="hr10_voltage_a")
	private Double hr10VoltageA;
	@Column(name="hr11_voltage_a")
	private Double hr11VoltageA;
	@Column(name="hr12_voltage_a")
	private Double hr12VoltageA;
	@Column(name="hr13_voltage_a")
	private Double hr13VoltageA;
	@Column(name="hr14_voltage_a")
	private Double hr14VoltageA;
	@Column(name="hr15_voltage_a")
	private Double hr15VoltageA;
	@Column(name="hr16_voltage_a")
	private Double hr16VoltageA;
	@Column(name="hr17_voltage_a")
	private Double hr17VoltageA;
	@Column(name="hr18_voltage_a")
	private Double hr18VoltageA;
	@Column(name="hr19_voltage_a")
	private Double hr19VoltageA;
	@Column(name="hr20_voltage_a")
	private Double hr20VoltageA;
	@Column(name="hr21_voltage_a")
	private Double hr21VoltageA;
	@Column(name="hr22_voltage_a")
	private Double hr22VoltageA;
	@Column(name="hr23_voltage_a")
	private Double hr23VoltageA;
	@Column(name="hr24_voltage_a")
	private Double hr24VoltageA;
	@Column(name="hr25_voltage_a")
	private Double hr25VoltageA;
	@Column(name="hr26_voltage_a")
	private Double hr26VoltageA;
	@Column(name="hr27_voltage_a")
	private Double hr27VoltageA;
	@Column(name="hr28_voltage_a")
	private Double hr28VoltageA;
	@Column(name="hr29_voltage_a")
	private Double hr29VoltageA;
	@Column(name="hr30_voltage_a")
	private Double hr30VoltageA;
	@Column(name="hr31_voltage_a")
	private Double hr31VoltageA;

	@Column(name="hr02_voltage_b")
	private Double hr02VoltageB;
	@Column(name="hr03_voltage_b")
	private Double hr03VoltageB;
	@Column(name="hr04_voltage_b")
	private Double hr04VoltageB;
	@Column(name="hr05_voltage_b")
	private Double hr05VoltageB;
	@Column(name="hr06_voltage_b")
	private Double hr06VoltageB;
	@Column(name="hr07_voltage_b")
	private Double hr07VoltageB;
	@Column(name="hr08_voltage_b")
	private Double hr08VoltageB;
	@Column(name="hr09_voltage_b")
	private Double hr09VoltageB;
	@Column(name="hr10_voltage_b")
	private Double hr10VoltageB;
	@Column(name="hr11_voltage_b")
	private Double hr11VoltageB;
	@Column(name="hr12_voltage_b")
	private Double hr12VoltageB;
	@Column(name="hr13_voltage_b")
	private Double hr13VoltageB;
	@Column(name="hr14_voltage_b")
	private Double hr14VoltageB;
	@Column(name="hr15_voltage_b")
	private Double hr15VoltageB;
	@Column(name="hr16_voltage_b")
	private Double hr16VoltageB;
	@Column(name="hr17_voltage_b")
	private Double hr17VoltageB;
	@Column(name="hr18_voltage_b")
	private Double hr18VoltageB;
	@Column(name="hr19_voltage_b")
	private Double hr19VoltageB;
	@Column(name="hr20_voltage_b")
	private Double hr20VoltageB;
	@Column(name="hr21_voltage_b")
	private Double hr21VoltageB;
	@Column(name="hr22_voltage_b")
	private Double hr22VoltageB;
	@Column(name="hr23_voltage_b")
	private Double hr23VoltageB;
	@Column(name="hr24_voltage_b")
	private Double hr24VoltageB;
	@Column(name="hr25_voltage_b")
	private Double hr25VoltageB;
	@Column(name="hr26_voltage_b")
	private Double hr26VoltageB;
	@Column(name="hr27_voltage_b")
	private Double hr27VoltageB;
	@Column(name="hr28_voltage_b")
	private Double hr28VoltageB;
	@Column(name="hr29_voltage_b")
	private Double hr29VoltageB;
	@Column(name="hr30_voltage_b")
	private Double hr30VoltageB;
	@Column(name="hr31_voltage_b")
	private Double hr31VoltageB;

	@Column(name="hr02_voltage_c")
	private Double hr02VoltageC;
	@Column(name="hr03_voltage_c")
	private Double hr03VoltageC;
	@Column(name="hr04_voltage_c")
	private Double hr04VoltageC;
	@Column(name="hr05_voltage_c")
	private Double hr05VoltageC;
	@Column(name="hr06_voltage_c")
	private Double hr06VoltageC;
	@Column(name="hr07_voltage_c")
	private Double hr07VoltageC;
	@Column(name="hr08_voltage_c")
	private Double hr08VoltageC;
	@Column(name="hr09_voltage_c")
	private Double hr09VoltageC;
	@Column(name="hr10_voltage_c")
	private Double hr10VoltageC;
	@Column(name="hr11_voltage_c")
	private Double hr11VoltageC;
	@Column(name="hr12_voltage_c")
	private Double hr12VoltageC;
	@Column(name="hr13_voltage_c")
	private Double hr13VoltageC;
	@Column(name="hr14_voltage_c")
	private Double hr14VoltageC;
	@Column(name="hr15_voltage_c")
	private Double hr15VoltageC;
	@Column(name="hr16_voltage_c")
	private Double hr16VoltageC;
	@Column(name="hr17_voltage_c")
	private Double hr17VoltageC;
	@Column(name="hr18_voltage_c")
	private Double hr18VoltageC;
	@Column(name="hr19_voltage_c")
	private Double hr19VoltageC;
	@Column(name="hr20_voltage_c")
	private Double hr20VoltageC;
	@Column(name="hr21_voltage_c")
	private Double hr21VoltageC;
	@Column(name="hr22_voltage_c")
	private Double hr22VoltageC;
	@Column(name="hr23_voltage_c")
	private Double hr23VoltageC;
	@Column(name="hr24_voltage_c")
	private Double hr24VoltageC;
	@Column(name="hr25_voltage_c")
	private Double hr25VoltageC;
	@Column(name="hr26_voltage_c")
	private Double hr26VoltageC;
	@Column(name="hr27_voltage_c")
	private Double hr27VoltageC;
	@Column(name="hr28_voltage_c")
	private Double hr28VoltageC;
	@Column(name="hr29_voltage_c")
	private Double hr29VoltageC;
	@Column(name="hr30_voltage_c")
	private Double hr30VoltageC;
	@Column(name="hr31_voltage_c")
	private Double hr31VoltageC;

	@Column(name="hr02_current_a")
	private Double hr02CurrentA;
	@Column(name="hr03_current_a")
	private Double hr03CurrentA;
	@Column(name="hr04_current_a")
	private Double hr04CurrentA;
	@Column(name="hr05_current_a")
	private Double hr05CurrentA;
	@Column(name="hr06_current_a")
	private Double hr06CurrentA;
	@Column(name="hr07_current_a")
	private Double hr07CurrentA;
	@Column(name="hr08_current_a")
	private Double hr08CurrentA;
	@Column(name="hr09_current_a")
	private Double hr09CurrentA;
	@Column(name="hr10_current_a")
	private Double hr10CurrentA;
	@Column(name="hr11_current_a")
	private Double hr11CurrentA;
	@Column(name="hr12_current_a")
	private Double hr12CurrentA;
	@Column(name="hr13_current_a")
	private Double hr13CurrentA;
	@Column(name="hr14_current_a")
	private Double hr14CurrentA;
	@Column(name="hr15_current_a")
	private Double hr15CurrentA;
	@Column(name="hr16_current_a")
	private Double hr16CurrentA;
	@Column(name="hr17_current_a")
	private Double hr17CurrentA;
	@Column(name="hr18_current_a")
	private Double hr18CurrentA;
	@Column(name="hr19_current_a")
	private Double hr19CurrentA;
	@Column(name="hr20_current_a")
	private Double hr20CurrentA;
	@Column(name="hr21_current_a")
	private Double hr21CurrentA;
	@Column(name="hr22_current_a")
	private Double hr22CurrentA;
	@Column(name="hr23_current_a")
	private Double hr23CurrentA;
	@Column(name="hr24_current_a")
	private Double hr24CurrentA;
	@Column(name="hr25_current_a")
	private Double hr25CurrentA;
	@Column(name="hr26_current_a")
	private Double hr26CurrentA;
	@Column(name="hr27_current_a")
	private Double hr27CurrentA;
	@Column(name="hr28_current_a")
	private Double hr28CurrentA;
	@Column(name="hr29_current_a")
	private Double hr29CurrentA;
	@Column(name="hr30_current_a")
	private Double hr30CurrentA;
	@Column(name="hr31_current_a")
	private Double hr31CurrentA;

	@Column(name="hr02_current_b")
	private Double hr02CurrentB;
	@Column(name="hr03_current_b")
	private Double hr03CurrentB;
	@Column(name="hr04_current_b")
	private Double hr04CurrentB;
	@Column(name="hr05_current_b")
	private Double hr05CurrentB;
	@Column(name="hr06_current_b")
	private Double hr06CurrentB;
	@Column(name="hr07_current_b")
	private Double hr07CurrentB;
	@Column(name="hr08_current_b")
	private Double hr08CurrentB;
	@Column(name="hr09_current_b")
	private Double hr09CurrentB;
	@Column(name="hr10_current_b")
	private Double hr10CurrentB;
	@Column(name="hr11_current_b")
	private Double hr11CurrentB;
	@Column(name="hr12_current_b")
	private Double hr12CurrentB;
	@Column(name="hr13_current_b")
	private Double hr13CurrentB;
	@Column(name="hr14_current_b")
	private Double hr14CurrentB;
	@Column(name="hr15_current_b")
	private Double hr15CurrentB;
	@Column(name="hr16_current_b")
	private Double hr16CurrentB;
	@Column(name="hr17_current_b")
	private Double hr17CurrentB;
	@Column(name="hr18_current_b")
	private Double hr18CurrentB;
	@Column(name="hr19_current_b")
	private Double hr19CurrentB;
	@Column(name="hr20_current_b")
	private Double hr20CurrentB;
	@Column(name="hr21_current_b")
	private Double hr21CurrentB;
	@Column(name="hr22_current_b")
	private Double hr22CurrentB;
	@Column(name="hr23_current_b")
	private Double hr23CurrentB;
	@Column(name="hr24_current_b")
	private Double hr24CurrentB;
	@Column(name="hr25_current_b")
	private Double hr25CurrentB;
	@Column(name="hr26_current_b")
	private Double hr26CurrentB;
	@Column(name="hr27_current_b")
	private Double hr27CurrentB;
	@Column(name="hr28_current_b")
	private Double hr28CurrentB;
	@Column(name="hr29_current_b")
	private Double hr29CurrentB;
	@Column(name="hr30_current_b")
	private Double hr30CurrentB;
	@Column(name="hr31_current_b")
	private Double hr31CurrentB;

	@Column(name="hr02_current_c")
	private Double hr02CurrentC;
	@Column(name="hr03_current_c")
	private Double hr03CurrentC;
	@Column(name="hr04_current_c")
	private Double hr04CurrentC;
	@Column(name="hr05_current_c")
	private Double hr05CurrentC;
	@Column(name="hr06_current_c")
	private Double hr06CurrentC;
	@Column(name="hr07_current_c")
	private Double hr07CurrentC;
	@Column(name="hr08_current_c")
	private Double hr08CurrentC;
	@Column(name="hr09_current_c")
	private Double hr09CurrentC;
	@Column(name="hr10_current_c")
	private Double hr10CurrentC;
	@Column(name="hr11_current_c")
	private Double hr11CurrentC;
	@Column(name="hr12_current_c")
	private Double hr12CurrentC;
	@Column(name="hr13_current_c")
	private Double hr13CurrentC;
	@Column(name="hr14_current_c")
	private Double hr14CurrentC;
	@Column(name="hr15_current_c")
	private Double hr15CurrentC;
	@Column(name="hr16_current_c")
	private Double hr16CurrentC;
	@Column(name="hr17_current_c")
	private Double hr17CurrentC;
	@Column(name="hr18_current_c")
	private Double hr18CurrentC;
	@Column(name="hr19_current_c")
	private Double hr19CurrentC;
	@Column(name="hr20_current_c")
	private Double hr20CurrentC;
	@Column(name="hr21_current_c")
	private Double hr21CurrentC;
	@Column(name="hr22_current_c")
	private Double hr22CurrentC;
	@Column(name="hr23_current_c")
	private Double hr23CurrentC;
	@Column(name="hr24_current_c")
	private Double hr24CurrentC;
	@Column(name="hr25_current_c")
	private Double hr25CurrentC;
	@Column(name="hr26_current_c")
	private Double hr26CurrentC;
	@Column(name="hr27_current_c")
	private Double hr27CurrentC;
	@Column(name="hr28_current_c")
	private Double hr28CurrentC;
	@Column(name="hr29_current_c")
	private Double hr29CurrentC;
	@Column(name="hr30_current_c")
	private Double hr30CurrentC;
	@Column(name="hr31_current_c")
	private Double hr31CurrentC;

	//bi-directional many-to-one association to ReceiptCircuit
	@ManyToOne
	@JoinColumn(name="circuit_id")
	private ReceiptCircuit receiptCircuit;

	public DataHarmonic() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getDistortionCurrent() {
		return this.distortionCurrent;
	}

	public void setDistortionCurrent(Double distortionCurrent) {
		this.distortionCurrent = distortionCurrent;
	}

	public Double getDistortionCurrentA() {
		return this.distortionCurrentA;
	}

	public void setDistortionCurrentA(Double distortionCurrentA) {
		this.distortionCurrentA = distortionCurrentA;
	}

	public Double getDistortionCurrentB() {
		return this.distortionCurrentB;
	}

	public void setDistortionCurrentB(Double distortionCurrentB) {
		this.distortionCurrentB = distortionCurrentB;
	}

	public Double getDistortionCurrentC() {
		return this.distortionCurrentC;
	}

	public void setDistortionCurrentC(Double distortionCurrentC) {
		this.distortionCurrentC = distortionCurrentC;
	}

	public Double getDistortionVoltage() {
		return this.distortionVoltage;
	}

	public void setDistortionVoltage(Double distortionVoltage) {
		this.distortionVoltage = distortionVoltage;
	}

	public Double getDistortionVoltageA() {
		return this.distortionVoltageA;
	}

	public void setDistortionVoltageA(Double distortionVoltageA) {
		this.distortionVoltageA = distortionVoltageA;
	}

	public Double getDistortionVoltageB() {
		return this.distortionVoltageB;
	}

	public void setDistortionVoltageB(Double distortionVoltageB) {
		this.distortionVoltageB = distortionVoltageB;
	}

	public Double getDistortionVoltageC() {
		return this.distortionVoltageC;
	}

	public void setDistortionVoltageC(Double distortionVoltageC) {
		this.distortionVoltageC = distortionVoltageC;
	}

	public Double getEvenDistortionCurrentA() {
		return this.evenDistortionCurrentA;
	}

	public void setEvenDistortionCurrentA(Double evenDistortionCurrentA) {
		this.evenDistortionCurrentA = evenDistortionCurrentA;
	}

	public Double getEvenDistortionCurrentB() {
		return this.evenDistortionCurrentB;
	}

	public void setEvenDistortionCurrentB(Double evenDistortionCurrentB) {
		this.evenDistortionCurrentB = evenDistortionCurrentB;
	}

	public Double getEvenDistortionCurrentC() {
		return this.evenDistortionCurrentC;
	}

	public void setEvenDistortionCurrentC(Double evenDistortionCurrentC) {
		this.evenDistortionCurrentC = evenDistortionCurrentC;
	}

	public Double getEvenDistortionVoltageA() {
		return this.evenDistortionVoltageA;
	}

	public void setEvenDistortionVoltageA(Double evenDistortionVoltageA) {
		this.evenDistortionVoltageA = evenDistortionVoltageA;
	}

	public Double getEvenDistortionVoltageB() {
		return this.evenDistortionVoltageB;
	}

	public void setEvenDistortionVoltageB(Double evenDistortionVoltageB) {
		this.evenDistortionVoltageB = evenDistortionVoltageB;
	}

	public Double getEvenDistortionVoltageC() {
		return this.evenDistortionVoltageC;
	}

	public void setEvenDistortionVoltageC(Double evenDistortionVoltageC) {
		this.evenDistortionVoltageC = evenDistortionVoltageC;
	}

	public Double getOddDistortionCurrentA() {
		return this.oddDistortionCurrentA;
	}

	public void setOddDistortionCurrentA(Double oddDistortionCurrentA) {
		this.oddDistortionCurrentA = oddDistortionCurrentA;
	}

	public Double getOddDistortionCurrentB() {
		return this.oddDistortionCurrentB;
	}

	public void setOddDistortionCurrentB(Double oddDistortionCurrentB) {
		this.oddDistortionCurrentB = oddDistortionCurrentB;
	}

	public Double getOddDistortionCurrentC() {
		return this.oddDistortionCurrentC;
	}

	public void setOddDistortionCurrentC(Double oddDistortionCurrentC) {
		this.oddDistortionCurrentC = oddDistortionCurrentC;
	}

	public Double getOddDistortionVoltageA() {
		return this.oddDistortionVoltageA;
	}

	public void setOddDistortionVoltageA(Double oddDistortionVoltageA) {
		this.oddDistortionVoltageA = oddDistortionVoltageA;
	}

	public Double getOddDistortionVoltageB() {
		return this.oddDistortionVoltageB;
	}

	public void setOddDistortionVoltageB(Double oddDistortionVoltageB) {
		this.oddDistortionVoltageB = oddDistortionVoltageB;
	}

	public Double getOddDistortionVoltageC() {
		return this.oddDistortionVoltageC;
	}

	public void setOddDistortionVoltageC(Double oddDistortionVoltageC) {
		this.oddDistortionVoltageC = oddDistortionVoltageC;
	}

	public Date getReadTime() {
		return this.readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public ReceiptCircuit getReceiptCircuit() {
		return this.receiptCircuit;
	}

	public void setReceiptCircuit(ReceiptCircuit receiptCircuit) {
		this.receiptCircuit = receiptCircuit;
	}

	public Double getHr02VoltageA() {
		return hr02VoltageA;
	}

	public void setHr02VoltageA(Double hr02VoltageA) {
		this.hr02VoltageA = hr02VoltageA;
	}

	public Double getHr03VoltageA() {
		return hr03VoltageA;
	}

	public void setHr03VoltageA(Double hr03VoltageA) {
		this.hr03VoltageA = hr03VoltageA;
	}

	public Double getHr04VoltageA() {
		return hr04VoltageA;
	}

	public void setHr04VoltageA(Double hr04VoltageA) {
		this.hr04VoltageA = hr04VoltageA;
	}

	public Double getHr05VoltageA() {
		return hr05VoltageA;
	}

	public void setHr05VoltageA(Double hr05VoltageA) {
		this.hr05VoltageA = hr05VoltageA;
	}

	public Double getHr06VoltageA() {
		return hr06VoltageA;
	}

	public void setHr06VoltageA(Double hr06VoltageA) {
		this.hr06VoltageA = hr06VoltageA;
	}

	public Double getHr07VoltageA() {
		return hr07VoltageA;
	}

	public void setHr07VoltageA(Double hr07VoltageA) {
		this.hr07VoltageA = hr07VoltageA;
	}

	public Double getHr08VoltageA() {
		return hr08VoltageA;
	}

	public void setHr08VoltageA(Double hr08VoltageA) {
		this.hr08VoltageA = hr08VoltageA;
	}

	public Double getHr09VoltageA() {
		return hr09VoltageA;
	}

	public void setHr09VoltageA(Double hr09VoltageA) {
		this.hr09VoltageA = hr09VoltageA;
	}

	public Double getHr10VoltageA() {
		return hr10VoltageA;
	}

	public void setHr10VoltageA(Double hr10VoltageA) {
		this.hr10VoltageA = hr10VoltageA;
	}

	public Double getHr11VoltageA() {
		return hr11VoltageA;
	}

	public void setHr11VoltageA(Double hr11VoltageA) {
		this.hr11VoltageA = hr11VoltageA;
	}

	public Double getHr12VoltageA() {
		return hr12VoltageA;
	}

	public void setHr12VoltageA(Double hr12VoltageA) {
		this.hr12VoltageA = hr12VoltageA;
	}

	public Double getHr13VoltageA() {
		return hr13VoltageA;
	}

	public void setHr13VoltageA(Double hr13VoltageA) {
		this.hr13VoltageA = hr13VoltageA;
	}

	public Double getHr14VoltageA() {
		return hr14VoltageA;
	}

	public void setHr14VoltageA(Double hr14VoltageA) {
		this.hr14VoltageA = hr14VoltageA;
	}

	public Double getHr15VoltageA() {
		return hr15VoltageA;
	}

	public void setHr15VoltageA(Double hr15VoltageA) {
		this.hr15VoltageA = hr15VoltageA;
	}

	public Double getHr16VoltageA() {
		return hr16VoltageA;
	}

	public void setHr16VoltageA(Double hr16VoltageA) {
		this.hr16VoltageA = hr16VoltageA;
	}

	public Double getHr17VoltageA() {
		return hr17VoltageA;
	}

	public void setHr17VoltageA(Double hr17VoltageA) {
		this.hr17VoltageA = hr17VoltageA;
	}

	public Double getHr18VoltageA() {
		return hr18VoltageA;
	}

	public void setHr18VoltageA(Double hr18VoltageA) {
		this.hr18VoltageA = hr18VoltageA;
	}

	public Double getHr19VoltageA() {
		return hr19VoltageA;
	}

	public void setHr19VoltageA(Double hr19VoltageA) {
		this.hr19VoltageA = hr19VoltageA;
	}

	public Double getHr20VoltageA() {
		return hr20VoltageA;
	}

	public void setHr20VoltageA(Double hr20VoltageA) {
		this.hr20VoltageA = hr20VoltageA;
	}

	public Double getHr21VoltageA() {
		return hr21VoltageA;
	}

	public void setHr21VoltageA(Double hr21VoltageA) {
		this.hr21VoltageA = hr21VoltageA;
	}

	public Double getHr22VoltageA() {
		return hr22VoltageA;
	}

	public void setHr22VoltageA(Double hr22VoltageA) {
		this.hr22VoltageA = hr22VoltageA;
	}

	public Double getHr23VoltageA() {
		return hr23VoltageA;
	}

	public void setHr23VoltageA(Double hr23VoltageA) {
		this.hr23VoltageA = hr23VoltageA;
	}

	public Double getHr24VoltageA() {
		return hr24VoltageA;
	}

	public void setHr24VoltageA(Double hr24VoltageA) {
		this.hr24VoltageA = hr24VoltageA;
	}

	public Double getHr25VoltageA() {
		return hr25VoltageA;
	}

	public void setHr25VoltageA(Double hr25VoltageA) {
		this.hr25VoltageA = hr25VoltageA;
	}

	public Double getHr26VoltageA() {
		return hr26VoltageA;
	}

	public void setHr26VoltageA(Double hr26VoltageA) {
		this.hr26VoltageA = hr26VoltageA;
	}

	public Double getHr27VoltageA() {
		return hr27VoltageA;
	}

	public void setHr27VoltageA(Double hr27VoltageA) {
		this.hr27VoltageA = hr27VoltageA;
	}

	public Double getHr28VoltageA() {
		return hr28VoltageA;
	}

	public void setHr28VoltageA(Double hr28VoltageA) {
		this.hr28VoltageA = hr28VoltageA;
	}

	public Double getHr29VoltageA() {
		return hr29VoltageA;
	}

	public void setHr29VoltageA(Double hr29VoltageA) {
		this.hr29VoltageA = hr29VoltageA;
	}

	public Double getHr30VoltageA() {
		return hr30VoltageA;
	}

	public void setHr30VoltageA(Double hr30VoltageA) {
		this.hr30VoltageA = hr30VoltageA;
	}

	public Double getHr31VoltageA() {
		return hr31VoltageA;
	}

	public void setHr31VoltageA(Double hr31VoltageA) {
		this.hr31VoltageA = hr31VoltageA;
	}

	public Double getHr02VoltageB() {
		return hr02VoltageB;
	}

	public void setHr02VoltageB(Double hr02VoltageB) {
		this.hr02VoltageB = hr02VoltageB;
	}

	public Double getHr03VoltageB() {
		return hr03VoltageB;
	}

	public void setHr03VoltageB(Double hr03VoltageB) {
		this.hr03VoltageB = hr03VoltageB;
	}

	public Double getHr04VoltageB() {
		return hr04VoltageB;
	}

	public void setHr04VoltageB(Double hr04VoltageB) {
		this.hr04VoltageB = hr04VoltageB;
	}

	public Double getHr05VoltageB() {
		return hr05VoltageB;
	}

	public void setHr05VoltageB(Double hr05VoltageB) {
		this.hr05VoltageB = hr05VoltageB;
	}

	public Double getHr06VoltageB() {
		return hr06VoltageB;
	}

	public void setHr06VoltageB(Double hr06VoltageB) {
		this.hr06VoltageB = hr06VoltageB;
	}

	public Double getHr07VoltageB() {
		return hr07VoltageB;
	}

	public void setHr07VoltageB(Double hr07VoltageB) {
		this.hr07VoltageB = hr07VoltageB;
	}

	public Double getHr08VoltageB() {
		return hr08VoltageB;
	}

	public void setHr08VoltageB(Double hr08VoltageB) {
		this.hr08VoltageB = hr08VoltageB;
	}

	public Double getHr09VoltageB() {
		return hr09VoltageB;
	}

	public void setHr09VoltageB(Double hr09VoltageB) {
		this.hr09VoltageB = hr09VoltageB;
	}

	public Double getHr10VoltageB() {
		return hr10VoltageB;
	}

	public void setHr10VoltageB(Double hr10VoltageB) {
		this.hr10VoltageB = hr10VoltageB;
	}

	public Double getHr11VoltageB() {
		return hr11VoltageB;
	}

	public void setHr11VoltageB(Double hr11VoltageB) {
		this.hr11VoltageB = hr11VoltageB;
	}

	public Double getHr12VoltageB() {
		return hr12VoltageB;
	}

	public void setHr12VoltageB(Double hr12VoltageB) {
		this.hr12VoltageB = hr12VoltageB;
	}

	public Double getHr13VoltageB() {
		return hr13VoltageB;
	}

	public void setHr13VoltageB(Double hr13VoltageB) {
		this.hr13VoltageB = hr13VoltageB;
	}

	public Double getHr14VoltageB() {
		return hr14VoltageB;
	}

	public void setHr14VoltageB(Double hr14VoltageB) {
		this.hr14VoltageB = hr14VoltageB;
	}

	public Double getHr15VoltageB() {
		return hr15VoltageB;
	}

	public void setHr15VoltageB(Double hr15VoltageB) {
		this.hr15VoltageB = hr15VoltageB;
	}

	public Double getHr16VoltageB() {
		return hr16VoltageB;
	}

	public void setHr16VoltageB(Double hr16VoltageB) {
		this.hr16VoltageB = hr16VoltageB;
	}

	public Double getHr17VoltageB() {
		return hr17VoltageB;
	}

	public void setHr17VoltageB(Double hr17VoltageB) {
		this.hr17VoltageB = hr17VoltageB;
	}

	public Double getHr18VoltageB() {
		return hr18VoltageB;
	}

	public void setHr18VoltageB(Double hr18VoltageB) {
		this.hr18VoltageB = hr18VoltageB;
	}

	public Double getHr19VoltageB() {
		return hr19VoltageB;
	}

	public void setHr19VoltageB(Double hr19VoltageB) {
		this.hr19VoltageB = hr19VoltageB;
	}

	public Double getHr20VoltageB() {
		return hr20VoltageB;
	}

	public void setHr20VoltageB(Double hr20VoltageB) {
		this.hr20VoltageB = hr20VoltageB;
	}

	public Double getHr21VoltageB() {
		return hr21VoltageB;
	}

	public void setHr21VoltageB(Double hr21VoltageB) {
		this.hr21VoltageB = hr21VoltageB;
	}

	public Double getHr22VoltageB() {
		return hr22VoltageB;
	}

	public void setHr22VoltageB(Double hr22VoltageB) {
		this.hr22VoltageB = hr22VoltageB;
	}

	public Double getHr23VoltageB() {
		return hr23VoltageB;
	}

	public void setHr23VoltageB(Double hr23VoltageB) {
		this.hr23VoltageB = hr23VoltageB;
	}

	public Double getHr24VoltageB() {
		return hr24VoltageB;
	}

	public void setHr24VoltageB(Double hr24VoltageB) {
		this.hr24VoltageB = hr24VoltageB;
	}

	public Double getHr25VoltageB() {
		return hr25VoltageB;
	}

	public void setHr25VoltageB(Double hr25VoltageB) {
		this.hr25VoltageB = hr25VoltageB;
	}

	public Double getHr26VoltageB() {
		return hr26VoltageB;
	}

	public void setHr26VoltageB(Double hr26VoltageB) {
		this.hr26VoltageB = hr26VoltageB;
	}

	public Double getHr27VoltageB() {
		return hr27VoltageB;
	}

	public void setHr27VoltageB(Double hr27VoltageB) {
		this.hr27VoltageB = hr27VoltageB;
	}

	public Double getHr28VoltageB() {
		return hr28VoltageB;
	}

	public void setHr28VoltageB(Double hr28VoltageB) {
		this.hr28VoltageB = hr28VoltageB;
	}

	public Double getHr29VoltageB() {
		return hr29VoltageB;
	}

	public void setHr29VoltageB(Double hr29VoltageB) {
		this.hr29VoltageB = hr29VoltageB;
	}

	public Double getHr30VoltageB() {
		return hr30VoltageB;
	}

	public void setHr30VoltageB(Double hr30VoltageB) {
		this.hr30VoltageB = hr30VoltageB;
	}

	public Double getHr31VoltageB() {
		return hr31VoltageB;
	}

	public void setHr31VoltageB(Double hr31VoltageB) {
		this.hr31VoltageB = hr31VoltageB;
	}

	public Double getHr02VoltageC() {
		return hr02VoltageC;
	}

	public void setHr02VoltageC(Double hr02VoltageC) {
		this.hr02VoltageC = hr02VoltageC;
	}

	public Double getHr03VoltageC() {
		return hr03VoltageC;
	}

	public void setHr03VoltageC(Double hr03VoltageC) {
		this.hr03VoltageC = hr03VoltageC;
	}

	public Double getHr04VoltageC() {
		return hr04VoltageC;
	}

	public void setHr04VoltageC(Double hr04VoltageC) {
		this.hr04VoltageC = hr04VoltageC;
	}

	public Double getHr05VoltageC() {
		return hr05VoltageC;
	}

	public void setHr05VoltageC(Double hr05VoltageC) {
		this.hr05VoltageC = hr05VoltageC;
	}

	public Double getHr06VoltageC() {
		return hr06VoltageC;
	}

	public void setHr06VoltageC(Double hr06VoltageC) {
		this.hr06VoltageC = hr06VoltageC;
	}

	public Double getHr07VoltageC() {
		return hr07VoltageC;
	}

	public void setHr07VoltageC(Double hr07VoltageC) {
		this.hr07VoltageC = hr07VoltageC;
	}

	public Double getHr08VoltageC() {
		return hr08VoltageC;
	}

	public void setHr08VoltageC(Double hr08VoltageC) {
		this.hr08VoltageC = hr08VoltageC;
	}

	public Double getHr09VoltageC() {
		return hr09VoltageC;
	}

	public void setHr09VoltageC(Double hr09VoltageC) {
		this.hr09VoltageC = hr09VoltageC;
	}

	public Double getHr10VoltageC() {
		return hr10VoltageC;
	}

	public void setHr10VoltageC(Double hr10VoltageC) {
		this.hr10VoltageC = hr10VoltageC;
	}

	public Double getHr11VoltageC() {
		return hr11VoltageC;
	}

	public void setHr11VoltageC(Double hr11VoltageC) {
		this.hr11VoltageC = hr11VoltageC;
	}

	public Double getHr12VoltageC() {
		return hr12VoltageC;
	}

	public void setHr12VoltageC(Double hr12VoltageC) {
		this.hr12VoltageC = hr12VoltageC;
	}

	public Double getHr13VoltageC() {
		return hr13VoltageC;
	}

	public void setHr13VoltageC(Double hr13VoltageC) {
		this.hr13VoltageC = hr13VoltageC;
	}

	public Double getHr14VoltageC() {
		return hr14VoltageC;
	}

	public void setHr14VoltageC(Double hr14VoltageC) {
		this.hr14VoltageC = hr14VoltageC;
	}

	public Double getHr15VoltageC() {
		return hr15VoltageC;
	}

	public void setHr15VoltageC(Double hr15VoltageC) {
		this.hr15VoltageC = hr15VoltageC;
	}

	public Double getHr16VoltageC() {
		return hr16VoltageC;
	}

	public void setHr16VoltageC(Double hr16VoltageC) {
		this.hr16VoltageC = hr16VoltageC;
	}

	public Double getHr17VoltageC() {
		return hr17VoltageC;
	}

	public void setHr17VoltageC(Double hr17VoltageC) {
		this.hr17VoltageC = hr17VoltageC;
	}

	public Double getHr18VoltageC() {
		return hr18VoltageC;
	}

	public void setHr18VoltageC(Double hr18VoltageC) {
		this.hr18VoltageC = hr18VoltageC;
	}

	public Double getHr19VoltageC() {
		return hr19VoltageC;
	}

	public void setHr19VoltageC(Double hr19VoltageC) {
		this.hr19VoltageC = hr19VoltageC;
	}

	public Double getHr20VoltageC() {
		return hr20VoltageC;
	}

	public void setHr20VoltageC(Double hr20VoltageC) {
		this.hr20VoltageC = hr20VoltageC;
	}

	public Double getHr21VoltageC() {
		return hr21VoltageC;
	}

	public void setHr21VoltageC(Double hr21VoltageC) {
		this.hr21VoltageC = hr21VoltageC;
	}

	public Double getHr22VoltageC() {
		return hr22VoltageC;
	}

	public void setHr22VoltageC(Double hr22VoltageC) {
		this.hr22VoltageC = hr22VoltageC;
	}

	public Double getHr23VoltageC() {
		return hr23VoltageC;
	}

	public void setHr23VoltageC(Double hr23VoltageC) {
		this.hr23VoltageC = hr23VoltageC;
	}

	public Double getHr24VoltageC() {
		return hr24VoltageC;
	}

	public void setHr24VoltageC(Double hr24VoltageC) {
		this.hr24VoltageC = hr24VoltageC;
	}

	public Double getHr25VoltageC() {
		return hr25VoltageC;
	}

	public void setHr25VoltageC(Double hr25VoltageC) {
		this.hr25VoltageC = hr25VoltageC;
	}

	public Double getHr26VoltageC() {
		return hr26VoltageC;
	}

	public void setHr26VoltageC(Double hr26VoltageC) {
		this.hr26VoltageC = hr26VoltageC;
	}

	public Double getHr27VoltageC() {
		return hr27VoltageC;
	}

	public void setHr27VoltageC(Double hr27VoltageC) {
		this.hr27VoltageC = hr27VoltageC;
	}

	public Double getHr28VoltageC() {
		return hr28VoltageC;
	}

	public void setHr28VoltageC(Double hr28VoltageC) {
		this.hr28VoltageC = hr28VoltageC;
	}

	public Double getHr29VoltageC() {
		return hr29VoltageC;
	}

	public void setHr29VoltageC(Double hr29VoltageC) {
		this.hr29VoltageC = hr29VoltageC;
	}

	public Double getHr30VoltageC() {
		return hr30VoltageC;
	}

	public void setHr30VoltageC(Double hr30VoltageC) {
		this.hr30VoltageC = hr30VoltageC;
	}

	public Double getHr31VoltageC() {
		return hr31VoltageC;
	}

	public void setHr31VoltageC(Double hr31VoltageC) {
		this.hr31VoltageC = hr31VoltageC;
	}

	public Double getHr02CurrentA() {
		return hr02CurrentA;
	}

	public void setHr02CurrentA(Double hr02CurrentA) {
		this.hr02CurrentA = hr02CurrentA;
	}

	public Double getHr03CurrentA() {
		return hr03CurrentA;
	}

	public void setHr03CurrentA(Double hr03CurrentA) {
		this.hr03CurrentA = hr03CurrentA;
	}

	public Double getHr04CurrentA() {
		return hr04CurrentA;
	}

	public void setHr04CurrentA(Double hr04CurrentA) {
		this.hr04CurrentA = hr04CurrentA;
	}

	public Double getHr05CurrentA() {
		return hr05CurrentA;
	}

	public void setHr05CurrentA(Double hr05CurrentA) {
		this.hr05CurrentA = hr05CurrentA;
	}

	public Double getHr06CurrentA() {
		return hr06CurrentA;
	}

	public void setHr06CurrentA(Double hr06CurrentA) {
		this.hr06CurrentA = hr06CurrentA;
	}

	public Double getHr07CurrentA() {
		return hr07CurrentA;
	}

	public void setHr07CurrentA(Double hr07CurrentA) {
		this.hr07CurrentA = hr07CurrentA;
	}

	public Double getHr08CurrentA() {
		return hr08CurrentA;
	}

	public void setHr08CurrentA(Double hr08CurrentA) {
		this.hr08CurrentA = hr08CurrentA;
	}

	public Double getHr09CurrentA() {
		return hr09CurrentA;
	}

	public void setHr09CurrentA(Double hr09CurrentA) {
		this.hr09CurrentA = hr09CurrentA;
	}

	public Double getHr10CurrentA() {
		return hr10CurrentA;
	}

	public void setHr10CurrentA(Double hr10CurrentA) {
		this.hr10CurrentA = hr10CurrentA;
	}

	public Double getHr11CurrentA() {
		return hr11CurrentA;
	}

	public void setHr11CurrentA(Double hr11CurrentA) {
		this.hr11CurrentA = hr11CurrentA;
	}

	public Double getHr12CurrentA() {
		return hr12CurrentA;
	}

	public void setHr12CurrentA(Double hr12CurrentA) {
		this.hr12CurrentA = hr12CurrentA;
	}

	public Double getHr13CurrentA() {
		return hr13CurrentA;
	}

	public void setHr13CurrentA(Double hr13CurrentA) {
		this.hr13CurrentA = hr13CurrentA;
	}

	public Double getHr14CurrentA() {
		return hr14CurrentA;
	}

	public void setHr14CurrentA(Double hr14CurrentA) {
		this.hr14CurrentA = hr14CurrentA;
	}

	public Double getHr15CurrentA() {
		return hr15CurrentA;
	}

	public void setHr15CurrentA(Double hr15CurrentA) {
		this.hr15CurrentA = hr15CurrentA;
	}

	public Double getHr16CurrentA() {
		return hr16CurrentA;
	}

	public void setHr16CurrentA(Double hr16CurrentA) {
		this.hr16CurrentA = hr16CurrentA;
	}

	public Double getHr17CurrentA() {
		return hr17CurrentA;
	}

	public void setHr17CurrentA(Double hr17CurrentA) {
		this.hr17CurrentA = hr17CurrentA;
	}

	public Double getHr18CurrentA() {
		return hr18CurrentA;
	}

	public void setHr18CurrentA(Double hr18CurrentA) {
		this.hr18CurrentA = hr18CurrentA;
	}

	public Double getHr19CurrentA() {
		return hr19CurrentA;
	}

	public void setHr19CurrentA(Double hr19CurrentA) {
		this.hr19CurrentA = hr19CurrentA;
	}

	public Double getHr20CurrentA() {
		return hr20CurrentA;
	}

	public void setHr20CurrentA(Double hr20CurrentA) {
		this.hr20CurrentA = hr20CurrentA;
	}

	public Double getHr21CurrentA() {
		return hr21CurrentA;
	}

	public void setHr21CurrentA(Double hr21CurrentA) {
		this.hr21CurrentA = hr21CurrentA;
	}

	public Double getHr22CurrentA() {
		return hr22CurrentA;
	}

	public void setHr22CurrentA(Double hr22CurrentA) {
		this.hr22CurrentA = hr22CurrentA;
	}

	public Double getHr23CurrentA() {
		return hr23CurrentA;
	}

	public void setHr23CurrentA(Double hr23CurrentA) {
		this.hr23CurrentA = hr23CurrentA;
	}

	public Double getHr24CurrentA() {
		return hr24CurrentA;
	}

	public void setHr24CurrentA(Double hr24CurrentA) {
		this.hr24CurrentA = hr24CurrentA;
	}

	public Double getHr25CurrentA() {
		return hr25CurrentA;
	}

	public void setHr25CurrentA(Double hr25CurrentA) {
		this.hr25CurrentA = hr25CurrentA;
	}

	public Double getHr26CurrentA() {
		return hr26CurrentA;
	}

	public void setHr26CurrentA(Double hr26CurrentA) {
		this.hr26CurrentA = hr26CurrentA;
	}

	public Double getHr27CurrentA() {
		return hr27CurrentA;
	}

	public void setHr27CurrentA(Double hr27CurrentA) {
		this.hr27CurrentA = hr27CurrentA;
	}

	public Double getHr28CurrentA() {
		return hr28CurrentA;
	}

	public void setHr28CurrentA(Double hr28CurrentA) {
		this.hr28CurrentA = hr28CurrentA;
	}

	public Double getHr29CurrentA() {
		return hr29CurrentA;
	}

	public void setHr29CurrentA(Double hr29CurrentA) {
		this.hr29CurrentA = hr29CurrentA;
	}

	public Double getHr30CurrentA() {
		return hr30CurrentA;
	}

	public void setHr30CurrentA(Double hr30CurrentA) {
		this.hr30CurrentA = hr30CurrentA;
	}

	public Double getHr31CurrentA() {
		return hr31CurrentA;
	}

	public void setHr31CurrentA(Double hr31CurrentA) {
		this.hr31CurrentA = hr31CurrentA;
	}

	public Double getHr02CurrentB() {
		return hr02CurrentB;
	}

	public void setHr02CurrentB(Double hr02CurrentB) {
		this.hr02CurrentB = hr02CurrentB;
	}

	public Double getHr03CurrentB() {
		return hr03CurrentB;
	}

	public void setHr03CurrentB(Double hr03CurrentB) {
		this.hr03CurrentB = hr03CurrentB;
	}

	public Double getHr04CurrentB() {
		return hr04CurrentB;
	}

	public void setHr04CurrentB(Double hr04CurrentB) {
		this.hr04CurrentB = hr04CurrentB;
	}

	public Double getHr05CurrentB() {
		return hr05CurrentB;
	}

	public void setHr05CurrentB(Double hr05CurrentB) {
		this.hr05CurrentB = hr05CurrentB;
	}

	public Double getHr06CurrentB() {
		return hr06CurrentB;
	}

	public void setHr06CurrentB(Double hr06CurrentB) {
		this.hr06CurrentB = hr06CurrentB;
	}

	public Double getHr07CurrentB() {
		return hr07CurrentB;
	}

	public void setHr07CurrentB(Double hr07CurrentB) {
		this.hr07CurrentB = hr07CurrentB;
	}

	public Double getHr08CurrentB() {
		return hr08CurrentB;
	}

	public void setHr08CurrentB(Double hr08CurrentB) {
		this.hr08CurrentB = hr08CurrentB;
	}

	public Double getHr09CurrentB() {
		return hr09CurrentB;
	}

	public void setHr09CurrentB(Double hr09CurrentB) {
		this.hr09CurrentB = hr09CurrentB;
	}

	public Double getHr10CurrentB() {
		return hr10CurrentB;
	}

	public void setHr10CurrentB(Double hr10CurrentB) {
		this.hr10CurrentB = hr10CurrentB;
	}

	public Double getHr11CurrentB() {
		return hr11CurrentB;
	}

	public void setHr11CurrentB(Double hr11CurrentB) {
		this.hr11CurrentB = hr11CurrentB;
	}

	public Double getHr12CurrentB() {
		return hr12CurrentB;
	}

	public void setHr12CurrentB(Double hr12CurrentB) {
		this.hr12CurrentB = hr12CurrentB;
	}

	public Double getHr13CurrentB() {
		return hr13CurrentB;
	}

	public void setHr13CurrentB(Double hr13CurrentB) {
		this.hr13CurrentB = hr13CurrentB;
	}

	public Double getHr14CurrentB() {
		return hr14CurrentB;
	}

	public void setHr14CurrentB(Double hr14CurrentB) {
		this.hr14CurrentB = hr14CurrentB;
	}

	public Double getHr15CurrentB() {
		return hr15CurrentB;
	}

	public void setHr15CurrentB(Double hr15CurrentB) {
		this.hr15CurrentB = hr15CurrentB;
	}

	public Double getHr16CurrentB() {
		return hr16CurrentB;
	}

	public void setHr16CurrentB(Double hr16CurrentB) {
		this.hr16CurrentB = hr16CurrentB;
	}

	public Double getHr17CurrentB() {
		return hr17CurrentB;
	}

	public void setHr17CurrentB(Double hr17CurrentB) {
		this.hr17CurrentB = hr17CurrentB;
	}

	public Double getHr18CurrentB() {
		return hr18CurrentB;
	}

	public void setHr18CurrentB(Double hr18CurrentB) {
		this.hr18CurrentB = hr18CurrentB;
	}

	public Double getHr19CurrentB() {
		return hr19CurrentB;
	}

	public void setHr19CurrentB(Double hr19CurrentB) {
		this.hr19CurrentB = hr19CurrentB;
	}

	public Double getHr20CurrentB() {
		return hr20CurrentB;
	}

	public void setHr20CurrentB(Double hr20CurrentB) {
		this.hr20CurrentB = hr20CurrentB;
	}

	public Double getHr21CurrentB() {
		return hr21CurrentB;
	}

	public void setHr21CurrentB(Double hr21CurrentB) {
		this.hr21CurrentB = hr21CurrentB;
	}

	public Double getHr22CurrentB() {
		return hr22CurrentB;
	}

	public void setHr22CurrentB(Double hr22CurrentB) {
		this.hr22CurrentB = hr22CurrentB;
	}

	public Double getHr23CurrentB() {
		return hr23CurrentB;
	}

	public void setHr23CurrentB(Double hr23CurrentB) {
		this.hr23CurrentB = hr23CurrentB;
	}

	public Double getHr24CurrentB() {
		return hr24CurrentB;
	}

	public void setHr24CurrentB(Double hr24CurrentB) {
		this.hr24CurrentB = hr24CurrentB;
	}

	public Double getHr25CurrentB() {
		return hr25CurrentB;
	}

	public void setHr25CurrentB(Double hr25CurrentB) {
		this.hr25CurrentB = hr25CurrentB;
	}

	public Double getHr26CurrentB() {
		return hr26CurrentB;
	}

	public void setHr26CurrentB(Double hr26CurrentB) {
		this.hr26CurrentB = hr26CurrentB;
	}

	public Double getHr27CurrentB() {
		return hr27CurrentB;
	}

	public void setHr27CurrentB(Double hr27CurrentB) {
		this.hr27CurrentB = hr27CurrentB;
	}

	public Double getHr28CurrentB() {
		return hr28CurrentB;
	}

	public void setHr28CurrentB(Double hr28CurrentB) {
		this.hr28CurrentB = hr28CurrentB;
	}

	public Double getHr29CurrentB() {
		return hr29CurrentB;
	}

	public void setHr29CurrentB(Double hr29CurrentB) {
		this.hr29CurrentB = hr29CurrentB;
	}

	public Double getHr30CurrentB() {
		return hr30CurrentB;
	}

	public void setHr30CurrentB(Double hr30CurrentB) {
		this.hr30CurrentB = hr30CurrentB;
	}

	public Double getHr31CurrentB() {
		return hr31CurrentB;
	}

	public void setHr31CurrentB(Double hr31CurrentB) {
		this.hr31CurrentB = hr31CurrentB;
	}

	public Double getHr02CurrentC() {
		return hr02CurrentC;
	}

	public void setHr02CurrentC(Double hr02CurrentC) {
		this.hr02CurrentC = hr02CurrentC;
	}

	public Double getHr03CurrentC() {
		return hr03CurrentC;
	}

	public void setHr03CurrentC(Double hr03CurrentC) {
		this.hr03CurrentC = hr03CurrentC;
	}

	public Double getHr04CurrentC() {
		return hr04CurrentC;
	}

	public void setHr04CurrentC(Double hr04CurrentC) {
		this.hr04CurrentC = hr04CurrentC;
	}

	public Double getHr05CurrentC() {
		return hr05CurrentC;
	}

	public void setHr05CurrentC(Double hr05CurrentC) {
		this.hr05CurrentC = hr05CurrentC;
	}

	public Double getHr06CurrentC() {
		return hr06CurrentC;
	}

	public void setHr06CurrentC(Double hr06CurrentC) {
		this.hr06CurrentC = hr06CurrentC;
	}

	public Double getHr07CurrentC() {
		return hr07CurrentC;
	}

	public void setHr07CurrentC(Double hr07CurrentC) {
		this.hr07CurrentC = hr07CurrentC;
	}

	public Double getHr08CurrentC() {
		return hr08CurrentC;
	}

	public void setHr08CurrentC(Double hr08CurrentC) {
		this.hr08CurrentC = hr08CurrentC;
	}

	public Double getHr09CurrentC() {
		return hr09CurrentC;
	}

	public void setHr09CurrentC(Double hr09CurrentC) {
		this.hr09CurrentC = hr09CurrentC;
	}

	public Double getHr10CurrentC() {
		return hr10CurrentC;
	}

	public void setHr10CurrentC(Double hr10CurrentC) {
		this.hr10CurrentC = hr10CurrentC;
	}

	public Double getHr11CurrentC() {
		return hr11CurrentC;
	}

	public void setHr11CurrentC(Double hr11CurrentC) {
		this.hr11CurrentC = hr11CurrentC;
	}

	public Double getHr12CurrentC() {
		return hr12CurrentC;
	}

	public void setHr12CurrentC(Double hr12CurrentC) {
		this.hr12CurrentC = hr12CurrentC;
	}

	public Double getHr13CurrentC() {
		return hr13CurrentC;
	}

	public void setHr13CurrentC(Double hr13CurrentC) {
		this.hr13CurrentC = hr13CurrentC;
	}

	public Double getHr14CurrentC() {
		return hr14CurrentC;
	}

	public void setHr14CurrentC(Double hr14CurrentC) {
		this.hr14CurrentC = hr14CurrentC;
	}

	public Double getHr15CurrentC() {
		return hr15CurrentC;
	}

	public void setHr15CurrentC(Double hr15CurrentC) {
		this.hr15CurrentC = hr15CurrentC;
	}

	public Double getHr16CurrentC() {
		return hr16CurrentC;
	}

	public void setHr16CurrentC(Double hr16CurrentC) {
		this.hr16CurrentC = hr16CurrentC;
	}

	public Double getHr17CurrentC() {
		return hr17CurrentC;
	}

	public void setHr17CurrentC(Double hr17CurrentC) {
		this.hr17CurrentC = hr17CurrentC;
	}

	public Double getHr18CurrentC() {
		return hr18CurrentC;
	}

	public void setHr18CurrentC(Double hr18CurrentC) {
		this.hr18CurrentC = hr18CurrentC;
	}

	public Double getHr19CurrentC() {
		return hr19CurrentC;
	}

	public void setHr19CurrentC(Double hr19CurrentC) {
		this.hr19CurrentC = hr19CurrentC;
	}

	public Double getHr20CurrentC() {
		return hr20CurrentC;
	}

	public void setHr20CurrentC(Double hr20CurrentC) {
		this.hr20CurrentC = hr20CurrentC;
	}

	public Double getHr21CurrentC() {
		return hr21CurrentC;
	}

	public void setHr21CurrentC(Double hr21CurrentC) {
		this.hr21CurrentC = hr21CurrentC;
	}

	public Double getHr22CurrentC() {
		return hr22CurrentC;
	}

	public void setHr22CurrentC(Double hr22CurrentC) {
		this.hr22CurrentC = hr22CurrentC;
	}

	public Double getHr23CurrentC() {
		return hr23CurrentC;
	}

	public void setHr23CurrentC(Double hr23CurrentC) {
		this.hr23CurrentC = hr23CurrentC;
	}

	public Double getHr24CurrentC() {
		return hr24CurrentC;
	}

	public void setHr24CurrentC(Double hr24CurrentC) {
		this.hr24CurrentC = hr24CurrentC;
	}

	public Double getHr25CurrentC() {
		return hr25CurrentC;
	}

	public void setHr25CurrentC(Double hr25CurrentC) {
		this.hr25CurrentC = hr25CurrentC;
	}

	public Double getHr26CurrentC() {
		return hr26CurrentC;
	}

	public void setHr26CurrentC(Double hr26CurrentC) {
		this.hr26CurrentC = hr26CurrentC;
	}

	public Double getHr27CurrentC() {
		return hr27CurrentC;
	}

	public void setHr27CurrentC(Double hr27CurrentC) {
		this.hr27CurrentC = hr27CurrentC;
	}

	public Double getHr28CurrentC() {
		return hr28CurrentC;
	}

	public void setHr28CurrentC(Double hr28CurrentC) {
		this.hr28CurrentC = hr28CurrentC;
	}

	public Double getHr29CurrentC() {
		return hr29CurrentC;
	}

	public void setHr29CurrentC(Double hr29CurrentC) {
		this.hr29CurrentC = hr29CurrentC;
	}

	public Double getHr30CurrentC() {
		return hr30CurrentC;
	}

	public void setHr30CurrentC(Double hr30CurrentC) {
		this.hr30CurrentC = hr30CurrentC;
	}

	public Double getHr31CurrentC() {
		return hr31CurrentC;
	}

	public void setHr31CurrentC(Double hr31CurrentC) {
		this.hr31CurrentC = hr31CurrentC;
	}
}
