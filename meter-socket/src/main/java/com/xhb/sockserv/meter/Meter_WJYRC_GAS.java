package com.xhb.sockserv.meter;

import java.util.Date;

import com.xhb.core.entity.DataSteam;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

/*
 * 吴江印染厂使用的气表，非标准modbus协议
 * @author jc 改编自CS中的协议 
 * @time 2016-04-12
 */
public class Meter_WJYRC_GAS extends AbstractDevice {
	
	private double flow;

	public  Meter_WJYRC_GAS(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter){
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}
	@Override
	public void buildWritingFrames() {
		makeFrame();
	}

	private void makeFrame() {
		byte[] sendData = new byte[]{(byte) 0xcc,(byte) 0xdd,(byte) Integer.parseInt(receiptMeter.getMeterNo()),(byte) Integer.parseInt(receiptMeter.getMeterNo())};
		writingFrames.add(sendData);
	}
	@Override
	public boolean analyzeFrame(byte[] frame) {
		int[] getData = new int[frame.length-9];
		for(int i=0;i<getData.length;i++){
			getData[i] = frame[i+8] & 0xff;
		}
		int sum = 0;
		for(int i=0;i<getData.length-1;i++){
			sum +=getData[i];
		}
		sum %= 256;
		if(getData[getData.length-1] == sum){
			int index = getData.length - 4;
			flow = (getData[index] * 10000 + getData[index+1] * 100 
					+ getData[index+2]) / 10.0;
			return true;
		}
		return false;
	}

	@Override
	public void handleResult() {
		ReceiptCircuit receiptCircuit = services.receiptCircuitService.findCircuitByDtuNoAndMeterNoAndLoopNo(receiptCollector.getCollectorNo(), receiptMeter.getMeterNo(), 1);
		DataSteam dataSteam = new DataSteam();
		dataSteam.setConsumption(flow);
		dataSteam.setReadTime(new Date());
		dataSteam.setReceiptCircuit(receiptCircuit);
		services.dataSteamService.save(dataSteam);
	}

}
