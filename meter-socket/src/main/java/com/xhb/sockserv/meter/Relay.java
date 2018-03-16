package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataSwitch;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.core.entity.SwitchStatus;


public class Relay extends AbstractDevice {

	private int address = 1;

	private SwitchStatus[] status = new SwitchStatus[4];

	public Relay(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
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
		senddata[1] = 0x01;
		senddata[2] = 0x00;
		senddata[3] = 0x00;
		senddata[4] = 0x00;
		senddata[5] = 0x04;

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

		if (data[2] != 0x01)
			return false;

		if ((data[3] & 0x01) == 0x01) {
			System.out.println("继电器1：合");
			status[0] = SwitchStatus.ON;
		} else {
			System.out.println("继电器1：断");
			status[0] = SwitchStatus.OFF;
		}

		if ((data[3] & 0x02) == 0x02) {
			System.out.println("继电器2：合");
			status[1] = SwitchStatus.ON;
		} else {
			System.out.println("继电器2：断");
			status[1] = SwitchStatus.OFF;
		}

		if ((data[3] & 0x04) == 0x04) {
			System.out.println("继电器3：合");
			status[2] = SwitchStatus.ON;
		} else {
			System.out.println("继电器3：断");
			status[2] = SwitchStatus.OFF;
		}

		if ((data[3] & 0x08) == 0x08) {
			System.out.println("继电器4：合");
			status[3] = SwitchStatus.ON;
		} else {
			System.out.println("继电器4：断");
			status[3] = SwitchStatus.OFF;
		}

		return true;
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		for (int i = 0; i < 4 && i < receiptCircuitList.size(); i++) {
			DataSwitch dataSwitch = new DataSwitch();
			dataSwitch.setReceiptCircuit(receiptCircuitList.get(i));
			dataSwitch.setReadTime(now);
			dataSwitch.setStatus(status[i]);
			services.dataSwitchService.save(dataSwitch);
		}
	}

}
