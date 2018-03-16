package com.xhb.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the collector_status database table.
 * 
 */
@Entity
@Table(name="collector_status")
@NamedQuery(name="CollectorStatus.findAll", query="SELECT c FROM CollectorStatus c")
public class CollectorStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4003832158893573876L;

	@Id
	@Column(name="collector_id")
	private Long collectorId;

	@Column(name="collector_ip")
	private String collectorIp;

	@Column(name="collector_port")
	private Integer collectorPort;

	@Column(name="server_ip")
	private String serverIp;

	@Column(name="server_port")
	private Integer serverPort;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="active_time")
	private Date activeTime;

	//bi-directional one-to-one association to ReceiptCollector
	@OneToOne
	@JoinColumn(name="collector_id")
	private ReceiptCollector receiptCollector;

	public CollectorStatus() {
	}

	public Long getCollectorId() {
		return this.collectorId;
	}

	public void setCollectorId(Long collectorId) {
		this.collectorId = collectorId;
	}

	public String getCollectorIp() {
		return this.collectorIp;
	}

	public void setCollectorIp(String collectorIp) {
		this.collectorIp = collectorIp;
	}

	public Integer getCollectorPort() {
		return collectorPort;
	}

	public void setCollectorPort(Integer collectorPort) {
		this.collectorPort = collectorPort;
	}

	public String getServerIp() {
		return this.serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public Integer getServerPort() {
		return this.serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public Date getActiveTime() {
		return this.activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public ReceiptCollector getReceiptCollector() {
		return this.receiptCollector;
	}

	public void setReceiptCollector(ReceiptCollector receiptCollector) {
		this.receiptCollector = receiptCollector;
	}

}