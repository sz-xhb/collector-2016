package com.xhb.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="modbus_generic_protocol")
public class ModbusGericProtocol implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2008637419521743382L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(name="protocol")
	private String protocol;
	
	@Enumerated(EnumType.STRING)
	@Column(name="electricity_type")
	private ElectricityType electricityType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public ElectricityType getElectricityType() {
		return electricityType;
	}

	public void setElectricityType(ElectricityType electricityType) {
		this.electricityType = electricityType;
	}
}
