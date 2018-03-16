package com.xhb.sockserv.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.xhb.core.entity.ElecParamAnalyse;
import com.xhb.core.entity.ModbusGericProtocol;
import com.xhb.core.entity.PacketRegisterRange;
import com.xhb.core.service.Services;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;

public abstract class ApplicationContext {

	private static ClassPathXmlApplicationContext instance;

	private static ApplicationConfig config;

	private static Services services;
	
	private static Map<String, Class<?>> modbusGenericProMap = new HashMap<>();

	static {
		instance = new ClassPathXmlApplicationContext("spring-*-config.xml");
		config = instance.getBean(ApplicationConfig.class);
		services = instance.getBean(Services.class);
	}

	public static Logger logger = LoggerFactory.getLogger(ApplicationContext.class);
	
	public static ApplicationConfig getConfig() {
		return config;
	}

	public static void setConfig(ApplicationConfig config) {
		ApplicationContext.config = config;
	}

	public static Services getServices() {
		return services;
	}

	public static void setServices(Services services) {
		ApplicationContext.services = services;
	}

	public static Map<String, Class<?>> getModbusGenericProMap() {
		return modbusGenericProMap;
	}

	public static void setModbusGenericProMap(Map<String, Class<?>> modbusGenericProMap) {
		ApplicationContext.modbusGenericProMap = modbusGenericProMap;
	}
	
