package com.xhb.sockserv.meter;

import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptMeter;

public class RelayOn extends AbstractDevice {

	public RelayOn(ReceiptMeter receiptMeter, ReceiptCircuit receiptCircuit) {
		this.receiptMeter = receiptMeter;
		this.receiptCircuit = receiptCircuit;
		buildWritingFrames();
	}

	@Override
	public void buildWritingFrames() {
		writingFrames.add(makeFrame());
	}

	private byte[] makeFrame() {
		int[] senddata = new int[8];
		byte[] senddata2 = new byte[8];
		int[] crc = new int[2];
		senddata[0] = Integer.parseInt(receiptMeter.getMeterNo());
		senddata[1] = 0x05;
		senddata[2] = 0x00;
		senddata[3] = Integer.parseInt(receiptCircuit.getCircuitNo()) - 1;
		senddata[4] = 0xFF;
		senddata[5] = 0x00;

		crc = CRC.calculateCRC(senddata, 6);
		senddata[6] = crc[0];
		senddata[7] = crc[1];

		for (int i = 0; i < 8; i++)
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
