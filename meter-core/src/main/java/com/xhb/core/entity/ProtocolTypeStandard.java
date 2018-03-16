package com.xhb.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="protocol_type_standard")
@NamedQuery(name="ProtocolTypeStandard.findAll", query="SELECT c FROM ProtocolTypeStandard c")
public class ProtocolTypeStandard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -564475986491292732L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="meter_type_code")
	private String meterTypeCode;
	
	@Column(name="meter_type_name")
	private String meterTypeName;
	
	@Column(name="protocol_type_code")
	private String protocolTypeCode;
	
	@Column(name="protocol_type_name")
	private String protocolTypeName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMeterTypeCode() {
		return meterTypeCode;
	}

	public void setMeterTypeCode(String meterTypeCode) {
		this.meterTypeCode = meterTypeCode;
	}

	public String getMeterTypeName() {
		return meterTypeName;
	}

	public void setMeterTypeName(String meterTypeName) {
		this.meterTypeName = meterTypeName;
	}

	public String getProtocolTypeCode() {
		return protocolTypeCode;
	}

	public void setProtocolTypeCode(String protocolTypeCode) {
		this.protocolTypeCode = protocolTypeCode;
	}

	public String getProtocolTypeName() {
		return protocolTypeName;
	}

	public void setProtocolTypeName(String protocolTypeName) {
		this.protocolTypeName = protocolTypeName;
	}
}
