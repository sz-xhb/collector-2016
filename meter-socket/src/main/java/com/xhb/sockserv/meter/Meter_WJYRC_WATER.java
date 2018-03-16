package com.xhb.sockserv.meter;

import java.util.Date;

import com.xhb.core.entity.DataWater;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

/*
 * 吴江印染厂使用的水表
 * @author jc 改编自CS中的协议 
 * @time 2016-04-12
 */
public class Meter_WJYRC_WATER extends AbstractDevice {

	private double flow;

	public Meter_WJYRC_WATER(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter){
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}
	@Override
	public void buildWritingFrames() {
		makeFrame();
	}

	private void makeFrame() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x04;
		data[2] = 0x10;
		data[3] = 0x18;
		data[4] = 0x00;
		data[5] = 0x02;
		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];
		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}
	@Override
	public boolean analyzeFrame(byte[] frame) {
		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}

		if (!CRC.isValid(data))
			return false;
		
		//flow = floatIeeeConvert(data,3);
		int temp = data[3] * 256 * 256 * 256 + data[4] * 256 * 256 + data[5] * 256  + data[6];
		flow = (double) temp;
		return true;
	}

	@Override
	public void handleResult() {
		ReceiptCircuit receiptCircuit = services.receiptCircuitService.findCircuitByDtuNoAndMeterNoAndLoopNo(receiptCollector.getCollectorNo(), receiptMeter.getMeterNo(), 1);
		DataWater dataWater = new DataWater();
		dataWater.setConsumption(flow);
		dataWater.setReceiptCircuit(receiptCircuit);
		dataWater.setReadTime(new Date());
		services.dataWaterService.save(dataWater);
	}

}
