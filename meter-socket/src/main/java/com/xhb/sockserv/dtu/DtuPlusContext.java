package com.xhb.sockserv.dtu;

import com.xhb.sockserv.meter.MeterPlus;

import io.netty.channel.ChannelHandlerContext;

public class DtuPlusContext {

	private ChannelHandlerContext dtuChannel;
	private String dtuNo;
	private long packetLastTime;
	private long heartlastTime;
	private MeterPlus meterPlus;
	private boolean isRight;
	private int retry;
	
	public ChannelHandlerContext getDtuChannel() {
		return dtuChannel;
	}
	public void setDtuChannel(ChannelHandlerContext dtuChannel) {
		this.dtuChannel = dtuChannel;
	}
	public String getDtuNo() {
		return dtuNo;
	}
	public void setDtuNo(String dtuNo) {
		this.dtuNo = dtuNo;
	}
	public long getPacketLastTime() {
		return packetLastTime;
	}
	public void setPacketLastTime(long packetLastTime) {
		this.packetLastTime = packetLastTime;
	}
	public long getHeartlastTime() {
		return heartlastTime;
	}
	public void setHeartlastTime(long heartlastTime) {
		this.heartlastTime = heartlastTime;
	}
	public MeterPlus getMeterPlus() {
		return meterPlus;
	}
	public void setMeterPlus(MeterPlus meterPlus) {
		this.meterPlus = meterPlus;
	}
	public boolean isRight() {
		return isRight;
	}
	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}
	public int getRetry() {
		return retry;
	}
	public void setRetry(int retry) {
		this.retry = retry;
	}
	
}
