package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataTemperature;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;



public class Temperature extends AbstractDevice {

	private int address;
	private double[] temp = new double[4];

	public Temperature(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		address = Integer.parseInt(receiptMeter.getMeterNo());
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		writingFrames.add(makeFrame(address));
	}

	private byte[] makeFrame(int address) {
		int[] senddata = new int[8];
		byte[] senddata2 = new byte[8];
		int[] crc = new int[2];
		senddata[0] = address;
		senddata[1] = 0x03;
		senddata[2] = 0x00;
		senddata[3] = 0x0E;
		senddata[4] = 0x00;
		senddata[5] = 0x05;

		crc = CRC.calculateCRC(senddata, 6);
		senddata[6] = crc[0];
		senddata[7] = crc[1];

		for (int i = 0; i < 8; i++)
			senddata2[i] = (byte) senddata[i];

		return senddata2;
	}

	@Override
	public boolean analyzeFrame(byte[] frame) {

		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}

		// crc校验是否正确
		if (!CRC.isValid(data))
			return false;

		if (data[2] != 0x0A)
			return false;

		for (int i = 0; i < 4; i++) {
			int tag = data[2 * i + 3] & 0x80;
			if (tag == 0x00) {
				temp[i] = (data[2 * i + 3] * 256 + data[2 * i + 4]) / 100;
			} else {
				temp[i] = -((data[2 * i + 3] & 0x7F) * 256 + data[2 * i + 4]) / 100;
			}
		}

		System.out.println("温度：" + temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3]);

		return true;
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		for (int i = 0; i < 4 && i < receiptCircuitList.size(); i++) {
			DataTemperature dataTemperature = new DataTemperature();
			dataTemperature.setReceiptCircuit(receiptCircuitList.get(i));
			dataTemperature.setReadTime(now);
			dataTemperature.setTemperature(temp[i]);
			services.dataTemperatureService.save(dataTemperature);
		}
	}

}
