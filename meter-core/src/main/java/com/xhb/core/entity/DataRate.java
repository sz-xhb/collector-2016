package com.xhb.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "data_rate")
public class DataRate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7979940194684698068L;

	@Id
	@OneToOne
	@JoinColumn(name="data_id")
	private DataElectricity dataElectricity;
	
	@ManyToOne
	@JoinColumn(name="circuit_id")
	private ReceiptCircuit receiptCircuit;
	
	@Column(name="read_time")
	private Date readTime;
	
	@Column(name="insert_time")
	private Date insertTime;
	
	@Column(name="kwh_total_1")
	private double kwhTotal1;
	
	@Column(name="kwh_total_2")
	private double kwhTotal2;
	
	@Column(name="kwh_total_3")
	private double kwhTotal3;
	
	@Column(name="kwh_total_4")
	private double kwhTotal4;
	
	@Column(name="kwh_1")
	private double kwh1;
	
	@Column(name="kwh_2")
	private double kwh2;
	
	@Column(name="kwh_3")
	private double kwh3;
	
	@Column(name="kwh_4")
	private double kwh4;
	
	@Column(name="kwh_rev_1")
	private double kwhRev1;
	
	@Column(name="kwh_rev_2")
	private double kwhRev2;
	
	@Column(name="kwh_rev_3")
	private double kwhRev3;
	
	@Column(name="kwh_rev_4")
	private double kwhRev4;
	
	@Column(name="kvarh1_1")
	private double kvarh11;
	
	@Column(name="kvarh1_2")
	private double kvarh12;
	
	@Column(name="kvarh1_3")
	private double kvarh13;
	
	@Column(name="kvarh1_4")
	private double kvarh14;
	
	@Column(name="kvarh2_1")
	private double kvarh21;
	
	@Column(name="kvarh2_2")
	private double kvarh22;
	
	@Column(name="kvarh2_3")
	private double kvarh23;
	
	@Column(name="kvarh2_4")
	private double kvarh24;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataRate)) {
			return false;
		}
		DataRate dr = (DataRate) obj;
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

	public ReceiptCircuit getReceiptCircuit() {
		return receiptCircuit;
	}

	public void setReceiptCircuit(ReceiptCircuit receiptCircuit) {
		this.receiptCircuit = receiptCircuit;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public double getKwhTotal1() {
		return kwhTotal1;
	}

	public void setKwhTotal1(double kwhTotal1) {
		this.kwhTotal1 = kwhTotal1;
	}

	public double getKwhTotal2() {
		return kwhTotal2;
	}

	public void setKwhTotal2(double kwhTotal2) {
		this.kwhTotal2 = kwhTotal2;
	}

	public double getKwhTotal3() {
		return kwhTotal3;
	}

	public void setKwhTotal3(double kwhTotal3) {
		this.kwhTotal3 = kwhTotal3;
	}

	public double getKwhTotal4() {
		return kwhTotal4;
	}

	public void setKwhTotal4(double kwhTotal4) {
		this.kwhTotal4 = kwhTotal4;
	}

	public double getKwh1() {
		return kwh1;
	}

	public void setKwh1(double kwh1) {
		this.kwh1 = kwh1;
	}

	public double getKwh2() {
		return kwh2;
	}

	public void setKwh2(double kwh2) {
		this.kwh2 = kwh2;
	}

	public double getKwh3() {
		return kwh3;
	}

	public void setKwh3(double kwh3) {
		this.kwh3 = kwh3;
	}

	public double getKwh4() {
		return kwh4;
	}

	public void setKwh4(double kwh4) {
		this.kwh4 = kwh4;
	}

	public double getKwhRev1() {
		return kwhRev1;
	}

	public void setKwhRev1(double kwhRev1) {
		this.kwhRev1 = kwhRev1;
	}

	public double getKwhRev2() {
		return kwhRev2;
	}

	public void setKwhRev2(double kwhRev2) {
		this.kwhRev2 = kwhRev2;
	}

	public double getKwhRev3() {
		return kwhRev3;
	}

	public void setKwhRev3(double kwhRev3) {
		this.kwhRev3 = kwhRev3;
	}

	public double getKwhRev4() {
		return kwhRev4;
	}

	public void setKwhRev4(double kwhRev4) {
		this.kwhRev4 = kwhRev4;
	}

	public double getKvarh11() {
		return kvarh11;
	}

	public void setKvarh11(double kvarh11) {
		this.kvarh11 = kvarh11;
	}

	public double getKvarh12() {
		return kvarh12;
	}

	public void setKvarh12(double kvarh12) {
		this.kvarh12 = kvarh12;
	}

	public double getKvarh13() {
		return kvarh13;
	}

	public void setKvarh13(double kvarh13) {
		this.kvarh13 = kvarh13;
	}

	public double getKvarh14() {
		return kvarh14;
	}

	public void setKvarh14(double kvarh14) {
		this.kvarh14 = kvarh14;
	}

	public double getKvarh21() {
		return kvarh21;
	}

	public void setKvarh21(double kvarh21) {
		this.kvarh21 = kvarh21;
	}

	public double getKvarh22() {
		return kvarh22;
	}

	public void setKvarh22(double kvarh22) {
		this.kvarh22 = kvarh22;
	}

	public double getKvarh23() {
		return kvarh23;
	}

	public void setKvarh23(double kvarh23) {
		this.kvarh23 = kvarh23;
	}

	public double getKvarh24() {
		return kvarh24;
	}

	public void setKvarh24(double kvarh24) {
		this.kvarh24 = kvarh24;
	}
	
}
