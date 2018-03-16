package com.xhb.sockserv.meter;

import java.util.Date;
import java.util.List;

import com.xhb.core.entity.DataElecOil;
import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;


public class Meter4_02a_1 extends Meter4_02 {

	public Meter4_02a_1(ReceiptCollector receiptCollector, ReceiptMeter receiptMeter) {
		super(receiptCollector, receiptMeter);
	}

	@Override
	public void handleResult() {
		Date now = new Date();
		List<ReceiptCircuit> receiptCircuitList = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		for (int i = 0; i < 4 && i < receiptCircuitList.size(); i++) {
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
			dataElectricity.setVoltageAB(voltageAB);
			dataElectricity.setVoltageBC(voltageBC);
			dataElectricity.setVoltageCA(voltageAC);
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
			dataElectricity.setKwh(kwh[i] );
			dataElectricity.setKwhForward(kwhForward[i]);
			dataElectricity.setKwhReverse(kwhReverse[i]);
			dataElectricity.setKvarh1(kvarh1[i]);
			dataElectricity.setKvarh2(kvarh2[i]);
			dataElectricity.setPowerFactor(powerFactor[i]);
			dataElectricity.setPowerFactorA(powerFactorA[i]);
			dataElectricity.setPowerFactorB(powerFactorB[i]);
			dataElectricity.setPowerFactorC(powerFactorC[i]);
			services.dataElectricityService.save(dataElectricity);
			DataElecOil deo = new DataElecOil();
			deo.setDataId(dataElectricity.getId());
			deo.setKvarh1(oilCommonkvarh1);
			deo.setKvarh2(oilCommonkvarh2);
			deo.setKwhFor(oilCommonKwhForward);
			deo.setKwhRev(oilCommonKwhReverse);
			deo.setKwhTotal(oilCommonKwh);
			services.dataElecOilService.save(deo);
		}
	}

	private void resultMutiplyRateThreePhase(int index) {
		voltageA *= Ubb;
		voltageB *= Ubb;
		voltageC *= Ubb;
		voltageAB *= Ubb;
		voltageAC *= Ubb;
		voltageBC *= Ubb;
		oilCommonKwh *= Ubb * Ibb;
		oilCommonKwhForward *= Ubb * Ibb;
		oilCommonKwhReverse *= Ubb * Ibb;
		oilCommonkvarh1 *= Ubb * Ibb; 
		oilCommonkvarh1 *= Ubb * Ibb;
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
