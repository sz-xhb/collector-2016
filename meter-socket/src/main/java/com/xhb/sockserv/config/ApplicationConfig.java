package com.xhb.sockserv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {

	@Value("${server.port}")
	private int serverPort;

	@Value("${meter.reading.period}")
	private long meterReadingPeriod;

	@Value("${frame.writing.period}")
	private long frameWritingPeriod;

	@Value("${frame.heart.writing.period}")
	private long frameHeartWritingPeriod;
	
	@Value("${frame.dtuPlus.writing.period}")
	private long frameDtuPlusWritingPeriod;
	
	@Value("${customer.name}")
	private String customerName;
	
	@Value("${company.name}")
	private String companyName;
	
	@Value("${rateFromDataBase.enabled}")
	private boolean rateFromDataBaseEnabled;

	@Value("${dtu.period.unified}")
	private boolean dtuPeriodUnified;
	
	@Value("${energy.collector.md5.attached.str}")
	private String energyCollectorMd5AttachedStr;
	
	public String getEnergyCollectorMd5AttachedStr() {
		return energyCollectorMd5AttachedStr;
	}

	public void setEnergyCollectorMd5AttachedStr(String energyCollectorMd5AttachedStr) {
		this.energyCollectorMd5AttachedStr = energyCollectorMd5AttachedStr;
	}

	public boolean isDtuPeriodUnified() {
		return dtuPeriodUnified;
	}

	public void setDtuPeriodUnified(boolean dtuPeriodUnified) {
		this.dtuPeriodUnified = dtuPeriodUnified;
	}

	public boolean isRateFromDataBaseEnabled() {
		return rateFromDataBaseEnabled;
	}

	public void setRateFromDataBaseEnabled(boolean rateFromDataBaseEnabled) {
		this.rateFromDataBaseEnabled = rateFromDataBaseEnabled;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getFrameHeartWritingPeriod() {
		return frameHeartWritingPeriod;
	}

	public void setFrameHeartWritingPeriod(long frameHeartWritingPeriod) {
		this.frameHeartWritingPeriod = frameHeartWritingPeriod;
	}

	public long getFrameDtuPlusWritingPeriod() {
		return frameDtuPlusWritingPeriod;
	}

	public void setFrameDtuPlusWritingPeriod(long frameDtuPlusWritingPeriod) {
		this.frameDtuPlusWritingPeriod = frameDtuPlusWritingPeriod;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public long getMeterReadingPeriod() {
		return meterReadingPeriod;
	}

	public void setMeterReadingPeriod(long meterReadingPeriod) {
		this.meterReadingPeriod = meterReadingPeriod;
	}

	public long getFrameWritingPeriod() {
		return frameWritingPeriod;
	}

	public void setFrameWritingPeriod(long frameWritingPeriod) {
		this.frameWritingPeriod = frameWritingPeriod;
	}

}
