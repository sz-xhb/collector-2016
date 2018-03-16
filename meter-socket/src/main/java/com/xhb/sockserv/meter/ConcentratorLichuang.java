package com.xhb.sockserv.meter;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.xhb.core.entity.DataElectricity;
import com.xhb.core.entity.DataHarmonic;
import com.xhb.core.entity.ElectricityType;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.core.service.Services;
import com.xhb.sockserv.config.ApplicationContext;

public class ConcentratorLichuang {

	private String xml;
	private String dtuNo;
	private Date readTime;

	protected Services services = ApplicationContext.getServices();

	private ReceiptCollector receiptCollector;

	public ConcentratorLichuang() {
	}

	public void analyzeFrame(byte[] data) throws Exception {

		xml = new String(data);
		String extractInfo = xmlExtract(xml);

		String[] element = extractInfo.split("addr:");
		if (element.length < 2)
			return;

		String[] element2 = element[0].split(" ");
		if (element2.length < 3)
			return;

		if (element2[0].contains("building_id") && element2[1].contains("gateway_id")) {
			dtuNo = element2[0].split(":")[1] + element2[1].split(":")[1];
		} else
			return;

		if (element2[2].contains("time")) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			readTime = format.parse(element2[2].split(":")[1]);
		} else
			return;

		receiptCollector = services.receiptCollectorService.getByDtuNo(dtuNo);
		if (receiptCollector == null)
			return;

