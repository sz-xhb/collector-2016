package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


public class Meter4_02e extends Meter4_02 {

	public Meter4_02e(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		super(receiptCollector, receiptMeter);
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		for (int i = 0; i < 12 && i < receiptCircuitList.size(); i++) {
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
			resultMutiplyRate(i);
			DataElectricity dataElectricity = new DataElectricity();
			dataElectricity.setReceiptCircuit(receiptCircuitList.get(i));
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
}
