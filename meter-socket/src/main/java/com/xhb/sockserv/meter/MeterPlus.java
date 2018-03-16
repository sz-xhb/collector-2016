package com.xhb.sockserv.meter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xhb.core.entity.CircuitType;
import com.xhb.core.entity.CollectorType;
import com.xhb.core.entity.Company;
import com.xhb.core.entity.Customer;
import com.xhb.core.entity.MeterStatus;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.core.service.Services;
import com.xhb.sockserv.config.ApplicationContext;
import com.xhb.sockserv.util.FrameUtils;

public class MeterPlus {

	private Services services = ApplicationContext.getServices();
	private Logger logger = LoggerFactory.getLogger(getClass());
	private int interfaceCount = 0;
	private int funcCode = 0x21;
	private List<Integer> deviceCount = new ArrayList<>();
	private String dtuAges = "1";
	private ConcurrentLinkedQueue<byte[]> devicePacketQueue = new ConcurrentLinkedQueue<byte[]>();
	private boolean collectorDataAgain = false;
	private static Lock lock = new ReentrantLock();
	
	
	public static String AM_3PHASE = "1";
	public static String AM_1PHASE = "2";
	public static String AM_DC = "3";
	public static String AM_HARM = "4";
	public static String AM_3PHASE_RATE = "5";
	public static String E_OP = "6";
	public static String WM = "7";
	public static String GM = "8";

	
	//获取下发报文
	public byte[] getPacketToWrite() {
		if (funcCode == 0x21) {
			return new byte[] { 0x55, (byte) 0xAA, 0x21, 0x00, 0x00 };
		}else if (funcCode == 0x23) {
			return new byte[] { 0x55, (byte) 0xAA, 0x23, 0x00, 0x00 };
		}else if (funcCode == 0x25) {
			return new byte[] { 0x55, (byte) 0xAA, 0x25, 0x00, 0x00 };
		}else if (funcCode == 0x26) {
			return makeDeviceConfigPacket();
		}else if (funcCode == 0x51) {
			return makeHistoryDataPacket();
		}
		return null;
	}

	/*
	 * 组织请求历史数据的下发报文
	 */
	private byte[] makeHistoryDataPacket() {
		String xmlData = "<req>\n\t<pre_result>1</pre_result>\n</req>";
		byte[] data = xmlData.getBytes();
		encrypt(data, 0x51);
		byte[] buffer = new byte[data.length + 5];
		buffer[0] = 0x55;
		buffer[1] = (byte) 0xAA;
		buffer[2] = 0x51;
		buffer[3] = (byte)(data.length / 256);
		buffer[4] = (byte)(data.length % 256);
		System.arraycopy(data, 0, buffer, 5, data.length);
		return buffer;
	}

	// 解密方法
	public void decrypt(byte[] buf, int token) {
		int org;
		for (int i = 0; i < buf.length; i++) {
			org = buf[i];
			buf[i] = (byte) (0xff - (org ^ token));
			token = org;
		}
	}

	// 加密方法
	public void encrypt(byte[] buf, int token) {
		int i;
		for (i = 0; i < buf.length; i++) {
			buf[i] = (byte) ((0xff - buf[i]) ^ token);
			token = buf[i];
		}
	}

	/*
	 * 组织设备信息配置的下发报文
	 */
	private byte[] makeDeviceConfigPacket() {
		if (devicePacketQueue.size() > 0) {
			return devicePacketQueue.poll();
		}
		for (int i = 0; i < interfaceCount; i++) {
			for (int j = 0; j < deviceCount.get(i); j++) {
				String xmlData = "<req>\n\t<addr>";
				xmlData += i + "," + j;
				xmlData += "</addr>\n</req>";
				byte[] data = xmlData.getBytes();
				encrypt(data, 0x26);
				byte[] buffer = new byte[data.length + 5];
				buffer[0] = 0x55;
				buffer[1] = (byte) 0xAA;
				buffer[2] = 0x26;
				buffer[3] = (byte)(data.length / 256);
				buffer[4] = (byte)(data.length % 256);
				System.arraycopy(data, 0, buffer, 5, data.length);
				devicePacketQueue.add(buffer);
			}
		}
		return null;
	}

