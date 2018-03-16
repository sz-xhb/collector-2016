package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


public class Meter4_02c extends Meter4_02 {

	public Meter4_02c(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		super(receiptCollector, receiptMeter);
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		for (int i = 0; i < 2 && i < receiptCircuitList.size(); i++) {
			if (config.isRateFromDataBaseEnabled()) {
				if (receiptCircuitList.get(i) == null) {
					return;
				}
				if (receiptCircuitList.get(i).getVoltageRatio() != null) {
					Ubb = receiptCircuitList.get(i).getVoltageRatio();
				}
				if (receiptCircuitList.get(i).getCurrentRatio() != null) {
					Ibb = receiptCircuitList.get(i).getCurrentRatio();
				}
			}
			resultMutiplyRateThreePhase(i);
			DataElectricity dataElectricity = new DataElectricity();
			dataElectricity.setReceiptCircuit(receiptCircuitList.get(i));
			dataElectricity.setReadTime(now);
			dataElectricity.setElectricityType(ElectricityType.AC_THREE);
			dataElectricity.setFrequency(frequency);
			dataElectricity.setVoltageA(voltageA);
			dataElectricity.setVoltageB(voltageB);
			dataElectricity.setVoltageC(voltageC);
			dataElectricity.setCurrentA(currentA[i]);
			dataElectricity.setCurrentB(currentB[i]);
			dataElectricity.setCurrentC(currentC[i]);
			dataElectricity.setKva(kva[i]);
			dataElectricity.setKvaA(kvaA[i]);
			dataElectricity.setKvaB(kvaB[i]);
			dataElectricity.setKvaC(kvaC[i]);
			dataElectricity.setKw(kw[i]);
			dataElectricity.setKwA(kwA[i]);
			dataElectricity.setKwB(kwB[i]);
			dataElectricity.setKwC(kwC[i]);
			dataElectricity.setKvar(kvar[i]);
			dataElectricity.setKvarA(kvarA[i]);
			dataElectricity.setKvarB(kvarB[i]);
			dataElectricity.setKvarC(kvarC[i]);
			dataElectricity.setKwh(kwh[i]);
			dataElectricity.setKwhForward(kwhForward[i]);
			dataElectricity.setKwhReverse(kwhReverse[i]);
			dataElectricity.setKvarh1(kvarh1[i]);
			dataElectricity.setKvarh2(kvarh2[i]);
			dataElectricity.setPowerFactor(powerFactor[i]);
			dataElectricity.setPowerFactorA(powerFactorA[i]);
			dataElectricity.setPowerFactorB(powerFactorB[i]);
			dataElectricity.setPowerFactorC(powerFactorC[i]);
			services.dataElectricityService.save(dataElectricity);
		}
		for (int i = 6; i < 12 && i - 4 < receiptCircuitList.size(); i++) {
			if (config.isRateFromDataBaseEnabled()) {
				if (receiptCircuitList.get(i-4) == null) {
					return;
				}
				if (receiptCircuitList.get(i-4).getVoltageRatio() != null) {
					Ubb = receiptCircuitList.get(i-4).getVoltageRatio();
				}
				if (receiptCircuitList.get(i-4).getCurrentRatio() != null) {
					Ibb = receiptCircuitList.get(i-4).getCurrentRatio();
				}
			}
			resultMutiplyRate(i);
			DataElectricity dataElectricity = new DataElectricity();
			dataElectricity.setReceiptCircuit(receiptCircuitList.get(i - 4));
			dataElectricity.setReadTime(now);
			dataElectricity.setElectricityType(ElectricityType.AC_SINGLE);
			dataElectricity.setFrequency(frequency);
			dataElectricity.setVoltage(voltageSP[i]);
			dataElectricity.setCurrent(currentSP[i]);
			dataElectricity.setKva(kvaSP[i]);
			dataElectricity.setKw(kwSP[i]);
			dataElectricity.setKvar(kvarSP[i]);
			dataElectricity.setPowerFactor(powerFactorSP[i]);
			dataElectricity.setKwh(kwhSP[i]);
			dataElectricity.setKwhForward(kwhForwardSP[i]);
			dataElectricity.setKwhReverse(kwhReverseSP[i]);
			dataElectricity.setKvarh1(kvarh1SP[i]);
			dataElectricity.setKvarh2(kvarh2SP[i]);
			services.dataElectricityService.save(dataElectricity);
		}
	}

	private void resultMutiplyRate(int index) {
		voltageSP[index] *= Ubb;
		currentSP[index] *= Ibb;
		kvaSP[index] *= Ubb * Ibb;
		kwSP[index] *= Ubb * Ibb;
		kvarSP[index] *= Ubb * Ibb;
		kwhSP[index] *= Ubb * Ibb;
		kwhForwardSP[index] *= Ubb * Ibb;
		kwhReverseSP[index] *= Ubb * Ibb;
		kvarh1SP[index] *= Ubb * Ibb;
		kvarh2SP[index] *= Ubb * Ibb;
	}

	private void resultMutiplyRateThreePhase(int index) {
		voltageA *= Ubb;
		voltageB *= Ubb;
		voltageC *= Ubb;
		currentA[index] *= Ibb;
		currentB[index] *= Ibb;
		currentC[index] *= Ibb;
		kva[index] *= Ubb * Ibb;
		kvaA[index] *= Ubb * Ibb;
		kvaB[index] *= Ubb * Ibb;
		kvaC[index] *= Ubb * Ibb;
		kvar[index] *= Ubb * Ibb;
		kvarA[index] *= Ubb * Ibb;
		kvarB[index] *= Ubb * Ibb;
		kvarC[index] *= Ubb * Ibb;
		kw[index] *= Ubb * Ibb;
		kwA[index] *= Ubb * Ibb;
		kwB[index] *= Ubb * Ibb;
		kwC[index] *= Ubb * Ibb;
		kwh[index] *= Ubb * Ibb;
		kwhForward[index] *= Ubb * Ibb;
		kwhReverse[index] *= Ubb * Ibb;
		kvarh1[index] *= Ubb * Ibb;
		kvarh2[index] *= Ubb * Ibb;
	}
}
