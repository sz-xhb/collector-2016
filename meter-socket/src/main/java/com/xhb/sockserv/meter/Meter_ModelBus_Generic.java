package com.xhb.sockserv.meter;
import java.util.List;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.DataElectricity3Phase;
import com.xhb.core.entity.ReceiptCircuit;

public class Meter_ModelBus_Generic extends AbstractDevice {

	@Override
	public void buildWritingFrames() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean analyzeFrame(byte[] frame) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleResult() {
		// TODO Auto-generated method stub

	}

	
	@SuppressWarnings("rawtypes")
	public List doGetReceiptCicuirt(){
		List<ReceiptCircuit> receiptCircuits = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
		return receiptCircuits;
	}
	
	
	public void saveDataToDataBase(DataElectricity dataElectricity,DataElectricity3Phase dataElectricity3Phase){
		services.dataElectricityService.save(dataElectricity);
		if (dataElectricity3Phase != null) {
			dataElectricity3Phase.setDataElectricity(dataElectricity);
			services.dataElectricity3PhaseService.save(dataElectricity3Phase);
		}
	}
}
