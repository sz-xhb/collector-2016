package com.xhb.sockserv.meter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.DataElectricity3Phase;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;

public class Meter_MobileThreeSinglePhase extends AbstractDevice {

	private String time;
	private double frequency;
	private double[] voltageSP = new double[3];
	private double[] currentSP = new double[3];
	private double[] kwSP = new double[3];
	private double[] kvarSP = new double[3];
	private double[] kvaSP = new double[3];
	private double[] powerFactorSP = new double[3];
	private double[] kwhSP = new double[3];
	private double kwh;
	private double kwhForward;
	private double kwhReverse;
	private double kvarh1;
	private double kvarh2;
	private double kwhForwardA;
	private double kwhReverseA;
	private double kwhForwardB;
	private double kwhReverseB;
	private double kwhForwardC;
	private double kwhReverseC;
	private double kvarh1A;
	private double kvarh2A;
	private double kvarh1B;
	private double kvarh2B;
	private double kvarh1C;
	private double kvarh2C;
	private int Ubb = 1;
	private int Ibb = 1;
	
	public Meter_MobileThreeSinglePhase(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter){
		this.receiptCollector = receiptCollector;
		this.receiptMeter = receiptMeter;
		buildWritingFrames();
	}
	@Override
	public void buildWritingFrames() {
		makeFrame();
		makeFrameRate();
		makeFramePhaseEnergy();
	}

	private void makeFrameRate() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x00;
		data[3] = 0x06;
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
	private void makeFramePhaseEnergy() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x90;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x1E;

		int[] crc = CRC.calculateCRC(data, 6);
		data[6] = crc[0];
		data[7] = crc[1];

