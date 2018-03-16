package com.xhb.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="packet_register_range")
public class PacketRegisterRange implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5930442868466065320L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="protocol_id")
	private ModbusGericProtocol modbusGericProtocol;
	
	@Column(name="startAt")
	private String startAt;
	
	@Column(name="endAt")
	private String endAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ModbusGericProtocol getModbusGericProtocol() {
		return modbusGericProtocol;
	}

	public void setModbusGericProtocol(ModbusGericProtocol modbusGericProtocol) {
		this.modbusGericProtocol = modbusGericProtocol;
	}

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}
}
