package com.xhb.core.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 * The persistent class for the receipt_collector database table.
 * 
 */
@Entity
@Table(name="receipt_collector")
@NamedQuery(name="ReceiptCollector.findAll", query="SELECT r FROM ReceiptCollector r")
public class ReceiptCollector implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1459254486764047818L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name="collector_no")
	private String collectorNo;

	@Enumerated(EnumType.STRING)
	@Column(name="collector_type")
	private CollectorType collectorType;

	private String name;

	@Column(name="collection_frequency")
	private Long period;
	
	@Column(name="phone_no")
	private String phoneNo;

	@Column(name="sim_no")
	private String simNo;
	
	@Column(name="ages")
	private Integer ages;

	@JsonManagedReference
	@OneToMany(mappedBy="receiptCollector")
	private List<ReceiptMeter> receiptMeters;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;

	@Column(name="build_id")
	private String buildInfo;
	
	public String getBuildInfo() {
		return buildInfo;
	}

	public void setBuildInfo(String buildInfo) {
		this.buildInfo = buildInfo;
	}

	public ReceiptCollector() {
	}

	public Long getPeriod() {
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	public Integer getAges() {
		return ages;
	}

	public void setAges(Integer ages) {
		this.ages = ages;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCollectorNo() {
		return this.collectorNo;
	}

	public void setCollectorNo(String collectorNo) {
		this.collectorNo = collectorNo;
	}

	public CollectorType getCollectorType() {
		return this.collectorType;
	}

	public void setCollectorType(CollectorType collectorType) {
		this.collectorType = collectorType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getSimNo() {
		return this.simNo;
	}

	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}

	public List<ReceiptMeter> getReceiptMeters() {
		return this.receiptMeters;
	}

	public void setReceiptMeters(List<ReceiptMeter> receiptMeters) {
		this.receiptMeters = receiptMeters;
	}

	public ReceiptMeter addReceiptMeter(ReceiptMeter receiptMeter) {
		getReceiptMeters().add(receiptMeter);
		receiptMeter.setReceiptCollector(this);

		return receiptMeter;
	}

	public ReceiptMeter removeReceiptMeter(ReceiptMeter receiptMeter) {
		getReceiptMeters().remove(receiptMeter);
		receiptMeter.setReceiptCollector(null);

		return receiptMeter;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