		byte[] frame = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			frame[i] = (byte) data[i];
		}
		writingFrames.add(frame);
	}
	private void makeFrame() {
		int[] data = new int[8];
		data[0] = Integer.parseInt(receiptMeter.getMeterNo());
		data[1] = 0x03;
		data[2] = 0x10;
		data[3] = 0x00;
		data[4] = 0x00;
		data[5] = 0x2C;
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
		if (readingFrames.size() != 3) {
			return false;
		}
		int[] data = new int[frame.length - 9];
		for (int i = 0; i < data.length; i++) {
			data[i] = frame[i + 8] & 0xFF;
		}

		if (!CRC.isValid(data))
			return false;

		if (data[2] == 0x04) {
			analyzeFrame1(data);
		}else if (data[2] == 0x58) {
			analyzeFrame2(data);
		}else if(data[2] == 0x3C){
			analyzeFrameSingleEnergy(data);
		}
		return true;
	}

	private void analyzeFrameSingleEnergy(int[] data) {
		kwhSP[0] = (data[3] * 256 * 256 * 256 + data[4] * 256 * 256 + data[5] * 256 + data[6]) / 100.0;
		kwhForwardA = (data[7] * 256 * 256 * 256 + data[8] * 256 * 256 + data[9] * 256 + data[10]) / 100.0;
		kwhReverseA = (data[11] * 256 * 256 * 256 + data[12] * 256 * 256 + data[13] * 256 + data[14]) / 100.0;
		kvarh1A = (data[15] * 256 * 256 * 256 + data[16] * 256 * 256 + data[17] * 256 + data[18]) / 100.0;
		kvarh2A = (data[19] * 256 * 256 * 256 + data[20] * 256 * 256 + data[21] * 256 + data[22]) / 100.0;
		kwhSP[1] = (data[23] * 256 * 256 * 256 + data[24] * 256 * 256 + data[25] * 256 + data[26]) / 100.0;
		kwhForwardB = (data[27] * 256 * 256 * 256 + data[28] * 256 * 256 + data[29] * 256 + data[30]) / 100.0;
		kwhReverseB = (data[31] * 256 * 256 * 256 + data[32] * 256 * 256 + data[33] * 256 + data[34]) / 100.0;
		kvarh1B = (data[35] * 256 * 256 * 256 + data[36] * 256 * 256 + data[37] * 256 + data[38]) / 100.0;
		kvarh2B = (data[39] * 256 * 256 * 256 + data[40] * 256 * 256 + data[41] * 256 + data[42]) / 100.0;
		kwhSP[2] = (data[43] * 256 * 256 * 256 + data[44] * 256 * 256 + data[45] * 256 + data[46]) / 100.0;
		kwhForwardC = (data[47] * 256 * 256 * 256 + data[48] * 256 * 256 + data[49] * 256 + data[50]) / 100.0;
		kwhReverseC = (data[51] * 256 * 256 * 256 + data[52] * 256 * 256 + data[53] * 256 + data[54]) / 100.0;
		kvarh1C = (data[55] * 256 * 256 * 256 + data[56] * 256 * 256 + data[57] * 256 + data[58]) / 100.0;
		kvarh2C = (data[59] * 256 * 256 * 256 + data[60] * 256 * 256 + data[61] * 256 + data[62]) / 100.0;
	}
	private void analyzeFrame2(int[] data) {
		time = "20";
		for (int i = 5; i < 10; i++) {
			if (data[i] < 0x10)
				time += "0" + Integer.toHexString(data[i]);
			else
				time += Integer.toHexString(data[i]);
		}
		frequency = (data[11] * 256 + data[12]) / 100.0;
		voltageSP[0] = (data[13] * 256 + data[14]) / 10.0;
		voltageSP[1] = (data[15] * 256 + data[16]) / 10.0;
		voltageSP[2] = (data[17] * 256 + data[18]) / 10.0;
		currentSP[0] = (data[19] * 256 + data[20]) / 100.0;
		currentSP[1] = (data[21] * 256 + data[22]) / 100.0;
		currentSP[2] = (data[23] * 256 + data[24]) / 100.0;
		kwSP[0] = (data[27] * 256 + data[28]) / 100.0;
		kwSP[1] = (data[29] * 256 + data[30]) / 100.0;
		kwSP[2] = (data[31] * 256 + data[32]) / 100.0;
		kvarSP[0] = (data[35] * 256 + data[36]) / 100.0;
		kvarSP[1] = (data[37] * 256 + data[38]) / 100.0;
		kvarSP[2] = (data[39] * 256 + data[40]) / 100.0;
		kvaSP[0] = (data[43] * 256 + data[44]) / 100.0;
		kvaSP[1] = (data[45] * 256 + data[46]) / 100.0;
		kvaSP[2] = (data[47] * 256 + data[48]) / 100.0;
		powerFactorSP[0] = (data[51] * 256 + data[52]) / 100.0;
		powerFactorSP[1] = (data[53] * 256 + data[54]) / 100.0;
		powerFactorSP[2] = (data[55] * 256 + data[56]) / 100.0;
		//kwh = (data[79] * 256 * 256 * 256 + data[80] * 256 * 256 + data[81] * 256 + data[82]) / 100.0;
		kwh = (data[59] * 256 * 256 * 256 + data[60] * 256 * 256 + data[61] * 256 + data[62]) / 100.0;
		kwhForward = (data[63] * 256 * 256 * 256 + data[64] * 256 * 256 + data[65] * 256 + data[66]) / 100.0;
		kwhReverse = (data[67] * 256 * 256 * 256 + data[68] * 256 * 256 + data[69] * 256 + data[70]) / 100.0;
		kvarh1 = (data[71] * 256 * 256 * 256 + data[72] * 256 * 256 + data[73] * 256 + data[74]) / 100.0;
		kvarh2 = (data[75] * 256 * 256 * 256 + data[76] * 256 * 256 + data[77] * 256 + data[78]) / 100.0;
		//kwhSP[1] = (data[83] * 256 * 256 * 256 + data[84] * 256 * 256 + data[85] * 256 + data[86]) / 100.0;
		//kwhSP[2] = (data[87] * 256 * 256 * 256 + data[88] * 256 * 256 + data[89] * 256 + data[90]) / 100.0;
	}
	private void analyzeFrame1(int[] data) {
		Ubb = data[3] * 256 + data[4];
		Ibb = data[5] * 256 + data[6];
	}
	@Override
	public void handleResult() {
		try {
			List<ReceiptCircuit> receiptCircuits = services.receiptCircuitService
					.queryByMeterId(receiptMeter.getId());
			for (ReceiptCircuit receiptCircuit : receiptCircuits) {
				if (config.isRateFromDataBaseEnabled()) {
					if (receiptCircuit == null) {
						return;
					}
					if (receiptCircuit.getVoltageRatio() != null) {
						Ubb = receiptCircuit.getVoltageRatio();
					}
					if (receiptCircuit.getCurrentRatio() != null) {
						Ibb = receiptCircuit.getCurrentRatio();
					}
				}
				Integer loopNo = Integer.parseInt(receiptCircuit.getCircuitNo());
				resultMutiplyRate(loopNo);
				DataElectricity dataElectricity = new DataElectricity();
				dataElectricity.setReceiptCircuit(receiptCircuit);
				dataElectricity.setCurrent(currentSP[loopNo - 1]);
				dataElectricity.setVoltage(voltageSP[loopNo - 1]);
				dataElectricity.setKw(kwSP[loopNo - 1]);
				dataElectricity.setKva(kvaSP[loopNo - 1]);
				dataElectricity.setKvar(kvarSP[loopNo - 1]);
				dataElectricity.setKwh(kwhSP[loopNo - 1]);
				//dataElectricity.setKwhForward(kwhForward);
				//dataElectricity.setKwhReverse(kwhReverse);
				dataElectricity.setKvarh1(kvarh1);
				dataElectricity.setKvarh2(kvarh2);
				dataElectricity.setElectricityType(ElectricityType.AC_SINGLE);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
				dataElectricity.setReadTime(sdf.parse(time));
				dataElectricity.setFrequency(frequency);
				dataElectricity.setPowerFactor(powerFactorSP[loopNo - 1]);
				services.dataElectricityService.save(dataElectricity);
				DataElectricity3Phase dataElectricity3Phase = new DataElectricity3Phase();
				dataElectricity3Phase.setDataElectricity(dataElectricity);
				dataElectricity3Phase.setKvarh1A(kvarh1A);
				dataElectricity3Phase.setKvarh1B(kvarh1B);
				dataElectricity3Phase.setKvarh1C(kvarh1C);
				dataElectricity3Phase.setKvarh2A(kvarh2A);
				dataElectricity3Phase.setKvarh2B(kvarh2B);
				dataElectricity3Phase.setKvarh2C(kvarh2C);
//				dataElectricity3Phase.setKwhA(kwhA);
//				dataElectricity3Phase.setKwhB(kwhB);
//				dataElectricity3Phase.setKwhC(kwhC);
				dataElectricity3Phase.setKwh(kwh);
				dataElectricity3Phase.setKwhForward(kwhForward);
				dataElectricity3Phase.setKwhReverse(kwhReverse);
				dataElectricity3Phase.setKwhForwardA(kwhForwardA);
				dataElectricity3Phase.setKwhForwardB(kwhForwardB);
				dataElectricity3Phase.setKwhForwardC(kwhForwardC);
				dataElectricity3Phase.setKwhReverseA(kwhReverseA);
				dataElectricity3Phase.setKwhReverseB(kwhReverseB);
				dataElectricity3Phase.setKwhReverseC(kwhReverseC);
				services.dataElectricity3PhaseService.save(dataElectricity3Phase);
			}
		} catch (Exception ex) {
			logger.error("save circuit of dtuNo:" + receiptCollector.getCollectorNo() + " meterNo:"
					+ receiptMeter.getMeterNo() + " error!", ex);
		}
	}
	private void resultMutiplyRate(Integer loopNo) {
		voltageSP[loopNo - 1] *= Ubb;
		currentSP[loopNo - 1] *= Ibb;
		kwSP[loopNo - 1] *= Ubb * Ibb;
		kvarSP[loopNo - 1] *= Ubb * Ibb;
		kvaSP[loopNo - 1] *= Ubb * Ibb;
		kwhSP[loopNo - 1] *= Ubb * Ibb;
		kvarh1 *= Ubb * Ibb;
		kvarh2 *= Ubb * Ibb;
		kwhReverseA *= Ubb * Ibb;
		kwhReverseB *= Ubb * Ibb;
		kwhReverseC *= Ubb * Ibb;
	}

}
