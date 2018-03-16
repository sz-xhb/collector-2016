package com.xhb.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "data_electricity_3phase")
public class DataElectricity3Phase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3378817664265737922L;


	@Id
	@OneToOne
	@JoinColumn(name="data_id")
	private DataElectricity dataElectricity;
	
	@Column(name="kwh")
	private Double kwh;
	@Column(name="kwhForward")
	private Double kwhForward;
	@Column(name="kwhReverse")
	private Double kwhReverse;
	@Column(name="kvarh1")
	private Double kvarh1;
	@Column(name="kvarh2")
	private Double kvarh2;
	@Column(name="kwhA")
	private Double kwhA;
	@Column(name="kwhForwardA")
	private Double kwhForwardA;
	@Column(name="kwhReverseA")
	private Double kwhReverseA;
	@Column(name="kvarh1A")
	private Double kvarh1A;
	@Column(name="kvarh2A")
	private Double kvarh2A;
	@Column(name="kwhB")
	private Double kwhB;
	@Column(name="kwhForwardB")
	private Double kwhForwardB;
	@Column(name="kwhReverseB")
	private Double kwhReverseB;
	@Column(name="kvarh1B")
	private Double kvarh1B;
	@Column(name="kvarh2B")
	private Double kvarh2B;
	@Column(name="kwhC")
	private Double kwhC;
	@Column(name="kwhForwardC")
	private Double kwhForwardC;
	@Column(name="kwhReverseC")
	private Double kwhReverseC;
	@Column(name="kvarh1C")
	private Double kvarh1C;
	@Column(name="kvarh2C")
	private Double kvarh2C;
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataElectricity3Phase)) {
			return false;
		}
		DataElectricity3Phase dr = (DataElectricity3Phase) obj;
		if(dr.getDataElectricity().getId() == this.dataElectricity.getId()){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.getDataElectricity().getId().hashCode();
	}
	
	public DataElectricity getDataElectricity() {
		return dataElectricity;
	}
	public void setDataElectricity(DataElectricity dataElectricity) {
		this.dataElectricity = dataElectricity;
	}
	public Double getKwhForward() {
		return kwhForward;
	}
	public void setKwhForward(Double kwhForward) {
		this.kwhForward = kwhForward;
	}
	public Double getKwhReverse() {
		return kwhReverse;
	}
	public void setKwhReverse(Double kwhReverse) {
		this.kwhReverse = kwhReverse;
	}
	public Double getKvarh1() {
		return kvarh1;
	}
	public void setKvarh1(Double kvarh1) {
		this.kvarh1 = kvarh1;
	}
	public Double getKvarh2() {
		return kvarh2;
	}
	public void setKvarh2(Double kvarh2) {
		this.kvarh2 = kvarh2;
	}
	public Double getKwhA() {
		return kwhA;
	}
	public void setKwhA(Double kwhA) {
		this.kwhA = kwhA;
	}
	public Double getKwhForwardA() {
		return kwhForwardA;
	}
	public void setKwhForwardA(Double kwhForwardA) {
		this.kwhForwardA = kwhForwardA;
	}
	public Double getKwhReverseA() {
		return kwhReverseA;
	}
	public void setKwhReverseA(Double kwhReverseA) {
		this.kwhReverseA = kwhReverseA;
	}
	public Double getKvarh1A() {
		return kvarh1A;
	}
	public void setKvarh1A(Double kvarh1a) {
		kvarh1A = kvarh1a;
	}
	public Double getKvarh2A() {
		return kvarh2A;
	}
	public void setKvarh2A(Double kvarh2a) {
		kvarh2A = kvarh2a;
	}
	public Double getKwhB() {
		return kwhB;
	}
	public void setKwhB(Double kwhB) {
		this.kwhB = kwhB;
	}
	public Double getKwhForwardB() {
		return kwhForwardB;
	}
	public void setKwhForwardB(Double kwhForwardB) {
		this.kwhForwardB = kwhForwardB;
	}
	public Double getKwhReverseB() {
		return kwhReverseB;
	}
	public void setKwhReverseB(Double kwhReverseB) {
		this.kwhReverseB = kwhReverseB;
	}
	public Double getKvarh1B() {
		return kvarh1B;
	}
	public void setKvarh1B(Double kvarh1b) {
		kvarh1B = kvarh1b;
	}
	public Double getKvarh2B() {
		return kvarh2B;
	}
	public void setKvarh2B(Double kvarh2b) {
		kvarh2B = kvarh2b;
	}
	public Double getKwhC() {
		return kwhC;
	}
	public void setKwhC(Double kwhC) {
		this.kwhC = kwhC;
	}
	public Double getKwhForwardC() {
		return kwhForwardC;
	}
	public void setKwhForwardC(Double kwhForwardC) {
		this.kwhForwardC = kwhForwardC;
	}
	public Double getKwhReverseC() {
		return kwhReverseC;
	}
	public void setKwhReverseC(Double kwhReverseC) {
		this.kwhReverseC = kwhReverseC;
	}
	public Double getKvarh1C() {
		return kvarh1C;
	}
	public void setKvarh1C(Double kvarh1c) {
		kvarh1C = kvarh1c;
	}
	public Double getKvarh2C() {
		return kvarh2C;
	}
	public void setKvarh2C(Double kvarh2c) {
		kvarh2C = kvarh2c;
	}
	public Double getKwh() {
		return kwh;
	}
	public void setKwh(Double kwh) {
		this.kwh = kwh;
	}
	
}
