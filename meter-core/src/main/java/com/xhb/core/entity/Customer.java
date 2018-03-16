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
 * The persistent class for the customer database table.
 * 
 */
@Entity
@Table(name="customer")
@NamedQuery(name="Customer.findAll", query="SELECT c FROM Customer c")
public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4473357976119014778L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name="dsm_id")
	private String dsmId;

	@Column(name="dsm_test")
	private Boolean dsmTest;

	private String name;

	@JsonBackReference
	//bi-directional many-to-one association to Company
	@ManyToOne
	@JoinColumn(name="company_id")
	private Company company;

	@JsonManagedReference
	//bi-directional many-to-one association to ReceiptCollector
	@OneToMany(mappedBy="customer")
	private List<ReceiptCollector> receiptCollectors;

	public Customer() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDsmId() {
		return this.dsmId;
	}

	public void setDsmId(String dsmId) {
		this.dsmId = dsmId;
	}

	public Boolean isDsmTest() {
		return this.dsmTest;
	}

	public void setDsmTest(Boolean dsmTest) {
		this.dsmTest = dsmTest;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<ReceiptCollector> getReceiptCollectors() {
		return this.receiptCollectors;
	}

	public void setReceiptCollectors(List<ReceiptCollector> receiptCollectors) {
		this.receiptCollectors = receiptCollectors;
	}

	public ReceiptCollector addReceiptCollector(ReceiptCollector receiptCollector) {
		getReceiptCollectors().add(receiptCollector);
		receiptCollector.setCustomer(this);

		return receiptCollector;
	}

	public ReceiptCollector removeReceiptCollector(ReceiptCollector receiptCollector) {
		getReceiptCollectors().remove(receiptCollector);
		receiptCollector.setCustomer(null);

		return receiptCollector;
	}

}
