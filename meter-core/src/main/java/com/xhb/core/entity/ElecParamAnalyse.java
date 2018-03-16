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
@Table(name="elec_param_analyse")
public class ElecParamAnalyse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4126855902098240513L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="packet_id")
	private PacketRegisterRange packetRegisterRange;
	
	@Column(name="loop_no")
	private Long loopNo;
	
	@Column(name="elec_param")
	private String elecParam;
	
	@Column(name="startAt")
	private String startAt;
	
	@Column(name="register_count")
	private int registerCount;
	
	@Column(name="calculate_str")
	private String calculateStr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PacketRegisterRange getPacketRegisterRange() {
		return packetRegisterRange;
	}

	public void setPacketRegisterRange(PacketRegisterRange packetRegisterRange) {
		this.packetRegisterRange = packetRegisterRange;
	}

	public Long getLoopNo() {
		return loopNo;
	}

	public void setLoopNo(Long loopNo) {
		this.loopNo = loopNo;
	}

	public String getElecParam() {
		return elecParam;
	}

	public void setElecParam(String elecParam) {
		this.elecParam = elecParam;
	}

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public int getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(int registerCount) {
		this.registerCount = registerCount;
	}

	public String getCalculateStr() {
		return calculateStr;
	}

	public void setCalculateStr(String calculateStr) {
		this.calculateStr = calculateStr;
	}
}
