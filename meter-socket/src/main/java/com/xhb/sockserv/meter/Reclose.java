package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataSwitch;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.core.entity.SwitchStatus;


public class Reclose extends AbstractDevice {

	private int address = 1;

	private SwitchStatus status;

	public Reclose(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
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
		senddata[2] = 0x10;
		senddata[3] = 0x02;
		senddata[4] = 0x00;
		senddata[5] = 0x01;

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

		if (data[2] != 0x02)
			return false;

		if (data[3] == 0x00 && data[4] == 0x00){
			System.out.println("执行器：合");
			status = SwitchStatus.ON;
		}
		else{
			System.out.println("执行器：断");
			status = SwitchStatus.OFF;
		}

		return true;
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		for (ReceiptCircuit receiptCircuit : receiptCircuitList) {
			DataSwitch dataSwitch = new DataSwitch();
			dataSwitch.setReceiptCircuit(receiptCircuit);
			dataSwitch.setReadTime(now);
			dataSwitch.setStatus(status);
			services.dataSwitchService.save(dataSwitch);
		}
	}

}