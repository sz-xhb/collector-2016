package com.xhb.sockserv.dtu;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.xhb.sockserv.meter.Device;

public class DtuContext {

	private String dtuNo;

	private ChannelHandlerContext dtuCHC;

	private ChannelHandlerContext cmdCHC;

	private ConcurrentLinkedQueue<Device> prdQueue = new ConcurrentLinkedQueue<Device>();

	private ConcurrentLinkedQueue<Device> cmdQueue = new ConcurrentLinkedQueue<Device>();

	private Device currentDevice;

	private long lastTime;

	private int delay;

	public String getDtuNo() {
		return dtuNo;
	}

	public void setDtuNo(String dtuNo) {
		this.dtuNo = dtuNo;
	}

	public ChannelHandlerContext getDtuCHC() {
		return dtuCHC;
	}

	public void setDtuCHC(ChannelHandlerContext dtuCHC) {
		this.dtuCHC = dtuCHC;
	}

	public ChannelHandlerContext getCmdCHC() {
		return cmdCHC;
	}

	public void setCmdCHC(ChannelHandlerContext cmdCHC) {
		this.cmdCHC = cmdCHC;
	}

	public ConcurrentLinkedQueue<Device> getPrdQueue() {
		return prdQueue;
	}

	public void setPrdQueue(ConcurrentLinkedQueue<Device> prdQueue) {
		this.prdQueue = prdQueue;
	}

	public ConcurrentLinkedQueue<Device> getCmdQueue() {
		return cmdQueue;
	}

	public void setCmdQueue(ConcurrentLinkedQueue<Device> cmdQueue) {
		this.cmdQueue = cmdQueue;
	}

	public Device getCurrentDevice() {
		return currentDevice;
	}

	public void setCurrentDevice(Device currentDevice) {
		this.currentDevice = currentDevice;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

}
