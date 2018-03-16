package com.xhb.sockserv.meter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xhb.core.entity.MeterStatus;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.core.service.Services;
import com.xhb.sockserv.config.ApplicationConfig;
import com.xhb.sockserv.config.ApplicationContext;

public abstract class AbstractDevice implements Device {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected ApplicationConfig config = ApplicationContext.getConfig();
	protected Services services = ApplicationContext.getServices();

	protected ReceiptCollector receiptCollector;

	protected ReceiptMeter receiptMeter;

	protected ReceiptCircuit receiptCircuit;// 远程操控使用

	protected List<byte[]> readingFrames = new ArrayList<byte[]>();
	protected List<byte[]> writingFrames = new ArrayList<byte[]>();

	private static final long TIME_OUT = 60000; // 3 * 60 * 1000
	private long writingTime;
	private int retry = 0; // total = retry + 1
	private boolean success;

	private boolean ignoreResponse;

	@Override
	public void processReadingFrame(byte[] readingFrame) {
		if (!isPacketBelongToMeter(readingFrame)) {
			return;
		}
		try {
			readingFrames.add(readingFrame);
			
			if (!writingFrames.isEmpty()) {
				return;
			}
			
			for (byte[] frame : readingFrames) {
				if (!analyzeFrame(frame)) {
					return;
				}
			}
			handleResult();
			updateMeterStatus();
			success = true;
		} catch (Exception ex) {
			logger.error("unknow Exception", ex);
		} finally {
			writingTime = 0;
		}
	}

	/**
	 * Do the receive packet belong to current Meter ?
	 * 
	 * @param the
	 *            receive packet
	 * @return Yes retrun true,No return false
	 */
	private boolean isPacketBelongToMeter(byte[] msg) {
		try {
			long meterNo = 0;
			if (receiptMeter.getProtocolType().equals("QIANBAOTONG_WATER")
					|| receiptMeter.getProtocolType().equals("CJ188_WATER")) {
				meterNo = getWaterNo(msg);
			} else{
				meterNo = doGetMeterNo(msg);
			}
			if (meterNo == Long.parseLong(receiptMeter.getMeterNo())) {
				return true;
			}
		} catch (Exception ex) {
			logger.error("Exception in isPacketBelongToMeter：", ex);
		}
		return false;
	}

	private long getWaterNo(byte[] msg) {
		byte[] data2 = new byte[msg.length - 9];
		for (int i = 0; i < data2.length; i++) {
			data2[i] = msg[i + 8];
		}

		int counter = 0;
		// 鍒ゆ柇 寮�澶磃e鐨� 瀛楃鍏ㄩ儴鍘绘帀
		for (int i = 0; i < data2.length; i++) {
			int fe = data2[i] & 0xff;
			if (fe == 0xfe) {
				counter++;
			} else { // 褰撲笉鏄痜e 鍚庣洿鎺ヨ烦杩�
				break;
			}
		}
		byte[] data = new byte[data2.length - counter];
		System.arraycopy(data2, counter, data, 0, data.length);
		String addr = "";
		for (int i = 8; i > 1; i--) {
			String temp = Integer.toHexString(data[i] & 0xff);
			if (temp.length() < 2) {
				temp = "0" + temp;
			}
			addr += temp;
		}
		return Long.parseLong(addr);
	}

	/**
	 * Get Device Address
	 * 
	 * @param the receive packet
	 *            
	 * @return meterNO
	 */
	private long doGetMeterNo(byte[] msg) throws Exception {
		int meterNo = msg[8] & 0xff;// MODBUS电表的获取电表地址的方式
		if (meterNo != 0xfe && (msg[9] & 0xff) != 0xfe && msg[8] != 0x68 && msg[15] != 0x68) {
			return meterNo;
		}else{// 其他协议的获取方式
			byte[] data = new byte[msg.length - 8];
			System.arraycopy(msg, 8, data, 0, data.length);
			
			int num = 0;
			while (num < data.length && data[num] != 0x68) {
				num++;
			}
			
			byte[] data2 = new byte[data.length - num];
			System.arraycopy(data, num, data2, 0, data2.length);
			
			if(data2[0] == 0x68 && data2[7] == 0x68)
			{
				String addr = "";
				for (int i = 6; i > 0; i--) {
					String temp = Integer.toHexString(data2[i] & 0xff);
					if (temp.length() < 2) {
						temp = "0" + temp;
					}
					addr += temp;
				}
				return Long.parseLong(addr);
			}
		}
		return meterNo;
	}

	private void updateMeterStatus() {
		if (receiptMeter == null) {
			return;
		}
		MeterStatus meterStatus = services.meterStatusService.get(receiptMeter.getId());
		if (meterStatus == null) {
			meterStatus = new MeterStatus();
			meterStatus.setMeterId(receiptMeter.getId());
			meterStatus.setReceiptMeter(receiptMeter);
			meterStatus.setActiveTime(new Date());
			services.meterStatusService.save(meterStatus);
		} else {
			meterStatus.setActiveTime(new Date());
			services.meterStatusService.update(meterStatus);
		}
	}

	@Override
	public byte[] nextWritingFrame() {
		if (System.currentTimeMillis() - writingTime < TIME_OUT) {
			return null;
		}
		if (success || ((writingFrames.isEmpty() || writingTime > 0) && retry == 0)) {
			return null;
		}
		if (writingFrames.isEmpty() || writingTime > 0) {
			retry = retry - 1;
			readingFrames.clear();
			writingFrames.clear();
			buildWritingFrames();
		}
		writingTime = System.currentTimeMillis();
		return writingFrames.remove(0);
	}

	@Override
	public boolean isComplete() {
		if (System.currentTimeMillis() - writingTime < TIME_OUT) {
			return false;
		}
		if (success || ((writingFrames.isEmpty() || writingTime > 0) && retry == 0)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isIgnoreResponse() {
		return ignoreResponse;
	}

	public void setIgnoreResponse(boolean ignoreResponse) {
		this.ignoreResponse = ignoreResponse;
	}

	public ReceiptCollector getReceiptCollector() {
		return receiptCollector;
	}

	public void setReceiptCollector(ReceiptCollector receiptCollector) {
		this.receiptCollector = receiptCollector;
	}

	public ReceiptMeter getReceiptMeter() {
		return receiptMeter;
	}

	public void setReceiptMeter(ReceiptMeter receiptMeter) {
		this.receiptMeter = receiptMeter;
	}

	public abstract void buildWritingFrames();

	public abstract boolean analyzeFrame(byte[] frame);

	public abstract void handleResult();

}