	/*
	 * 处理采集器配置的响应报文，调用方法更新采集器配置
	 */
	public void handleDtuConfigInfo(byte[] msg, String dtuNo) {
		try {
			byte[] data = new byte[msg.length - 5];
			System.arraycopy(msg, 5, data, 0, data.length);
			decrypt(data, 0x23);
			String xmlData = new String(data,"utf-8");
			logger.warn(dtuNo + "采集器加配置信息:" + xmlData);
			SAXBuilder build = new SAXBuilder();
			StringReader reader = new StringReader(xmlData);
			Document document = build.build(reader);// 获得文档对象
			Element root = document.getRootElement();// 获得根节点
			@SuppressWarnings("unchecked")
			List<Element> nodes = root.getChildren();
			String dtuNoNew = nodes.get(0).getText();
			String dtuNameNew = nodes.get(2).getText();
			String period = nodes.get(4).getText();
			CollectorType type = CollectorType.COLLECTORPLUS;
			updateCollectorConfigInfo(dtuNoNew,dtuNameNew,type,period);
			funcCode = 0x25;
		} catch (Exception ex) {
			logger.error("handle dtu plus config info error!", ex);
		}
	}

	/*
	 * 更新采集器的配置信息
	 */
	private void updateCollectorConfigInfo(String dtuNoNew, String dtuNameNew, CollectorType type, String period) {
		String csvInfo = doGetInfoFromCSVByCollectorNo(dtuNoNew);
		String companyName = null;
		String customerName = null;
		if (csvInfo == null) {
			companyName = ApplicationContext.getConfig().getCompanyName();
			customerName = ApplicationContext.getConfig().getCustomerName();
			doUpdateCsvFile(dtuNoNew + "," + companyName + "," + customerName);
		}else{
			companyName = csvInfo.split(",")[1];
			customerName = csvInfo.split(",")[2];
		}
		
		lock.lock();
		try {
			Company company = services.companyServcie.findByCompanyName(companyName);
			if (company == null) {
				company = new Company();
				company.setName(companyName);
				services.companyServcie.save(company);
			}
			Customer customer = services.customerService.findByCustomerName(customerName);
			if (customer == null) {
				customer = new Customer();
				customer.setCompany(company);
				customer.setName(customerName);
				services.customerService.save(customer);
			}

			ReceiptCollector rc = services.receiptCollectorService.getByDtuNo(dtuNoNew);
			if (rc == null) {
				rc = new ReceiptCollector();
				rc.setAges(Integer.parseInt(dtuAges));
				rc.setCollectorNo(dtuNoNew);
				rc.setName(dtuNameNew);
				rc.setCollectorType(type);
				rc.setCustomer(customer);
				rc.setPeriod(Long.parseLong(period) / 60L);
				services.receiptCollectorService.save(rc);
			} else {
				rc.setName(dtuNoNew);
				rc.setName(dtuNameNew);
				rc.setCollectorType(type);
				rc.setCustomer(customer);
				rc.setAges(Integer.parseInt(dtuAges));
				rc.setPeriod(Long.parseLong(period) / 60L);
				services.receiptCollectorService.update(rc);
				;
			}
		} catch (Exception ex) {
			logger.error("handle collector configInfo error!", ex);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 向csv文件中追加新的匹配信息
	 * @param tailStr 要追加的字符串
	 */
	private void doUpdateCsvFile(String tailStr) {
		lock.lock();
		try {
			String path = java.net.URLDecoder.decode(getClass().getClassLoader().getResource("").getPath(),"UTF-8");
			BufferedWriter bw = new BufferedWriter(
					new FileWriter(new File(path + "collector_company_customer.csv"), true));
			bw.write(tailStr);
			bw.newLine();
			bw.close();
		} catch (Exception ex) {
			logger.error("get company info from csv file error!", ex);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 从csv配置文件中获取匹配的字符串信息
	 * @param dtuNoNew 采集器的编号
	 * @return csv文件中匹配的记录
	 */
	private String doGetInfoFromCSVByCollectorNo(String dtuNoNew) {
		lock.lock();
		try {
			String path = java.net.URLDecoder.decode(getClass().getClassLoader().getResource("").getPath(),"UTF-8");
			BufferedReader br = new BufferedReader(new FileReader(new File(path + "collector_company_customer.csv")));
			String readStr = br.readLine();
			while (readStr != null) {
				if (readStr.split(",")[0].equals(dtuNoNew)) {
					br.close();
					return readStr;
				}
				readStr = br.readLine();
			}
			br.close();
		} catch (Exception ex) {
			logger.error("get company info from csv file error!",ex);
		}finally {
			lock.unlock();
		}
		return null;
	}

	/*
	 * 处理采集器的接口响应报文，存储采集器的接口数
	 */
	public void handDtuInterfaceInfo(byte[] msg, String dtuNo) {
		try {
			byte[] data = new byte[msg.length - 5];
			System.arraycopy(msg, 5, data, 0, data.length);
			decrypt(data, 0x25);
			String xmlData = new String(data,"utf-8");
			logger.warn(dtuNo + "采集器的接口数:" + xmlData);
			SAXBuilder build = new SAXBuilder();
			StringReader reader = new StringReader(xmlData);
			Document document = build.build(reader);// 获得文档对象
			Element root = document.getRootElement();// 获得根节点
			@SuppressWarnings("unchecked")
			List<Element> nodes = root.getChildren();
			interfaceCount = Integer.parseInt(nodes.get(0).getText());
			for (int i = 0; i < interfaceCount; i++) {
				deviceCount.add(Integer.parseInt(nodes.get(i+1).getText()));
			}
			funcCode = 0x26;
		} catch (Exception ex) {
			logger.error("handle dtu plus interface info error!", ex);
		}
	}

	
	/*
	 * 处理采集器、设备的配置信息
	 */
	public void handleDtuDeviceConfigInfo(byte[] msg, String dtuNo) {
		try {
			ReceiptCollector collector = services.receiptCollectorService.getByDtuNo(dtuNo);
			if (collector == null) {
				logger.info("the dtu:" + dtuNo + " is not exist!");
				return;
			}
			byte[] data = new byte[msg.length - 5];
			System.arraycopy(msg, 5, data, 0, data.length);
			decrypt(data, 0x26);
			String xmlData = new String(data,"utf-8");
			logger.warn(dtuNo + "设备的配置信息:" + xmlData);
			SAXBuilder build = new SAXBuilder();
			StringReader reader = new StringReader(xmlData);
			Document document = build.build(reader);// 获得文档对象
			Element root = document.getRootElement();// 获得根节点
			@SuppressWarnings("unchecked")
			List<Element> nodes = root.getChildren();
			String meterNo = nodes.get(0).getText();
			String meterType = doGetMeterType(nodes.get(1).getText());
			String protocolType = doGetProtocolType(nodes.get(1).getText());
			String nhbMeterType = doGetNhbMeterType(nodes.get(1).getText());
			ReceiptMeter meter = services.ReceiptMeterService.findByDtuNoAndMeterNo(dtuNo, meterNo);
			if(meter == null){
				meter = new ReceiptMeter();
				meter.setMeterNo(meterNo);
				meter.setMeterType(meterType);
				meter.setProtocolType(protocolType);
				meter.setReceiptCollector(collector);
				meter.setNhbMeterType(nhbMeterType);
				services.ReceiptMeterService.save(meter);
			}else{
				meter.setMeterNo(meterNo);
				meter.setReceiptCollector(collector);
				meter.setMeterType(meterType);
				meter.setProtocolType(protocolType);
				meter.setNhbMeterType(nhbMeterType);
				services.ReceiptMeterService.update(meter);
			}			
			for (int i = 5; i < nodes.size(); i++) {
				Integer loopNo = Integer.parseInt(nodes.get(i).getText().split(",")[0]) + 1;
				String loopName = nodes.get(i).getText().split(",")[1];
				ReceiptCircuit circuit = services.receiptCircuitService.findCircuitByDtuNoAndMeterNoAndLoopNo(dtuNo, meterNo, loopNo);
				if(circuit == null){
					circuit = new ReceiptCircuit();
					circuit.setCircuitNo(loopNo.toString());
					circuit.setName(loopName);
					circuit.setCircuitType(CircuitType.ELECTRICITY);
					circuit.setReceiptMeter(meter);
					services.receiptCircuitService.save(circuit);
				}else{
					circuit.setCircuitNo(loopNo.toString());
					circuit.setName(loopName);
					circuit.setReceiptMeter(meter);
					services.receiptCircuitService.update(circuit);
				}		
			}
			//logger.info("devicePacketQueue.size:" + devicePacketQueue.size());
			if (devicePacketQueue.size() == 0) {
				funcCode = 0x51;
			}
			
		} catch (Exception ex) {
			logger.error("handle dtu plus interface info error!", ex);
		}
	}
	/**
	 * 根据上传的设备类型，获取新宏博设备类型
	 * @param meterTypeCode 设备类型的编码
	 * @return 新宏博设备类型
	 */
	private String doGetNhbMeterType(String meterTypeCode) {
		if (meterTypeCode.equals("101") || meterTypeCode.equals("102") || meterTypeCode.equals("103")
				|| meterTypeCode.equals("201") || meterTypeCode.equals("202") || meterTypeCode.equals("203")
				|| meterTypeCode.equals("302") || meterTypeCode.equals("303") || meterTypeCode.equals("103") ) {
			return "ELECTRICITY";
		}
		if (meterTypeCode.equals("401") || meterTypeCode.equals("104") || meterTypeCode.equals("204")) {
			return "HARMNIC";
		}
		if (meterTypeCode.equals("601")) {
			return "SWITCH";
		}
		if (meterTypeCode.equals("701")) {
			return "WATER";
		}
		if (meterTypeCode.equals("801") || meterTypeCode.equals("802")) {
			return "STEAM";
		}
		if (meterTypeCode.equals("502") || meterTypeCode.equals("503") || meterTypeCode.equals("504")) {
			return "RATE";
		}
		return "ELECTRICITY";
	}

	/**
	 * 根据设备类型获取设备的协议
	 * @param meterTypeCode 设备类型的编码
	 * @return 协议类型的编码
	 */
	private String doGetProtocolType(String meterTypeCode) {
		if (meterTypeCode.equals("101")) {
			return "NHB_2007_1";
		}else if (meterTypeCode.equals("102")) {
			return "NHB_M4V02_4_0";
		}else if (meterTypeCode.equals("105")) {
			return "ACR_3_1";
		}else if (meterTypeCode.equals("103")) {
			return "NHB_M1V02_3";
		}else if (meterTypeCode.equals("201")) {
			return "DLT645_2007_THREEWAY";
		}else if (meterTypeCode.equals("202")) {
			return "NHB_M4V02_0";
		}else if (meterTypeCode.equals("203")) {
			return "NHB_NETER_ONEWAY_VB";
		}else if (meterTypeCode.equals("301")) {
			return "DLT645_2007_DC";
		}else if (meterTypeCode.equals("302")) {
			return "DC";
		}else if (meterTypeCode.equals("303") || meterTypeCode.equals("304")) {
			return "MODEL_DC";
		}else if (meterTypeCode.equals("401") || meterTypeCode.equals("104") || meterTypeCode.equals("204")) {
			return "NHB_HARMONIC_PQA";
		}else if (meterTypeCode.equals("601")) {
			return "RECLOSER";
		}else if (meterTypeCode.equals("701")) {
			return "QIANBAOTONG_WATER";
		}else if (meterTypeCode.equals("801") || meterTypeCode.equals("802")) {
			return "SIBO_YIKONG_GAS";
		}else if (meterTypeCode.equals("502") || meterTypeCode.equals("503")|| meterTypeCode.equals("504")) {
			return "NHB_RATE";
		}
		return "OTHER";
	}

	//获取设备类型
	private String doGetMeterType(String meterTypeCode) {
		Map<String, String> map = FrameUtils.getMeterTypeNameAndCodeMap();
		if (meterTypeCode.equals("101") || meterTypeCode.equals("103")|| meterTypeCode.equals("201")|| meterTypeCode.equals("105")) {
			return map.get("[1]三相单路表");
		}else if(meterTypeCode.equals("202") || meterTypeCode.equals("102")){
			return map.get("[2]三相四路表");
		}else if(meterTypeCode.equals("203")){
			return map.get("[3]单相单路表");
		}else if(meterTypeCode.equals("302")){
			return map.get("[4]单路直流表");
		}else if(meterTypeCode.equals("303") || meterTypeCode.equals("304")){
			return map.get("[5]多路直流表");
		}else if(meterTypeCode.equals("401") || meterTypeCode.equals("104") || meterTypeCode.equals("204")){
			return map.get("[6]谐波表");
		}else if(meterTypeCode.equals("601")){
			return map.get("[7]开关设备");
		}else if(meterTypeCode.equals("701")){
			return map.get("[8]水表");
		}else if(meterTypeCode.equals("801") || meterTypeCode.equals("802")){
			return map.get("[9]蒸汽表");
		}else if(meterTypeCode.equals("502") || meterTypeCode.equals("503") || meterTypeCode.equals("504")){
			return map.get("[11]三相单路费率表");
		}
		return map.get("[18]其他类型表");
	}

	/**
	 * 解析收到的xml数据，按设备类型调用不同的方法将设备数据存储到数据库
	 * @param msg  
	 * 		收到的报文
	 * @param dtuNo 
	 * 		采集器编号
	 */
	public void handleDtuDataInfo(byte[] msg, String dtuNo) {
		try {
			byte[] data = new byte[msg.length - 5];
			System.arraycopy(msg, 5, data, 0, data.length);
			decrypt(data, 0x51);
			String xmlData = new String(data,"utf-8");
			//logger.warn(dtuNo + " : historydata received");// + xmlData
			logger.warn(dtuNo + "读历史数据：" + xmlData);// + xmlData
			SAXBuilder build = new SAXBuilder();
			StringReader reader = new StringReader(xmlData);
			Document document = build.build(reader);// 获得文档对象
			Element root = document.getRootElement();// 获得根节点
			@SuppressWarnings("unchecked")
			List<Element> iterms = root.getChildren();
			if(iterms.size() == 0){
				collectorDataAgain = false;
			}
			for (Element el : iterms) {
				@SuppressWarnings("unchecked")
				List<Element> nodes = el.getChildren();
				String meterNo = nodes.get(1).getText().split(",")[1];
				Integer loopNo = Integer.parseInt(nodes.get(1).getText().split(",")[2]) + 1;
				String dataInfo = nodes.get(2).getText();
				String dType = nodes.get(0).getText();
				MeterPlusAbstract meterPlusAbstract = null;
				if (dType.equals(AM_3PHASE)) {
					meterPlusAbstract = new MeterPlusAM3PHASE();
				}else if (dType.equals(AM_1PHASE)) {
					meterPlusAbstract = new MeterPlusAM1PHASE();
				}else if (dType.equals(AM_DC)) {
					meterPlusAbstract = new MeterPlusAMDC();
				}else if (dType.equals(AM_HARM)) {
					meterPlusAbstract = new MeterPlusAMHARM();
				}else if (dType.equals(AM_3PHASE_RATE)) {
					meterPlusAbstract = new MeterPlusAM3PHASERATE();
				}else if (dType.equals(E_OP)) {
					//saveE_OPEnergyInfo(dtuNo,meterNo,loopNo,dataInfo);
				}else if (dType.equals(WM)) {
					meterPlusAbstract = new MeterPlusWater();
				}else if (dType.equals(GM)) {
					meterPlusAbstract = new MeterPlusGM();
				}else {
					break;
				}
				meterPlusAbstract.handleEnergyInfo(dtuNo, meterNo, loopNo, dataInfo);
				//如果历史数据有积压，那么接着读历史数据，不用等到下一个周期再读
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date collectTime = sdf.parse("20" + dataInfo.substring(0, 12));
				if (System.currentTimeMillis() - collectTime.getTime() > ApplicationContext.getConfig().getFrameDtuPlusWritingPeriod()) {
					funcCode = 0x51;
					collectorDataAgain = true;
				}else{
					collectorDataAgain = false;
				}
				//更新采集器加的在线状态
				updateMeterStatus(dtuNo,meterNo);
			}
		} catch (Exception ex) {
			logger.error("handle dtu plus data info error!", ex);
		}
	}

	/*
	 * 更新设备在线时间
	 */
	private void updateMeterStatus(String dtuNo, String meterNo) {
		ReceiptMeter meter = services.ReceiptMeterService.findByDtuNoAndMeterNo(dtuNo,meterNo);
		if (meter == null) {
			return;
		}
		MeterStatus meterStatus = services.meterStatusService.get(meter.getId());
		if (meterStatus == null) {
			meterStatus = new MeterStatus();
			meterStatus.setActiveTime(new Date());
			meterStatus.setMeterId(meter.getId());
			meterStatus.setReceiptMeter(meter);
			services.meterStatusService.save(meterStatus);
		}else {
			meterStatus.setActiveTime(new Date());
			services.meterStatusService.update(meterStatus);
		}
		
	}

	public int getInterfaceCount() {
		return interfaceCount;
	}

	public void setInterfaceCount(int interfaceCount) {
		this.interfaceCount = interfaceCount;
	}

	public List<Integer> getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(List<Integer> deviceCount) {
		this.deviceCount = deviceCount;
	}

	public int getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(int funcCode) {
		this.funcCode = funcCode;
	}

	public String getDtuAges() {
		return dtuAges;
	}

	public void setDtuAges(String dtuAges) {
		this.dtuAges = dtuAges;
	}
	public ConcurrentLinkedQueue<byte[]> getDevicePacketQueue() {
		return devicePacketQueue;
	}

	public void setDevicePacketQueue(ConcurrentLinkedQueue<byte[]> devicePacketQueue) {
		this.devicePacketQueue = devicePacketQueue;
	}

	public boolean isCollectorDataAgain() {
		return collectorDataAgain;
	}

	public void setCollectorDataAgain(boolean collectorDataAgain) {
		this.collectorDataAgain = collectorDataAgain;
	}

	
}