		for (int i = 1; i < element.length; i++) {
			String[] element3 = element[i].split(" ");

			String meterNo = element3[0];
			ReceiptMeter receiptMeter = null;
			ReceiptCircuit receiptCircuit = null;

			List<ReceiptMeter> meters = services.ReceiptMeterService.findByCollectorId(receiptCollector.getId());
			for (ReceiptMeter meter : meters) {
				if (meterNo.equalsIgnoreCase(meter.getMeterNo())) {
					receiptMeter = meter;
					break;
				}
				if (hexStringToInt(meterNo) == Integer.parseInt(meter.getMeterNo())) {
					receiptMeter = meter;
					break;
				}
			}
			if (receiptMeter == null) {
				return;
			}

			List<ReceiptCircuit> circuits = services.receiptCircuitService.queryByMeterId(receiptMeter.getId());
			for (ReceiptCircuit circuit : circuits) {
				if (String.valueOf(Long.parseLong("1")).equals(StringUtils.stripStart(circuit.getCircuitNo(), "0"))) {
					receiptCircuit = circuit;
					break;
				}
			}
			if (receiptCircuit == null) {
				return;
			}

			if (receiptMeter.getProtocolType().toString().equals("NHB_M1V02_1")) {
				modbusV02B_process(element3, receiptCircuit);
			} else if (receiptMeter.getProtocolType().toString().equals("LCH_HARMONIC")) {
				modbus_LCH_HARMONIC_process(element3, receiptCircuit);
			}
		}
	}

	public String xmlExtract(String xml) throws Exception {

		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
		ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
		XMLStreamReader reader = factory.createXMLStreamReader(in);

		int event = reader.getEventType();

		boolean display = false;
		String output = "";

		while (true) {
			switch (event) {
			case XMLStreamConstants.START_DOCUMENT:
				break;

			case XMLStreamConstants.START_ELEMENT:
				if (reader.getName().toString().equals("building_id") || reader.getName().toString().equals("gateway_id") || reader.getName().toString().equals("time")) {
					// System.out.print(reader.getName() + ":");
					output += reader.getName() + ":";
					display = true;
				}
				for (int i = 0, n = reader.getAttributeCount(); i < n; ++i) {
					if (reader.getAttributeName(i).toString().equals("addr")) {
						// System.out.print(reader.getAttributeName(i) + ":" + reader.getAttributeValue(i) + " ");
						output += reader.getAttributeName(i) + ":" + reader.getAttributeValue(i) + " ";
					}
					if (reader.getAttributeName(i).toString().equals("idex")) {
						// System.out.print(reader.getAttributeValue(i) + ":");
						output += reader.getAttributeValue(i) + ":";
						display = true;
					}
				}
				break;

			case XMLStreamConstants.CHARACTERS:
				if (reader.isWhiteSpace())
					break;
				if (display) {
					// System.out.print(reader.getText() + " ");
					output += reader.getText() + " ";
					display = false;
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				break;

			case XMLStreamConstants.END_DOCUMENT:
				break;
			}

			if (!reader.hasNext()) {
				break;
			}
			event = reader.next();
		}

		// System.out.println("");
		System.out.println(output);
		return output;
	}

	public void modbusV02B_process(String[] element, ReceiptCircuit receiptCircuit) {

		double voltageA = 0;
		double voltageB = 0;
		double voltageC = 0;
		double currentA = 0;
		double currentB = 0;
		double currentC = 0;
		double kw = 0;
		double powerFactor = 0;
		double kwh = 0;

		for (int i = 1; i < element.length; i++) {
			if (!element[i].contains(":"))
				continue;

			String idex = element[i].split(":")[0].substring(6);
			double value = Double.parseDouble(element[i].split(":")[1]);

			if (idex.equals("01")) voltageA = value;
			if (idex.equals("02")) voltageB = value;
			if (idex.equals("03")) voltageC = value;
			if (idex.equals("04")) currentA = value;
			if (idex.equals("05")) currentB = value;
			if (idex.equals("06")) currentC = value;
			if (idex.equals("07")) kw = value;
			if (idex.equals("0A")) powerFactor = value;
			if (idex.equals("66")) kwh = value;
		}

		DataElectricity dataElectricity = new DataElectricity();
		dataElectricity.setReceiptCircuit(receiptCircuit);
		dataElectricity.setReadTime(readTime);
		dataElectricity.setElectricityType(ElectricityType.AC_THREE);
		dataElectricity.setVoltageA(voltageA);
		dataElectricity.setVoltageB(voltageB);
		dataElectricity.setVoltageC(voltageC);
		dataElectricity.setCurrentA(currentA);
		dataElectricity.setCurrentB(currentB);
		dataElectricity.setCurrentC(currentC);
		dataElectricity.setKw(kw);
		dataElectricity.setKwh(kwh);
		dataElectricity.setPowerFactor(powerFactor);
		services.dataElectricityService.save(dataElectricity);

	}

	public void modbus_LCH_HARMONIC_process(String[] element, ReceiptCircuit receiptCircuit) {

		double voltageA = 0;
		double voltageB = 0;
		double voltageC = 0;
		double voltageAB = 0;
		double voltageBC = 0;
		double voltageCA = 0;
		double currentA = 0;
		double currentB = 0;
		double currentC = 0;
		double kw = 0;
		double powerFactor = 0;
		double kwh = 0;

		double[] ratioVoltageA = new double[20];

		double[] ratioVoltageB = new double[20];

		double[] ratioVoltageC = new double[20];

		double[] ratioCurrentA = new double[20];

		double[] ratioCurrentB = new double[20];

		double[] ratioCurrentC = new double[20];

		for (int i = 1; i < element.length; i++) {
			if (!element[i].contains(":"))
				continue;

			String idex = element[i].split(":")[0].substring(6);
			double value = Double.parseDouble(element[i].split(":")[1]);

			if (idex.equals("01")) voltageA = value;
			if (idex.equals("02")) voltageB = value;
			if (idex.equals("03")) voltageC = value;
			if (idex.equals("2C")) voltageAB = value;
			if (idex.equals("2D")) voltageBC = value;
			if (idex.equals("2E")) voltageCA = value;
			if (idex.equals("04")) currentA = value;
			if (idex.equals("05")) currentB = value;
			if (idex.equals("06")) currentC = value;
			if (idex.equals("07")) kw = value;
			if (idex.equals("0A")) powerFactor = value;
			if (idex.equals("69")) kwh = value;

			if (idex.equals("6E")) ratioVoltageA[0] = value / 100.0;
			if (idex.equals("6F")) ratioVoltageA[1] = value / 100.0;
			if (idex.equals("70")) ratioVoltageA[2] = value / 100.0;
			if (idex.equals("71")) ratioVoltageA[3] = value / 100.0;
			if (idex.equals("72")) ratioVoltageA[4] = value / 100.0;
			if (idex.equals("73")) ratioVoltageA[5] = value / 100.0;
			if (idex.equals("74")) ratioVoltageA[6] = value / 100.0;
			if (idex.equals("75")) ratioVoltageA[7] = value / 100.0;
			if (idex.equals("76")) ratioVoltageA[8] = value / 100.0;
			if (idex.equals("77")) ratioVoltageA[9] = value / 100.0;
			if (idex.equals("78")) ratioVoltageA[10] = value / 100.0;
			if (idex.equals("79")) ratioVoltageA[11] = value / 100.0;
			if (idex.equals("7A")) ratioVoltageA[12] = value / 100.0;
			if (idex.equals("7B")) ratioVoltageA[13] = value / 100.0;
			if (idex.equals("7C")) ratioVoltageA[14] = value / 100.0;
			if (idex.equals("7D")) ratioVoltageA[15] = value / 100.0;
			if (idex.equals("7E")) ratioVoltageA[16] = value / 100.0;
			if (idex.equals("7F")) ratioVoltageA[17] = value / 100.0;
			if (idex.equals("80")) ratioVoltageA[18] = value / 100.0;
			if (idex.equals("81")) ratioVoltageA[19] = value / 100.0;

			if (idex.equals("82")) ratioVoltageB[0] = value / 100.0;
			if (idex.equals("83")) ratioVoltageB[1] = value / 100.0;
			if (idex.equals("84")) ratioVoltageB[2] = value / 100.0;
			if (idex.equals("85")) ratioVoltageB[3] = value / 100.0;
			if (idex.equals("86")) ratioVoltageB[4] = value / 100.0;
			if (idex.equals("87")) ratioVoltageB[5] = value / 100.0;
			if (idex.equals("88")) ratioVoltageB[6] = value / 100.0;
			if (idex.equals("89")) ratioVoltageB[7] = value / 100.0;
			if (idex.equals("8A")) ratioVoltageB[8] = value / 100.0;
			if (idex.equals("8B")) ratioVoltageB[9] = value / 100.0;
			if (idex.equals("8C")) ratioVoltageB[10] = value / 100.0;
			if (idex.equals("8D")) ratioVoltageB[11] = value / 100.0;
			if (idex.equals("8E")) ratioVoltageB[12] = value / 100.0;
			if (idex.equals("8F")) ratioVoltageB[13] = value / 100.0;
			if (idex.equals("90")) ratioVoltageB[14] = value / 100.0;
			if (idex.equals("91")) ratioVoltageB[15] = value / 100.0;
			if (idex.equals("92")) ratioVoltageB[16] = value / 100.0;
			if (idex.equals("93")) ratioVoltageB[17] = value / 100.0;
			if (idex.equals("94")) ratioVoltageB[18] = value / 100.0;
			if (idex.equals("95")) ratioVoltageB[19] = value / 100.0;

			if (idex.equals("96")) ratioVoltageC[0] = value / 100.0;
			if (idex.equals("97")) ratioVoltageC[1] = value / 100.0;
			if (idex.equals("98")) ratioVoltageC[2] = value / 100.0;
			if (idex.equals("99")) ratioVoltageC[3] = value / 100.0;
			if (idex.equals("9A")) ratioVoltageC[4] = value / 100.0;
			if (idex.equals("9B")) ratioVoltageC[5] = value / 100.0;
			if (idex.equals("9C")) ratioVoltageC[6] = value / 100.0;
			if (idex.equals("9D")) ratioVoltageC[7] = value / 100.0;
			if (idex.equals("9E")) ratioVoltageC[8] = value / 100.0;
			if (idex.equals("9F")) ratioVoltageC[9] = value / 100.0;
			if (idex.equals("A0")) ratioVoltageC[10] = value / 100.0;
			if (idex.equals("A1")) ratioVoltageC[11] = value / 100.0;
			if (idex.equals("A2")) ratioVoltageC[12] = value / 100.0;
			if (idex.equals("A3")) ratioVoltageC[13] = value / 100.0;
			if (idex.equals("A4")) ratioVoltageC[14] = value / 100.0;
			if (idex.equals("A5")) ratioVoltageC[15] = value / 100.0;
			if (idex.equals("A6")) ratioVoltageC[16] = value / 100.0;
			if (idex.equals("A7")) ratioVoltageC[17] = value / 100.0;
			if (idex.equals("A8")) ratioVoltageC[18] = value / 100.0;
			if (idex.equals("A9")) ratioVoltageC[19] = value / 100.0;

			if (idex.equals("AA")) ratioCurrentA[0] = value / 100.0;
			if (idex.equals("AB")) ratioCurrentA[1] = value / 100.0;
			if (idex.equals("AC")) ratioCurrentA[2] = value / 100.0;
			if (idex.equals("AD")) ratioCurrentA[3] = value / 100.0;
			if (idex.equals("AE")) ratioCurrentA[4] = value / 100.0;
			if (idex.equals("AF")) ratioCurrentA[5] = value / 100.0;
			if (idex.equals("B0")) ratioCurrentA[6] = value / 100.0;
			if (idex.equals("B1")) ratioCurrentA[7] = value / 100.0;
			if (idex.equals("B2")) ratioCurrentA[8] = value / 100.0;
			if (idex.equals("B3")) ratioCurrentA[9] = value / 100.0;
			if (idex.equals("B4")) ratioCurrentA[10] = value / 100.0;
			if (idex.equals("B5")) ratioCurrentA[11] = value / 100.0;
			if (idex.equals("B6")) ratioCurrentA[12] = value / 100.0;
			if (idex.equals("B7")) ratioCurrentA[13] = value / 100.0;
			if (idex.equals("B8")) ratioCurrentA[14] = value / 100.0;
			if (idex.equals("B9")) ratioCurrentA[15] = value / 100.0;
			if (idex.equals("BA")) ratioCurrentA[16] = value / 100.0;
			if (idex.equals("BB")) ratioCurrentA[17] = value / 100.0;
			if (idex.equals("BC")) ratioCurrentA[18] = value / 100.0;
			if (idex.equals("BD")) ratioCurrentA[19] = value / 100.0;

			if (idex.equals("BE")) ratioCurrentB[0] = value / 100.0;
			if (idex.equals("BF")) ratioCurrentB[1] = value / 100.0;
			if (idex.equals("C0")) ratioCurrentB[2] = value / 100.0;
			if (idex.equals("C1")) ratioCurrentB[3] = value / 100.0;
			if (idex.equals("C2")) ratioCurrentB[4] = value / 100.0;
			if (idex.equals("C3")) ratioCurrentB[5] = value / 100.0;
			if (idex.equals("C4")) ratioCurrentB[6] = value / 100.0;
			if (idex.equals("C5")) ratioCurrentB[7] = value / 100.0;
			if (idex.equals("C6")) ratioCurrentB[8] = value / 100.0;
			if (idex.equals("C7")) ratioCurrentB[9] = value / 100.0;
			if (idex.equals("C8")) ratioCurrentB[10] = value / 100.0;
			if (idex.equals("C9")) ratioCurrentB[11] = value / 100.0;
			if (idex.equals("CA")) ratioCurrentB[12] = value / 100.0;
			if (idex.equals("CB")) ratioCurrentB[13] = value / 100.0;
			if (idex.equals("CC")) ratioCurrentB[14] = value / 100.0;
			if (idex.equals("CD")) ratioCurrentB[15] = value / 100.0;
			if (idex.equals("CE")) ratioCurrentB[16] = value / 100.0;
			if (idex.equals("CF")) ratioCurrentB[17] = value / 100.0;
			if (idex.equals("D0")) ratioCurrentB[18] = value / 100.0;
			if (idex.equals("D1")) ratioCurrentB[19] = value / 100.0;

			if (idex.equals("D2")) ratioCurrentC[0] = value / 100.0;
			if (idex.equals("D3")) ratioCurrentC[1] = value / 100.0;
			if (idex.equals("D4")) ratioCurrentC[2] = value / 100.0;
			if (idex.equals("D5")) ratioCurrentC[3] = value / 100.0;
			if (idex.equals("D6")) ratioCurrentC[4] = value / 100.0;
			if (idex.equals("D7")) ratioCurrentC[5] = value / 100.0;
			if (idex.equals("D8")) ratioCurrentC[6] = value / 100.0;
			if (idex.equals("D9")) ratioCurrentC[7] = value / 100.0;
			if (idex.equals("DA")) ratioCurrentC[8] = value / 100.0;
			if (idex.equals("DB")) ratioCurrentC[9] = value / 100.0;
			if (idex.equals("DC")) ratioCurrentC[10] = value / 100.0;
			if (idex.equals("DD")) ratioCurrentC[11] = value / 100.0;
			if (idex.equals("DE")) ratioCurrentC[12] = value / 100.0;
			if (idex.equals("DF")) ratioCurrentC[13] = value / 100.0;
			if (idex.equals("E0")) ratioCurrentC[14] = value / 100.0;
			if (idex.equals("E1")) ratioCurrentC[15] = value / 100.0;
			if (idex.equals("E2")) ratioCurrentC[16] = value / 100.0;
			if (idex.equals("E3")) ratioCurrentC[17] = value / 100.0;
			if (idex.equals("E4")) ratioCurrentC[18] = value / 100.0;
			if (idex.equals("E5")) ratioCurrentC[19] = value / 100.0;

		}

		DataElectricity dataElectricity = new DataElectricity();
		dataElectricity.setReceiptCircuit(receiptCircuit);
		dataElectricity.setReadTime(readTime);
		dataElectricity.setElectricityType(ElectricityType.AC_THREE);
		dataElectricity.setVoltageA(voltageA);
		dataElectricity.setVoltageB(voltageB);
		dataElectricity.setVoltageC(voltageC);
		dataElectricity.setVoltageAB(voltageAB);
		dataElectricity.setVoltageBC(voltageBC);
		dataElectricity.setVoltageCA(voltageCA);
		dataElectricity.setCurrentA(currentA);
		dataElectricity.setCurrentB(currentB);
		dataElectricity.setCurrentC(currentC);
		dataElectricity.setKw(kw);
		dataElectricity.setPowerFactor(powerFactor);
		dataElectricity.setKwh(kwh);
		services.dataElectricityService.save(dataElectricity);

		DataHarmonic dataHarmonic = new DataHarmonic();
		dataHarmonic.setReceiptCircuit(receiptCircuit);
		dataHarmonic.setReadTime(readTime);

		dataHarmonic.setHr02VoltageA(ratioVoltageA[0]);
		dataHarmonic.setHr03VoltageA(ratioVoltageA[1]);
		dataHarmonic.setHr04VoltageA(ratioVoltageA[2]);
		dataHarmonic.setHr05VoltageA(ratioVoltageA[3]);
		dataHarmonic.setHr06VoltageA(ratioVoltageA[4]);
		dataHarmonic.setHr07VoltageA(ratioVoltageA[5]);
		dataHarmonic.setHr08VoltageA(ratioVoltageA[6]);
		dataHarmonic.setHr09VoltageA(ratioVoltageA[7]);
		dataHarmonic.setHr10VoltageA(ratioVoltageA[8]);
		dataHarmonic.setHr11VoltageA(ratioVoltageA[9]);
		dataHarmonic.setHr12VoltageA(ratioVoltageA[10]);
		dataHarmonic.setHr13VoltageA(ratioVoltageA[11]);
		dataHarmonic.setHr14VoltageA(ratioVoltageA[12]);
		dataHarmonic.setHr15VoltageA(ratioVoltageA[13]);
		dataHarmonic.setHr16VoltageA(ratioVoltageA[14]);
		dataHarmonic.setHr17VoltageA(ratioVoltageA[15]);
		dataHarmonic.setHr18VoltageA(ratioVoltageA[16]);
		dataHarmonic.setHr19VoltageA(ratioVoltageA[17]);
		dataHarmonic.setHr20VoltageA(ratioVoltageA[18]);
		dataHarmonic.setHr21VoltageA(ratioVoltageA[19]);

		dataHarmonic.setHr02VoltageB(ratioVoltageB[0]);
		dataHarmonic.setHr03VoltageB(ratioVoltageB[1]);
		dataHarmonic.setHr04VoltageB(ratioVoltageB[2]);
		dataHarmonic.setHr05VoltageB(ratioVoltageB[3]);
		dataHarmonic.setHr06VoltageB(ratioVoltageB[4]);
		dataHarmonic.setHr07VoltageB(ratioVoltageB[5]);
		dataHarmonic.setHr08VoltageB(ratioVoltageB[6]);
		dataHarmonic.setHr09VoltageB(ratioVoltageB[7]);
		dataHarmonic.setHr10VoltageB(ratioVoltageB[8]);
		dataHarmonic.setHr11VoltageB(ratioVoltageB[9]);
		dataHarmonic.setHr12VoltageB(ratioVoltageB[10]);
		dataHarmonic.setHr13VoltageB(ratioVoltageB[11]);
		dataHarmonic.setHr14VoltageB(ratioVoltageB[12]);
		dataHarmonic.setHr15VoltageB(ratioVoltageB[13]);
		dataHarmonic.setHr16VoltageB(ratioVoltageB[14]);
		dataHarmonic.setHr17VoltageB(ratioVoltageB[15]);
		dataHarmonic.setHr18VoltageB(ratioVoltageB[16]);
		dataHarmonic.setHr19VoltageB(ratioVoltageB[17]);
		dataHarmonic.setHr20VoltageB(ratioVoltageB[18]);
		dataHarmonic.setHr21VoltageB(ratioVoltageB[19]);

		dataHarmonic.setHr02VoltageC(ratioVoltageC[0]);
		dataHarmonic.setHr03VoltageC(ratioVoltageC[1]);
		dataHarmonic.setHr04VoltageC(ratioVoltageC[2]);
		dataHarmonic.setHr05VoltageC(ratioVoltageC[3]);
		dataHarmonic.setHr06VoltageC(ratioVoltageC[4]);
		dataHarmonic.setHr07VoltageC(ratioVoltageC[5]);
		dataHarmonic.setHr08VoltageC(ratioVoltageC[6]);
		dataHarmonic.setHr09VoltageC(ratioVoltageC[7]);
		dataHarmonic.setHr10VoltageC(ratioVoltageC[8]);
		dataHarmonic.setHr11VoltageC(ratioVoltageC[9]);
		dataHarmonic.setHr12VoltageC(ratioVoltageC[10]);
		dataHarmonic.setHr13VoltageC(ratioVoltageC[11]);
		dataHarmonic.setHr14VoltageC(ratioVoltageC[12]);
		dataHarmonic.setHr15VoltageC(ratioVoltageC[13]);
		dataHarmonic.setHr16VoltageC(ratioVoltageC[14]);
		dataHarmonic.setHr17VoltageC(ratioVoltageC[15]);
		dataHarmonic.setHr18VoltageC(ratioVoltageC[16]);
		dataHarmonic.setHr19VoltageC(ratioVoltageC[17]);
		dataHarmonic.setHr20VoltageC(ratioVoltageC[18]);
		dataHarmonic.setHr21VoltageC(ratioVoltageC[19]);

		dataHarmonic.setHr02CurrentA(ratioCurrentA[0]);
		dataHarmonic.setHr03CurrentA(ratioCurrentA[1]);
		dataHarmonic.setHr04CurrentA(ratioCurrentA[2]);
		dataHarmonic.setHr05CurrentA(ratioCurrentA[3]);
		dataHarmonic.setHr06CurrentA(ratioCurrentA[4]);
		dataHarmonic.setHr07CurrentA(ratioCurrentA[5]);
		dataHarmonic.setHr08CurrentA(ratioCurrentA[6]);
		dataHarmonic.setHr09CurrentA(ratioCurrentA[7]);
		dataHarmonic.setHr10CurrentA(ratioCurrentA[8]);
		dataHarmonic.setHr11CurrentA(ratioCurrentA[9]);
		dataHarmonic.setHr12CurrentA(ratioCurrentA[10]);
		dataHarmonic.setHr13CurrentA(ratioCurrentA[11]);
		dataHarmonic.setHr14CurrentA(ratioCurrentA[12]);
		dataHarmonic.setHr15CurrentA(ratioCurrentA[13]);
		dataHarmonic.setHr16CurrentA(ratioCurrentA[14]);
		dataHarmonic.setHr17CurrentA(ratioCurrentA[15]);
		dataHarmonic.setHr18CurrentA(ratioCurrentA[16]);
		dataHarmonic.setHr19CurrentA(ratioCurrentA[17]);
		dataHarmonic.setHr20CurrentA(ratioCurrentA[18]);
		dataHarmonic.setHr21CurrentA(ratioCurrentA[19]);

		dataHarmonic.setHr02CurrentB(ratioCurrentB[0]);
		dataHarmonic.setHr03CurrentB(ratioCurrentB[1]);
		dataHarmonic.setHr04CurrentB(ratioCurrentB[2]);
		dataHarmonic.setHr05CurrentB(ratioCurrentB[3]);
		dataHarmonic.setHr06CurrentB(ratioCurrentB[4]);
		dataHarmonic.setHr07CurrentB(ratioCurrentB[5]);
		dataHarmonic.setHr08CurrentB(ratioCurrentB[6]);
		dataHarmonic.setHr09CurrentB(ratioCurrentB[7]);
		dataHarmonic.setHr10CurrentB(ratioCurrentB[8]);
		dataHarmonic.setHr11CurrentB(ratioCurrentB[9]);
		dataHarmonic.setHr12CurrentB(ratioCurrentB[10]);
		dataHarmonic.setHr13CurrentB(ratioCurrentB[11]);
		dataHarmonic.setHr14CurrentB(ratioCurrentB[12]);
		dataHarmonic.setHr15CurrentB(ratioCurrentB[13]);
		dataHarmonic.setHr16CurrentB(ratioCurrentB[14]);
		dataHarmonic.setHr17CurrentB(ratioCurrentB[15]);
		dataHarmonic.setHr18CurrentB(ratioCurrentB[16]);
		dataHarmonic.setHr19CurrentB(ratioCurrentB[17]);
		dataHarmonic.setHr20CurrentB(ratioCurrentB[18]);
		dataHarmonic.setHr21CurrentB(ratioCurrentB[19]);

		dataHarmonic.setHr02CurrentC(ratioCurrentC[0]);
		dataHarmonic.setHr03CurrentC(ratioCurrentC[1]);
		dataHarmonic.setHr04CurrentC(ratioCurrentC[2]);
		dataHarmonic.setHr05CurrentC(ratioCurrentC[3]);
		dataHarmonic.setHr06CurrentC(ratioCurrentC[4]);
		dataHarmonic.setHr07CurrentC(ratioCurrentC[5]);
		dataHarmonic.setHr08CurrentC(ratioCurrentC[6]);
		dataHarmonic.setHr09CurrentC(ratioCurrentC[7]);
		dataHarmonic.setHr10CurrentC(ratioCurrentC[8]);
		dataHarmonic.setHr11CurrentC(ratioCurrentC[9]);
		dataHarmonic.setHr12CurrentC(ratioCurrentC[10]);
		dataHarmonic.setHr13CurrentC(ratioCurrentC[11]);
		dataHarmonic.setHr14CurrentC(ratioCurrentC[12]);
		dataHarmonic.setHr15CurrentC(ratioCurrentC[13]);
		dataHarmonic.setHr16CurrentC(ratioCurrentC[14]);
		dataHarmonic.setHr17CurrentC(ratioCurrentC[15]);
		dataHarmonic.setHr18CurrentC(ratioCurrentC[16]);
		dataHarmonic.setHr19CurrentC(ratioCurrentC[17]);
		dataHarmonic.setHr20CurrentC(ratioCurrentC[18]);
		dataHarmonic.setHr21CurrentC(ratioCurrentC[19]);

		services.dataHarmonicService.save(dataHarmonic);
	}

	public int hexStringToInt(String data) {

		char[] array = data.toCharArray();
		if (array.length > 2)
			return 0;
		else if (array.length == 1)
			return charToByte(array[0]);
		else {
			return (charToByte(array[0]) * 16 + charToByte(array[1]));
		}

	}

	private byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
}
