package com.xhb.sockserv.keep;

import io.netty.channel.ChannelHandlerContext;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xhb.sockserv.meter.AbstractDevice;
import com.xhb.sockserv.meter.CJ188_WATER;
import com.xhb.sockserv.meter.CalibrationModbusV02;
import com.xhb.sockserv.meter.Meter_97;
import com.xhb.sockserv.meter.Meter_AKR_DTSD1352;
import com.xhb.sockserv.meter.Meter_AKR_PZ80E4;
import com.xhb.sockserv.meter.Meter_CHZJZ_PZ810;
import com.xhb.sockserv.meter.Meter_CHZJZ_SFER_200;
import com.xhb.sockserv.meter.Meter_CHZJZ_ZRY4Z_9HY;
import com.xhb.sockserv.meter.Meter_DG_XS;
import com.xhb.sockserv.meter.Meter_EIGHT_DC;
import com.xhb.sockserv.meter.Meter_Hisense_Electric;
import com.xhb.sockserv.meter.Meter_LCH_EX8_33_V;
import com.xhb.sockserv.meter.Meter_LCH_SIX_DC;
import com.xhb.sockserv.meter.Meter_MODEL_DC;
import com.xhb.sockserv.meter.Meter_Mobile1;
import com.xhb.sockserv.meter.Meter_Mobile2;
import com.xhb.sockserv.meter.Meter_MobilePhaseEnergy;
import com.xhb.sockserv.meter.Meter_MobileThreeSinglePhase;
import com.xhb.sockserv.meter.Meter_NB_WATER;
import com.xhb.sockserv.meter.Meter_Oil_Market;
import com.xhb.sockserv.meter.Meter_OneWayA;
import com.xhb.sockserv.meter.Meter_Sefer_PD194E_9S4;
import com.xhb.sockserv.meter.Meter_Sefer_PD194_Harmonic;
import com.xhb.sockserv.meter.Meter_WJYRC_GAS;
import com.xhb.sockserv.meter.Meter_WJYRC_WATER;
import com.xhb.sockserv.meter.Meter_XP_96BDE;
import com.xhb.sockserv.meter.Meter_Yunshang_HongYin;
import com.xhb.sockserv.meter.Meter_Yunshang_ShenZhen_PMC43X;
import com.xhb.sockserv.meter.Reclose;
import com.xhb.sockserv.meter.Relay;
import com.xhb.sockserv.meter.RelayXHB;
import com.xhb.sockserv.meter.Temperature;
import com.xhb.sockserv.meter.TemperatureXHB;
import com.xhb.sockserv.util.ChannelHandlerContextUtils;
import com.xhb.core.entity.CollectorType;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.core.service.Services;
import com.xhb.sockserv.command.CommandResponse;
import com.xhb.sockserv.config.ApplicationConfig;
import com.xhb.sockserv.config.ApplicationContext;
import com.xhb.sockserv.dtu.DtuBridge;
import com.xhb.sockserv.dtu.DtuContext;
import com.xhb.sockserv.meter.Device;
import com.xhb.sockserv.meter.ElectricFilter;
import com.xhb.sockserv.meter.Meter4_02a;
import com.xhb.sockserv.meter.Meter4_02b;
import com.xhb.sockserv.meter.Meter4_02b_1;
import com.xhb.sockserv.meter.Meter4_02c;
import com.xhb.sockserv.meter.Meter4_02c_1;
import com.xhb.sockserv.meter.Meter4_02d;
import com.xhb.sockserv.meter.Meter4_02d_1;
import com.xhb.sockserv.meter.Meter4_02e;
import com.xhb.sockserv.meter.Meter4_02a_1;
import com.xhb.sockserv.meter.MeterDC;
import com.xhb.sockserv.meter.MeterLichuangHarmonic;
import com.xhb.sockserv.meter.MeterXinhongboHarmonic;
import com.xhb.sockserv.meter.MeterXinhongboHarmonicPQA;
import com.xhb.sockserv.meter.MeterZhimingModbus;
import com.xhb.sockserv.meter.Meter_07_Energy;
import com.xhb.sockserv.meter.Meter_07_NHB;
import com.xhb.sockserv.meter.Meter_07_NoRate;
import com.xhb.sockserv.meter.Meter_07_YUNSHANG;
import com.xhb.sockserv.meter.Meter_07_ZHENGTAI;