	/*
	 * 从数据库读取事先配置的协议信息构造modbus通用协议的Map，map的key为通用协议的标识，value为动态生成的反射类
	 */
	public static void makeModbusGenericProMap() {
		try {
			List<ModbusGericProtocol> mgps = services.modbusGericProtocolService.findAll();
			List<PacketRegisterRange> prrs = services.packetRegisterRangeService.findAll();
			List<ElecParamAnalyse> epas = services.elecParamAnalyseService.findAll();
			// 记录每个协议类的发包个数,key为通用协议的标识，value为发包的个数
			Map<String, Integer> proPacketCountMap = doGetProPacketCountMap(mgps, prrs);
			int index = 0;
			for (ModbusGericProtocol modbusGericProtocol : mgps) {
				logger.info(modbusGericProtocol.getProtocol());
				ClassPool pool = ClassPool.getDefault();
				//引入包
				pool.importPackage("java.util.Date");
				pool.importPackage("java.util.List");
				pool.importPackage("com.xhb.sockserv.meter.CRC");
				pool.importPackage("com.xhb.core.entity.DataElectricity");
				pool.importPackage("com.xhb.core.entity.DataElectricity3Phase");
				pool.importPackage("com.xhb.core.entity.ReceiptCircuit");
				pool.importPackage("com.xhb.core.entity.ElectricityType");
				//创建动态类
				CtClass cc = pool.get("com.xhb.sockserv.meter.Meter_ModelBus_Generic");
				cc.setName("Meter_ModelBus_Generic" + index);
				index++;
				//给动态类添加属性
				for(ElecParamAnalyse elecParamAnalyse : epas){
					if (elecParamAnalyse.getPacketRegisterRange().getModbusGericProtocol().getId() == modbusGericProtocol.getId()) {
						CtField cf = new CtField(doGetFieldType(pool,elecParamAnalyse.getElecParam()), 
								elecParamAnalyse.getElecParam() + "_" + elecParamAnalyse.getLoopNo() , cc);
						cf.setModifiers(Modifier.PRIVATE);
						cc.addField(cf);
					}
				}
				//给动态类添加组帧的方法
				String buildWritingFrames = makePacketFrame(prrs,modbusGericProtocol.getId());
				CtMethod m = cc.getDeclaredMethod("buildWritingFrames");
				m.insertBefore(buildWritingFrames);
				logger.info(buildWritingFrames);
				
				//添加解帧和计算字段值的方法
				int count = proPacketCountMap.get(modbusGericProtocol.getProtocol());
				String analysFrameBefore = makeAnalysFrameBefore(count, prrs, epas, modbusGericProtocol.getId());
				CtMethod analyseMethod = cc.getDeclaredMethod("analyzeFrame");
				analyseMethod.insertBefore(analysFrameBefore);
				logger.info(analysFrameBefore);
				//添加入库方法
				String caculateLastResult = getLastResult(prrs, epas, modbusGericProtocol.getId());
				CtMethod handleResult = cc.getDeclaredMethod("handleResult");
				String handleResultBefore = makehandleResultBefore(modbusGericProtocol,epas);
				handleResult.insertBefore(caculateLastResult);
				handleResult.insertAfter(handleResultBefore);
				logger.info(caculateLastResult);
				logger.info(handleResultBefore);
				//组成协议映射
				Class<?> clazz = cc.toClass();
				cc.freeze();
				cc.defrost();
				modbusGenericProMap.put(modbusGericProtocol.getProtocol(), clazz);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("load modbus generic protocol class error!", ex);
		}

	}
	
	/*
	 * 组织计算方法，将寄存器的初始值按照计算公式计算出最终值
	 */
	private static String getLastResult(List<PacketRegisterRange> packetRegisterRanges, List<ElecParamAnalyse> elecParamAnalyses, Long protocolId) {
		String ret = "{";
		for (PacketRegisterRange packetRegisterRange : packetRegisterRanges) {
			if (packetRegisterRange.getModbusGericProtocol().getId() != protocolId) {
				continue;
			}
			for (ElecParamAnalyse elecAnalyse : elecParamAnalyses) {
				if (elecAnalyse.getPacketRegisterRange().getId() != packetRegisterRange.getId()) {
					continue;
				}
				ret += elecAnalyse.getElecParam() + "_" + elecAnalyse.getLoopNo() + "=";
				String formula = elecAnalyse.getCalculateStr().replace("}", "_" + elecAnalyse.getLoopNo());
				formula = formula.replace("{", " ");
				ret += formula + ";";
			}
		}
		ret += "}";
		return ret;
	}

	/*
	 * 将计算出来的数据存储到数据表中
	 */
	private static String makehandleResultBefore(ModbusGericProtocol modbusGericProtocol, List<ElecParamAnalyse> elecParamAnalyses) {
		String ret = "{List receipCircuits = doGetReceiptCicuirt();"
				+ "Date now = new Date();"
				+ "for(int i = 0; i < receipCircuits.size() ; i++ ){"
				+ "ReceiptCircuit receiptCircuit = (ReceiptCircuit)receipCircuits.get(i);"
				+ "DataElectricity3Phase dataElectricity3Phase = null;"
				+ "DataElectricity dataElectricity = new DataElectricity();"
				+ "dataElectricity.setReceiptCircuit(receiptCircuit);"
				+ "dataElectricity.setReadTime(now);"
				+ "dataElectricity.setElectricityType(ElectricityType." + modbusGericProtocol.getElectricityType() + ");";
		for (ElecParamAnalyse elecParamAnalyse : elecParamAnalyses) {
			if (elecParamAnalyse.getPacketRegisterRange().getModbusGericProtocol().getId() != modbusGericProtocol.getId()
					|| elecParamAnalyse.getElecParam().startsWith("U") || elecParamAnalyse.getElecParam().startsWith("I")) {
				continue;
			}
			if (isSplitPhaseVariable(elecParamAnalyse.getElecParam())) {
				ret += " if(dataElectricity3Phase == null){dataElectricity3Phase = new DataElectricity3Phase();}";
				ret += "if(receiptCircuit.getCircuitNo().equals(\"" + elecParamAnalyse.getLoopNo() + "\")){";
				ret += " dataElectricity3Phase.set"+ elecParamAnalyse.getElecParam().substring(0, 1).toUpperCase() 
						+ elecParamAnalyse.getElecParam().substring(1) + "(new Double(" + elecParamAnalyse.getElecParam() 
						+ "_" + elecParamAnalyse.getLoopNo() + "));}";
				continue;
			}
			ret += "if(receiptCircuit.getCircuitNo().equals(\"" + elecParamAnalyse.getLoopNo() + "\")){";
			ret += "dataElectricity.set" + elecParamAnalyse.getElecParam().substring(0, 1).toUpperCase() 
					+ elecParamAnalyse.getElecParam().substring(1) + "(new Double(" + elecParamAnalyse.getElecParam() 
					+ "_" + elecParamAnalyse.getLoopNo() + "));}";
		}
		ret += "saveDataToDataBase(dataElectricity,dataElectricity3Phase);";
		ret += "}}";
		return ret;
	}

	private static boolean isSplitPhaseVariable(String elecParam) {
		if (elecParam.equals("kwhA") || elecParam.equals("kwhB") || elecParam.equals("kwhC") 
				|| elecParam.equals("kwhForwardA") || elecParam.equals("kwhForwardB") || elecParam.equals("kwhForwardC")
				|| elecParam.equals("kwhReverseA") || elecParam.equals("kwhReverseB") || elecParam.equals("kwhReverseC")
				|| elecParam.equals("kvarh1A") || elecParam.equals("kvarh1B") || elecParam.equals("kvarh1C")
				|| elecParam.equals("kvarh2A") || elecParam.equals("kvarh2B") || elecParam.equals("kvarh2C")) {
			return true;
		}
		return false;
	}

	/*
	 * 组织协议类的下发帧
	 */
	private static String makePacketFrame(List<PacketRegisterRange> prrs, Long protocolId) {
		String ret = "{int[] data = new int[8];byte[] frame;data[0] = Integer.parseInt(receiptMeter.getMeterNo());data[1] = 0x03;";
		for(PacketRegisterRange packetRegisterRange : prrs){
			if (packetRegisterRange.getModbusGericProtocol().getId() == protocolId) {
				int startAt = Integer.parseInt(packetRegisterRange.getStartAt(), 16);
				int endAt = Integer.parseInt(packetRegisterRange.getEndAt(), 16);
				int length = endAt - startAt + 1;
				ret += " data[2] = 0x" + packetRegisterRange.getStartAt().substring(0, 2) + "; ";
				ret += " data[3] = 0x" + packetRegisterRange.getStartAt().substring(2, 4) + "; ";
				ret += " data[4] = " + (length / 256) + "; ";
				ret += " data[5] = " + (length % 256) + "; ";
				ret += " int[] crc = CRC.calculateCRC(data, 6); ";
				ret += " data[6] = crc[0]; ";
				ret += " data[7] = crc[1]; ";
				ret += " frame = new byte[data.length]; ";
				ret += " for (int i = 0; i < data.length; i++) { ";
				ret += " 	frame[i] = (byte) data[i]; ";
				ret += " } ";
				ret += " writingFrames.add(frame); ";
			}
		}
		ret += "}";
		return ret;
	}

	
	/*
	 * 组织解帧方法，解出寄存器的初始值
	 */
	private static String makeAnalysFrameBefore(int count, List<PacketRegisterRange> packetRegisterRanges,
			List<ElecParamAnalyse> elecParamAnalyses, Long protocolId) {
		String ret = "{if (readingFrames.size() != " + count + ") {" + " return false; " + "} "
				+ " int[] data = new int[frame.length - 9];" + " for (int i = 0; i < data.length; i++) {"
				+ " data[i] = frame[i + 8] & 0xFF;" + "} "
				+ " int meterNo = Integer.parseInt(receiptMeter.getMeterNo()); " + " if(meterNo != data[0]){ "
				+ " return false; " + " } " + " if (!CRC.isValid(data)) " + " return false;";
		for (PacketRegisterRange packetRegisterRange : packetRegisterRanges) {
			if (packetRegisterRange.getModbusGericProtocol().getId() != protocolId) {
				continue;
			}
			int startAt = Integer.parseInt(packetRegisterRange.getStartAt(), 16);
			int endAt = Integer.parseInt(packetRegisterRange.getEndAt(), 16);
			int length = endAt - startAt + 1;
			ret += "if (data[2] == " + length * 2 + ") { ";
			for (ElecParamAnalyse elecAnalyse : elecParamAnalyses) {
				if(elecAnalyse.getPacketRegisterRange().getId() != packetRegisterRange.getId() ||  elecAnalyse.getStartAt().equals("")){
					continue;
				}
				int elecParamStart = Integer.parseInt(elecAnalyse.getStartAt(), 16);
				ret += "" + elecAnalyse.getElecParam() + "_" + elecAnalyse.getLoopNo() + "=";
				if (elecAnalyse.getRegisterCount() == 1) {
					ret += "data[" + ((elecParamStart - startAt) * 2 + 3) + "] * 256 + data["
							+ ((elecParamStart - startAt) * 2 + 4) + "] ; ";
				} else {
					ret += "data[" + ((elecParamStart - startAt) * 2 + 3) + "] * 256 * 256 * 256 + data["
							+ ((elecParamStart - startAt) * 2 + 4) + "] * 256 * 256 + data["
							+ ((elecParamStart - startAt) * 2 + 5) + "] * 256 + data["
							+ ((elecParamStart - startAt) * 2 + 6) + "] ; ";
				}
			}
			ret += "}";
		}
		ret += " return true;}";
		return ret;
	}
	
	
	/*
	 * 获取电参量类型
	 */
	public static CtClass doGetFieldType(ClassPool pool,String elecParam)throws Exception{
		if(elecParam.equals("readTime")){
			return pool.getCtClass("java.lang.String");
		}
		return CtClass.doubleType;
	}

	/*
	 * 记录每个协议类的发包个数,key为通用协议的标识，value为发包的个数
	 */
	private static Map<String, Integer> doGetProPacketCountMap(List<ModbusGericProtocol> mgps,
			List<PacketRegisterRange> prrs) {
		Map<String, Integer> proPacketCountMap = new HashMap<>();
		for(ModbusGericProtocol modbusGericProtocol : mgps){
			Integer count = 0;
			for(PacketRegisterRange packetRegisterRange : prrs){
				if (packetRegisterRange.getModbusGericProtocol().getId() == modbusGericProtocol.getId()) {
					count++;
				}
			}
			proPacketCountMap.put(modbusGericProtocol.getProtocol(), count);
		}
		return proPacketCountMap;
	}
}
