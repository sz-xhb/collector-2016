package com.xhb.sockserv.meter;

import java.util.Date;

import com.xhb.core.entity.DataWater;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

public class CJ188_WATER extends AbstractDevice {

	private int[] address = new int[7]; // 0娑撶儤娓舵妯圭秴

	// 濮樻挳鍣�
	private Double flow;

	DataWater dataWater;

	public CJ188_WATER(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		for (int i = 0; i < address.length; i++) {
			address[i] = Integer.parseInt(receiptMeter.getMeterNo().substring(2 * i, 2 * i + 2), 16);
		}
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		int[] data = new int[16];
		// 鐠у嘲顫愮敮锟�
		data[0] = 0x68;
		// 濮樼銆� 娑擄拷10
		data[1] = 0x10;
		// 鐞涖劌褰� 14娴ｏ拷
		data[2] = address[6];
		data[3] = address[5];
		data[4] = address[4];
		data[5] = address[3];
		data[6] = address[2];
		data[7] = address[1];
		data[8] = address[0];
		// 閹貉冨煑閻拷
		data[9] = 0x01;
		// 閺佺増宓侀梹鍨
		data[10] = 0x03;
		// 閺佺増宓侀弽鍥槕
		data[11] = 0x90;
		data[12] = 0x1F;
		// SER鎼村繐鍨崣锟�
		data[13] = 0x00;
		// CS 閺嶏繝鐛欓崪锟�
		data[14] = Verify.csSum(data[0] + data[1] + data[2] + data[3] + data[4] + data[5] + data[6] + data[7] + data[8]
				+ data[9] + data[10] + data[11] + data[12] + data[13]);
		// 缂佹挻娼敮锟�
		data[15] = 0x16;

		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);

	}

	@Override
	public boolean analyzeFrame(byte[] frame) {
		// 鍏堝幓鍖呭ご鍖呭熬
		byte[] data2 = new byte[frame.length - 9];
		for (int i = 0; i < data2.length; i++) {
			data2[i] = frame[i + 8];
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

		// 鏂扮殑鏁扮粍
		byte[] data3 = new byte[data2.length - counter];
		System.arraycopy(data2, counter, data3, 0, data3.length);

		int[] data = new int[data3.length];
		for (int i = 0; i < data3.length; i++) {
			data[i] = data3[i] & 0xFF;
		}

		if (data[9] != 0x81) {
			return false;
		}

		// 濮樻挳鍣�
		flow = (Integer.parseInt(Integer.toHexString(data[22])) * 100 * 100 * 100
				+ Integer.parseInt(Integer.toHexString(data[21])) * 100 * 100
				+ Integer.parseInt(Integer.toHexString(data[20])) * 100
				+ Integer.parseInt(Integer.toHexString(data[19]))) / 100.0;

		return true;
	}

	@Override
	public void handleResult() {
		ReceiptCircuit circuit = services.receiptCircuitService
				.findCircuitByDtuNoAndMeterNoAndLoopNo(receiptCollector.getCollectorNo(), receiptMeter.getMeterNo(), 1);
		dataWater = new DataWater();
		Date nowTime = new Date();
		dataWater.setReadTime(nowTime);
		dataWater.setReceiptCircuit(circuit);
		dataWater.setConsumption(flow);
		services.dataWaterService.save(dataWater);
	}

}