public class KeepWritingTimerTask extends TimerTask {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ApplicationConfig config = ApplicationContext.getConfig();

	private Services services = ApplicationContext.getServices();

	private Map<String, Class<?>> modbusGenericProMap = ApplicationContext.getModbusGenericProMap();
	@Override
	public void run() {
		try {
			Enumeration<DtuContext> elements = KeepContextMap.getInstance().elements();
			while (elements.hasMoreElements()) {
				DtuContext dtuContext = elements.nextElement();
				ChannelHandlerContext dtuCHC = dtuContext.getDtuCHC();
				if (dtuCHC == null || !dtuCHC.channel().isActive() || DtuBridge.isBridging(dtuCHC)) {
					continue;
				}
				if (dtuContext.getDelay() > 0) {
					dtuContext.setDelay(dtuContext.getDelay() - 1);
					continue;
				}
				handlePrdQueue(dtuContext);
				handleCmdQueue(dtuContext);
				writeDevice(dtuContext);
			}
		} catch (Exception e) {
			logger.error("unexpected exception", e);
		}
	}

	private void handlePrdQueue(DtuContext dtuContext) {
		Device device = dtuContext.getCurrentDevice();
		if (device != null && !device.isComplete()) {
			return;
		}
		ConcurrentLinkedQueue<Device> prdQueue = dtuContext.getPrdQueue();
		//timeOut记录抄表时间间隔，如果配置文件中要求统一的采集间隔，则从配置文件中读取采集时间
		//如果采集时间不统一，则分别从采集器的采集周期字段中读取采集周期
		long timerOut = 0L;
		if(config.isDtuPeriodUnified()){
			timerOut = config.getMeterReadingPeriod();
		}else{
			ReceiptCollector collector = services.receiptCollectorService.getByDtuNo(dtuContext.getDtuNo());
			if (collector == null) {
				logger.warn("{} not found", dtuContext.getDtuNo());
				return;
			}
			timerOut = collector.getPeriod();
			timerOut *= 60000L;
		}
		if (prdQueue.isEmpty()
				&& System.currentTimeMillis() - dtuContext.getLastTime() > timerOut) {
			ReceiptCollector collector = services.receiptCollectorService.getByDtuNo(dtuContext.getDtuNo());
			if (collector == null) {
				logger.warn("{} not found", dtuContext.getDtuNo());
				return;
			}
			if (collector.getCollectorType() == CollectorType.COLLECTORPLUS) {
				logger.warn("this collector is collectorPlus!", dtuContext.getDtuNo());
				return;
			}
			List<ReceiptMeter> meters = services.ReceiptMeterService.findByCollectorId(collector.getId());
			if (meters.isEmpty()) {
				logger.warn("{} is empty", dtuContext.getDtuNo());
				return;
			}
			addCalibrations(prdQueue, collector, meters);
			addDevices(prdQueue, collector, meters);
			dtuContext.setLastTime(System.currentTimeMillis());
		}
	}

	//每天凌晨校时
	private void addCalibrations(ConcurrentLinkedQueue<Device> prdQueue,
			ReceiptCollector collector, List<ReceiptMeter> meters) {
		Date now = new Date();
		Date date = DateUtils.truncate(now, Calendar.DATE);
		if (now.getTime() - date.getTime() > config.getMeterReadingPeriod()) {
			return;
		}
		boolean calibrateModbusV02 = false;
		for (ReceiptMeter meter : meters) {
			if (meter.getProtocolType().equals("NHB_M4V02_0")) {
				calibrateModbusV02 = true;
			}
			if (meter.getProtocolType().equals("NHB_M4V02_1")) {
				calibrateModbusV02 = true;
			}
			if (meter.getProtocolType().equals("NHB_M4V02_2")) {
				calibrateModbusV02 = true;
			}
			if (meter.getProtocolType().equals("NHB_M4V02_3")) {
				calibrateModbusV02 = true;
			}
			if (meter.getProtocolType().equals("NHB_M4V02_4")) {
				calibrateModbusV02 = true;
			}
		}
		if (calibrateModbusV02) {
			CalibrationModbusV02 calibrationModbusV02 = new CalibrationModbusV02();
			calibrationModbusV02.setIgnoreResponse(true);
			prdQueue.add(calibrationModbusV02);
		}
	}

