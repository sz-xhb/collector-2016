package com.xhb.sockserv.meter;

import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

public class ElectricFilterControl extends AbstractDevice {

	private String type;

	public ElectricFilterControl(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter, String type) {
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		this.type = type;
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		writingFrames.add(makeFrame(type));
	}

	private byte[] makeFrame(String type) {
		int[] senddata = new int[13];
		byte[] senddata2 = new byte[13];
		int[] crc = new int[2];
		senddata[0] = Integer.parseInt(receiptMeter.getMeterNo());
		senddata[1] = 0x10;

		switch (type) {
		case "RESET_SWITCH_ON":
			senddata[2] = 0x00;
			senddata[3] = 0x2A;
			senddata[4] = 0x00;
			senddata[5] = 0x02;
			senddata[6] = 0x04;
			senddata[7] = 0x3F;
			senddata[8] = 0x80;
			senddata[9] = 0x00;
			senddata[10] = 0x00;
			break;
		case "RESET_SWITCH_OFF":
			senddata[2] = 0x00;
			senddata[3] = 0x2A;
			senddata[4] = 0x00;
			senddata[5] = 0x02;
			senddata[6] = 0x04;
			senddata[7] = 0x00;
			senddata[8] = 0x00;
			senddata[9] = 0x00;
			senddata[10] = 0x00;
			break;
		case "START_SWITCH_ON":
			senddata[2] = 0x00;
			senddata[3] = 0x2C;
			senddata[4] = 0x00;
			senddata[5] = 0x02;
			senddata[6] = 0x04;
			senddata[7] = 0x3F;
			senddata[8] = 0x80;
			senddata[9] = 0x00;
			senddata[10] = 0x00;
			break;
		case "START_SWITCH_OFF":
			senddata[2] = 0x00;
			senddata[3] = 0x2C;
			senddata[4] = 0x00;
			senddata[5] = 0x02;
			senddata[6] = 0x04;
			senddata[7] = 0x00;
			senddata[8] = 0x00;
			senddata[9] = 0x00;
			senddata[10] = 0x00;
			break;
		case "CONTROL_SWITCH_ON":
			senddata[2] = 0x00;
			senddata[3] = 0x2E;
			senddata[4] = 0x00;
			senddata[5] = 0x02;
			senddata[6] = 0x04;
			senddata[7] = 0x3F;
			senddata[8] = 0x80;
			senddata[9] = 0x00;
			senddata[10] = 0x00;
			break;
		case "CONTROL_SWITCH_OFF":
			senddata[2] = 0x00;
			senddata[3] = 0x2E;
			senddata[4] = 0x00;
			senddata[5] = 0x02;
			senddata[6] = 0x04;
			senddata[7] = 0x00;
			senddata[8] = 0x00;
			senddata[9] = 0x00;
			senddata[10] = 0x00;
			break;
		default:
			break;
		}

		crc = CRC.calculateCRC(senddata, 11);
		senddata[11] = crc[0];
		senddata[12] = crc[1];

		for (int i = 0; i < 13; i++)
			senddata2[i] = (byte) senddata[i];

		return senddata2;
	}

	@Override
	public boolean analyzeFrame(byte[] frame) {
		return true;
	}

	@Override
	public void handleResult() {
		//
	}

}
