package com.xhb.core.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 * The persistent class for the receipt_meter database table.
 * 
 */
@Entity
@Table(name="receipt_meter")
@NamedQuery(name="ReceiptMeter.findAll", query="SELECT r FROM ReceiptMeter r")
public class ReceiptMeter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3955299737397585814L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name="meter_no")
	private String meterNo;

	@Column(name="meter_type")
	private String nhbMeterType;
	
	//@Enumerated(EnumType.STRING)
	@Column(name="meter_type2")
	private String meterType;

	//@Enumerated(EnumType.STRING)
	@Column(name="protocol_type")
	private String protocolType;

	@JsonManagedReference
	//bi-directional many-to-one association to ReceiptCircuit
	@OneToMany(mappedBy="receiptMeter")
	private List<ReceiptCircuit> receiptCircuits;

	@JsonBackReference
	//bi-directional many-to-one association to ReceiptCollector
	@ManyToOne
	@JoinColumn(name="collector_id")
	private ReceiptCollector receiptCollector;

	@Column(name="generic_pro_identifier")
	private String genericProIdentifier;
	
	public ReceiptMeter() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMeterNo() {
		return this.meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	public List<ReceiptCircuit> getReceiptCircuits() {
		return this.receiptCircuits;
	}

	public void setReceiptCircuits(List<ReceiptCircuit> receiptCircuits) {
		this.receiptCircuits = receiptCircuits;
	}

	public ReceiptCircuit addReceiptCircuit(ReceiptCircuit receiptCircuit) {
		getReceiptCircuits().add(receiptCircuit);
		receiptCircuit.setReceiptMeter(this);

		return receiptCircuit;
	}

	public ReceiptCircuit removeReceiptCircuit(ReceiptCircuit receiptCircuit) {
		getReceiptCircuits().remove(receiptCircuit);
		receiptCircuit.setReceiptMeter(null);
		return receiptCircuit;
	}

	public ReceiptCollector getReceiptCollector() {
		return this.receiptCollector;
	}

	public void setReceiptCollector(ReceiptCollector receiptCollector) {
		this.receiptCollector = receiptCollector;
	}

	public String getGenericProIdentifier() {
		return genericProIdentifier;
	}

	public void setGenericProIdentifier(String genericProIdentifier) {
		this.genericProIdentifier = genericProIdentifier;
	}

	public String getMeterType() {
		return meterType;
	}

	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getNhbMeterType() {
		return nhbMeterType;
	}

	public void setNhbMeterType(String nhbMeterType) {
		this.nhbMeterType = nhbMeterType;
	}
	
}