	private void addDevices(ConcurrentLinkedQueue<Device> prdQueue,
			ReceiptCollector collector, List<ReceiptMeter> meters) {
		for (ReceiptMeter meter : meters) {
			//97协议表
			if (meter.getProtocolType().equals("DLT645_1997")) {
				prdQueue.add(new Meter_97(collector, meter));
			}
			//07协议表:[102]DLT645_2007/电能表
			if (meter.getProtocolType().equals("DLT645_2007_Energy")) {
				prdQueue.add(new Meter_07_Energy(collector, meter));
			}
			//07协议表:[103]DLT645_2007/无变比无线电压
			if (meter.getProtocolType().equals("DLT645_2007_NoRate")) {
				prdQueue.add(new Meter_07_NoRate(collector, meter));
			}
			//07协议表:[101]DLT645_2007/新宏博全功能
			if (meter.getProtocolType().equals("DLT645_2007_NHB")) {
				prdQueue.add(new Meter_07_NHB(collector, meter));
			}
			//07协议表:[120]DLT645_2007/云尚电力
			if (meter.getProtocolType().equals("DTL645_2007_YUNSHANG")) {
				prdQueue.add(new Meter_07_YUNSHANG(collector, meter));
			}
			//07协议表:[113]DLT645_2007/电能表（正泰）
			if (meter.getProtocolType().equals("DTL645_2007_ZHENGTAI")) {
				prdQueue.add(new Meter_07_ZHENGTAI(collector, meter));
			}
			//指明modbus
			if (meter.getProtocolType().equals("ZHIMING_MODBUS")) {
				prdQueue.add(new MeterZhimingModbus(collector, meter));
			}
			//v02最老单路三相表
			if (meter.getProtocolType().equals("NHB_M1V02_0")) {
				prdQueue.add(new Meter_Mobile1(collector, meter));
			}
			//添加线电压后的单路三相表
			if (meter.getProtocolType().equals("NHB_M1V02_1")) {
				prdQueue.add(new Meter_Mobile2(collector, meter));
			}
			//V02单路三相表做三个单相表使用
			if (meter.getProtocolType().equals("NHB_M1V02_2")) {
				prdQueue.add(new Meter_MobileThreeSinglePhase(collector, meter));
			}
			//V02单路三相表添加分相电能
			if (meter.getProtocolType().equals("NHB_M1V02_3")) {
				prdQueue.add(new Meter_MobilePhaseEnergy(collector, meter));
			}
			// 02协议四路表 四路单相,
			if (meter.getProtocolType().equals("NHB_M4V02_0")) {
				prdQueue.add(new Meter4_02e(collector, meter));
			}
			// 02协议四路表 前一路三相，后三路单相
			if (meter.getProtocolType().equals("NHB_M4V02_1")) {
				prdQueue.add(new Meter4_02d(collector, meter));
			}
			// 02协议四路表 前一路三相并增加线电压和油机电能，后三路单相
			if (meter.getProtocolType().equals("NHB_M4V02_1_1")) {
				prdQueue.add(new Meter4_02d_1(collector, meter));
			}
			// 02协议四路表 前两路三相，后两路单相
			if (meter.getProtocolType().equals("NHB_M4V02_2")) {
				prdQueue.add(new Meter4_02c(collector, meter));
			}
			// 02协议四路表 前两路三相并增加线电压和油机电能，后两路单相
			if (meter.getProtocolType().equals("NHB_M4V02_2_1")) {
				prdQueue.add(new Meter4_02c_1(collector, meter));
			}
			// 02协议四路表 前三路三相，后一路单相
			if (meter.getProtocolType().equals("NHB_M4V02_3")) {
				prdQueue.add(new Meter4_02b(collector, meter));
			}
			// 02协议四路表 前三路三相并增加线电压和油机电能，后一路单相
			if (meter.getProtocolType().equals("NHB_M4V02_3_1")) {
				prdQueue.add(new Meter4_02b_1(collector, meter));
			}
			// 02协议四路表 四路三相,
			if (meter.getProtocolType().equals("NHB_M4V02_4")) {
				prdQueue.add(new Meter4_02a(collector, meter));
			}
			// 02协议四路表 增加线电压的四路三相,
			if (meter.getProtocolType().equals("NHB_M4V02_4_1")) {
				prdQueue.add(new Meter4_02a_1(collector, meter));
			}
			//力创谐波表
			if (meter.getProtocolType().equals("LCH_HARMONIC")) {
				prdQueue.add(new MeterLichuangHarmonic(collector, meter));
			}
			//新宏博谐波表
			if (meter.getProtocolType().equals("NHB_HARMONIC")) {
				prdQueue.add(new MeterXinhongboHarmonic(collector, meter));
			}
			// 直流表
			if (meter.getProtocolType().equals("DC")) {
				prdQueue.add(new MeterDC(collector, meter));
			}
			// 温度传感器（力创）
			if (meter.getProtocolType().equals("TEMP")) {
				prdQueue.add(new Temperature(collector, meter));
			}
			// 温度传感器（XHB）
			if (meter.getProtocolType().equals("TEMP_NHB")) {
				prdQueue.add(new TemperatureXHB(collector, meter));
			}
			// 继电器控制模块（力创）
			if (meter.getProtocolType().equals("RELAY")) {
				prdQueue.add(new Relay(collector, meter));
			}
			// 继电器控制模块（XHB）
			if (meter.getProtocolType().equals("RELAY_NHB")) {
				prdQueue.add(new RelayXHB(collector, meter));
			}
			//开关
			if (meter.getProtocolType().equals("RECLOSER")) {
				prdQueue.add(new Reclose(collector, meter));
			}
			//新宏博质量分析仪
			if (meter.getProtocolType().equals("NHB_HARMONIC_PQA")) {
				prdQueue.add(new MeterXinhongboHarmonicPQA(collector, meter));
			}
			//单项单路表（数据按照最老的单路三相表的协议存数在A相）
			if (meter.getProtocolType().equals("NHB_METER_ONEWAY")) {
				prdQueue.add(new Meter_OneWayA(collector, meter));
			}
			//迅鹏SPA-96BDE直流电能表
			if (meter.getProtocolType().equals("XP_SPA_96BDE")) {
				prdQueue.add(new Meter_XP_96BDE(collector, meter));
			}
			//力创EX8-33-V
			if (meter.getProtocolType().equals("LCH_EX8_33_V")) {
				prdQueue.add(new Meter_LCH_EX8_33_V(collector, meter));
			}
			//安科瑞PZ80E4电表
			if (meter.getProtocolType().equals("AKR_PZ80E4")) {
				prdQueue.add(new Meter_AKR_PZ80E4(collector, meter));
			}
			//安科瑞DTSD1352电表
			if (meter.getProtocolType().equals("AKR_DTSD1352")) {
				prdQueue.add(new Meter_AKR_DTSD1352(collector, meter));
			}
			//东歌电气 X/S系列
			if (meter.getProtocolType().equals("AKR_DTSD1352")) {
				prdQueue.add(new Meter_DG_XS(collector, meter));
			}
			//宁波水表
			if (meter.getProtocolType().equals("NB_WATER")) {
				prdQueue.add(new Meter_NB_WATER(collector, meter));
			}
			//CJ188水表
			if (meter.getProtocolType().equals("QIANBAOTONG_WATER")) {
				prdQueue.add(new CJ188_WATER(collector, meter));
			}
			//吴江印染厂使用的气表
			if (meter.getProtocolType().equals("WJYRC_GAS")) {
				prdQueue.add(new Meter_WJYRC_GAS(collector, meter));
			}
			//吴江印染厂使用的水表
			if (meter.getProtocolType().equals("WJYRC_WATER")) {
				prdQueue.add(new Meter_WJYRC_WATER(collector, meter));
			}
			//常州济中使用的斯菲尔电表
			if (meter.getProtocolType().equals("CHZJZ_SFER_200")) {
				prdQueue.add(new Meter_CHZJZ_SFER_200(collector, meter));
			}
			//苏教科使用的江阴中瑞电表ZRY4Z-9HY
			if (meter.getProtocolType().equals("CHZJZ_ZRY4Z_9HY")) {
				prdQueue.add(new Meter_CHZJZ_ZRY4Z_9HY(collector, meter));
			}
			//常州酒店使用的PZ810电表
			if (meter.getProtocolType().equals("CHZJZ_PD810")) {
				prdQueue.add(new Meter_CHZJZ_PZ810(collector, meter));
			}
			//安徽电信使用的力创六路直流表
			if (meter.getProtocolType().equals("LCH_SIX_DC")) {
				prdQueue.add(new Meter_LCH_SIX_DC(collector, meter));
			}
			//八路直流表
			if (meter.getProtocolType().equals("EIGHT_DC")) {
				prdQueue.add(new Meter_EIGHT_DC(collector, meter));
			}
			//模块化直流表
			if (meter.getProtocolType().equals("MODEL_DC")) {
				prdQueue.add(new Meter_MODEL_DC(collector, meter));
			}
			//新宏博油机市电表
			if (meter.getProtocolType().equals("OIL_MARKET")) {
				prdQueue.add(new Meter_Oil_Market(collector, meter));
			}
			//给领步使用的有源滤波器
			if (meter.getProtocolType().equals("LINGBU_FILTER")) {
				prdQueue.add(new ElectricFilter(collector, meter));
			}
			//浙江海信电子表
			if (meter.getProtocolType().equals("HAIXIN_ELEC")) {
				prdQueue.add(new Meter_Hisense_Electric(collector, meter));
			}
			//江苏斯菲尔PD194E-9S4
			if (meter.getProtocolType().equals("SEFER_PD194E_9S4")) {
				prdQueue.add(new Meter_Sefer_PD194E_9S4(collector, meter));
			}
			//江苏斯菲尔PD194E-9HY、3江苏斯菲尔PD194Z-9JW  统称斯菲儿谐波表
			if (meter.getProtocolType().equals("SEFER_HARMONIC")) {
				prdQueue.add(new Meter_Sefer_PD194_Harmonic(collector, meter));
			}
			//云上鸿引表（电表结构和浙江海信电子的寄存器地址一致）
			if (meter.getProtocolType().equals("YUNSHANG_HONGYIN")) {
				prdQueue.add(new Meter_Yunshang_HongYin(collector, meter));
			}
			//云上深圳电子PMC-43X简易谐波表
			if (meter.getProtocolType().equals("YUNSHANG_PMC43X")) {
				prdQueue.add(new Meter_Yunshang_ShenZhen_PMC43X(collector, meter));
			}
			//modbus通用协议
			if (meter.getProtocolType().equals("MODBUS_GENERIC")) {
				try {
					logger.info(meter.getGenericProIdentifier());
					if (modbusGenericProMap.containsKey(meter.getGenericProIdentifier())) {
						Class<?> clazz = modbusGenericProMap.get(meter.getGenericProIdentifier());
						Object obj = clazz.newInstance();
						AbstractDevice aDevice = (AbstractDevice) obj;
						aDevice.setReceiptCollector(collector);
						aDevice.setReceiptMeter(meter);
						aDevice.buildWritingFrames();
						Device device = (Device) aDevice;
						prdQueue.add(device);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.info("MODBUS_GENERIC error!",ex);
				}
			}
		}
	}

	private void handleCmdQueue(DtuContext dtuContext) {
		Device device = dtuContext.getCurrentDevice();
		if (device != null && !device.isComplete()) {
			return;
		}
		ConcurrentLinkedQueue<Device> cmdQueue = dtuContext.getCmdQueue();
		if (cmdQueue.isEmpty() && dtuContext.getCmdCHC() != null) {
			ChannelHandlerContextUtils.writeAndFlushJson(dtuContext.getCmdCHC(), CommandResponse.SUCCESS);
			dtuContext.setCmdCHC(null);
		}
	}

	/**
	 * write one frame every time
	 * 
	 * @param dtuContext
	 */
	private void writeDevice(DtuContext dtuContext) {
		Device device = nextDevice(dtuContext);
		if (device == null || device.isComplete()) {
			return;
		}
		byte[] frame = device.nextWritingFrame();
		if (frame == null) {
			return;
		}
		logger.info("send msg to " + dtuContext.getDtuNo());
		ChannelHandlerContextUtils.writeAndFlush(dtuContext.getDtuCHC(), frame);
		if (device.isIgnoreResponse()) {
			device.processReadingFrame(new byte[] {});
		}
	}

	private Device nextDevice(DtuContext dtuContext) {
		Device device = dtuContext.getCurrentDevice();
		if (device != null && !device.isComplete()) {
			return device;
		}
		if (!dtuContext.getCmdQueue().isEmpty()) {
			device = dtuContext.getCmdQueue().poll();
		} else if (!dtuContext.getPrdQueue().isEmpty()) {
			device = dtuContext.getPrdQueue().poll();
		} else {
			device = null;
		}
		dtuContext.setCurrentDevice(device);
		return device;
	}

}
