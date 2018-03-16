package com.xhb.sockserv.dtu;

import com.xhb.sockserv.util.EnergyCollectorInteractiveStatus;

import io.netty.channel.ChannelHandlerContext;

public class EnergyCollectorContext {
	
	private ChannelHandlerContext dtuCHC;
	
	private String buildId;
	
	private String collectorNo;
	
	private EnergyCollectorInteractiveStatus status;
	
	private String sequence;
	
	public ChannelHandlerContext getDtuCHC() {
		return dtuCHC;
	}
	public void setDtuCHC(ChannelHandlerContext dtuCHC) {
		this.dtuCHC = dtuCHC;
	}
	public String getBuildId() {
		return buildId;
	}
	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}
	public String getCollectorNo() {
		return collectorNo;
	}
	public void setCollectorNo(String collectorNo) {
		this.collectorNo = collectorNo;
	}
	public EnergyCollectorInteractiveStatus getStatus() {
		return status;
	}
	public void setStatus(EnergyCollectorInteractiveStatus status) {
		this.status = status;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}
