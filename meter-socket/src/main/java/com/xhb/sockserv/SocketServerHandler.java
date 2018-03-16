package com.xhb.sockserv;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TimerTask;
import java.util.Timer;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xhb.core.entity.BuildInfo;
import com.xhb.core.entity.CollectorStatus;
import com.xhb.core.entity.ReceiptCircuit;
import com.xhb.core.entity.ReceiptCollector;
import com.xhb.core.entity.ReceiptMeter;
import com.xhb.core.service.Services;
import com.xhb.sockserv.command.CommandRequest;
import com.xhb.sockserv.command.CommandResponse;
import com.xhb.sockserv.config.ApplicationConfig;
import com.xhb.sockserv.config.ApplicationContext;
import com.xhb.sockserv.dtu.DtuBridge;
import com.xhb.sockserv.dtu.DtuContext;
import com.xhb.sockserv.dtu.DtuPlusContext;
import com.xhb.sockserv.dtu.EnergyCollectorContext;
import com.xhb.sockserv.keep.KeepContextMap;
import com.xhb.sockserv.keep.KeepEnergyCollectorContextMap;
import com.xhb.sockserv.keep.KeepDtuPlusContextMap;
import com.xhb.sockserv.meter.Device;
import com.xhb.sockserv.meter.MeterPlus;
import com.xhb.sockserv.meter.MeterXinhongboDsm;
import com.xhb.sockserv.meter.Reclose;
import com.xhb.sockserv.meter.RecloseControlOff;
import com.xhb.sockserv.meter.RecloseControlOn;
import com.xhb.sockserv.meter.RecloseOff;
import com.xhb.sockserv.meter.RecloseOn;
import com.xhb.sockserv.meter.RecloseResetOff;
import com.xhb.sockserv.meter.RecloseResetOn;
import com.xhb.sockserv.meter.RecloseStartOff;
import com.xhb.sockserv.meter.RecloseStartOn;
import com.xhb.sockserv.meter.Relay;
import com.xhb.sockserv.meter.RelayOff;
import com.xhb.sockserv.meter.RelayOn;
import com.xhb.sockserv.meter.RelayXHB;
import com.xhb.sockserv.meter.RelayXHBOff;
import com.xhb.sockserv.meter.RelayXHBOn;
import com.xhb.sockserv.util.AES_CBC_NoPadding;
import com.xhb.sockserv.util.CRC16;
import com.xhb.sockserv.util.ChannelHandlerContextUtils;
import com.xhb.sockserv.util.EnergyCollectorInteractiveStatus;
import com.xhb.sockserv.util.FrameUtils;
import com.xhb.sockserv.util.JsonUtils;
import com.xhb.sockserv.util.MD5;
import com.xhb.sockserv.util.RandomString;
import com.xhb.sockserv.util.XmlMsgTemplate;

public class SocketServerHandler extends SimpleChannelInboundHandler<byte[]> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ApplicationConfig config = ApplicationContext.getConfig();

	private Services services = ApplicationContext.getServices();

	private class TimerTask_ChannelActive_21 extends TimerTask {
		private ChannelHandlerContext ctx;

		public TimerTask_ChannelActive_21(ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}

		@Override
		public void run() {
			ChannelHandlerContextUtils.writeAndFlush(ctx, new byte[] { 0x55, (byte) 0xAA, 0x21, 0x00, 0x00 });
		}
	}
	
	private class TimerTask_ChannelActive_23 extends TimerTask {
		private ChannelHandlerContext ctx;

		public TimerTask_ChannelActive_23(ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}

		@Override
		public void run() {
			ChannelHandlerContextUtils.writeAndFlush(ctx, new byte[] { 0x55, (byte) 0xAA, 0x23, 0x00, 0x00 });
		}
	}
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Timer timer = new Timer();
		timer.schedule(new TimerTask_ChannelActive_21(ctx), 10000);// 采集器加连接以后，延时10秒发送心跳
		timer.schedule(new TimerTask_ChannelActive_23(ctx), 30000);
	};

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		logger.warn("channelRead0：" + FrameUtils.toString(msg));
		if (msg.length == 0) {
			return;
		}
		String s = new String(msg).trim();
		if (s.startsWith("{") && s.endsWith("}")) {
			command(ctx, s);
		} else if ((msg[0] == 0x55 && msg[2] != (byte) 0x7A && DtuBridge.isBridging(ctx))
				|| (msg[0] == (byte) 0xAA && msg[1] == (byte) 0x18) || (msg[0] == (byte) 0x4A && msg[1] == (byte) 0x59)
				|| (msg[0] == (byte) 0xAA && msg[1] == (byte) 0x07)) {
			DtuBridge.writeAndFlush(ctx, msg);
		} else if (msg[0] == (byte) 0x55 && msg[1] == (byte) 0xAA && msg[2] == (byte) 0x21) {
			DtuPlusRegisterHandler(ctx, msg);
		} else if (msg[0] == (byte) 0x55 && msg[1] == (byte) 0xAA && msg[2] == (byte) 0x23) {
			handleDtuPlusConfigInfo(ctx, msg);
		} else if (msg[0] == (byte) 0x55 && msg[1] == (byte) 0xAA && msg[2] == (byte) 0x25) {
			handleDtuPlusInterfaceInfo(ctx, msg);
		} else if (msg[0] == (byte) 0x55 && msg[1] == (byte) 0xAA && msg[2] == (byte) 0x26) {
			handleDtuPlusDeviceConfigInfo(ctx, msg);
		} else if (msg[0] == (byte) 0x55 && msg[1] == (byte) 0xAA && msg[2] == (byte) 0x51) {
			handleDtuPlusDataInfo(ctx, msg);
		} else if (msg[0] == (byte) 0x55 && msg[1] == (byte) 0xAA && msg[2] == (byte) 0x6A) {
			resetBridge(ctx, msg);
		} else if (msg[0] == (byte) 0x55 && msg[1] == (byte) 0xAA && msg[2] == (byte) 0x7A) {
			transferHeChuanDtu(ctx, msg);
		} else if (msg[0] == (byte) 0x55 && msg[1] == (byte) 0xAA && msg[2] == (byte) 0x8A) {
			resetBridgeOfMeterPlus(ctx, msg);
		} else if ((msg[0] == (byte) 0x68) && (msg[1] == (byte) 0x01) && (msg[msg.length - 1] == (byte) 0x16)) {
			register(ctx, msg);
		} else if ((msg[0] == (byte) 0x68) && (msg[1] == (byte) 0x02) && (msg[msg.length - 1] == (byte) 0x16)) {
			heartbeat(ctx, msg);
		} else if ((msg[0] == (byte) 0x68) && (msg[1] == (byte) 0x03) && (msg[msg.length - 1] == (byte) 0x16)
				&& (msg[8] != (byte) 0x6a)) {
			readDevice(ctx, msg);
		} else if ((msg[0] == (byte) 0x68) && (msg[1] == (byte) 0xAA) && (msg[msg.length - 1] == (byte) 0x16)) {
			resetBridge(ctx, msg);
		} else if ((msg[0] == (byte) 0x68) && (msg[1] == (byte) 0x68) && (msg[2] == (byte) 0x16)
				&& (msg[3] == (byte) 0x16) && (msg[msg.length - 4] == (byte) 0x55)
				&& (msg[msg.length - 3] == (byte) 0xAA) && (msg[msg.length - 2] == (byte) 0x55)
				&& (msg[msg.length - 1] == (byte) 0xAA)) {
			energyCollectorHandler(ctx, msg);
			return;
		} else 
		{
			logger.info("所有条件不满足" + FrameUtils.toString(msg));
		}
	}

	// 能耗采集器数据的处理
	private void energyCollectorHandler(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		if (msg.length < 18) {
			logger.info("the length of msg is too short!");
			return;
		}
		int dataLength = (msg[7] & 0xff) * 256 * 256 * 256 + (msg[6] & 0xff) * 256 * 256 + (msg[5] & 0xff) * 256
				+ (msg[4] & 0xff);
		if (dataLength + 14 != msg.length) {
			logger.info("the received msg's length is error!");
			return;
		}
		byte[] crcBuf = CRC16.do_crc(msg, msg.length - 6);
		if (crcBuf[0] != msg[msg.length - 6] || crcBuf[1] != msg[msg.length - 5]) {
			logger.info("the received msg's CRC valid is not passed!");
			return;
		}
		byte[] dataBuffer = new byte[dataLength - 4];
		for (int i = 0; i < dataBuffer.length; i++) {
			dataBuffer[i] = msg[i + 12];
		}
		byte[] decryptedBytes = AES_CBC_NoPadding.decodeAES(dataBuffer);
		String xmlData = new String(decryptedBytes, "utf-8");
		logger.info(xmlData);
		String regex = "<type>(.*?)</type>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(xmlData);
		if (matcher.find()) {
			String xmlType = matcher.group(0).split("<")[1].split(">")[1];
			if (xmlType.equals("request")) {
				handleEnergyCollectorRequest(ctx, xmlData);
			} else if (xmlType.equals("md5")) {
				handleEnergyCollectorMd5Valid(ctx, xmlData);
			} else if (xmlType.equals("notify")) {
				handleEnergyCollectorNotify(ctx, xmlData);
			} else if (xmlType.equals("report")) {
				handleEnergyCollectorReportData(ctx, xmlData);
			}
		} else {
			logger.info("not find the xml type!");
			return;
		}
	}

	// 处理能耗采集器的数据上报信息
	private void handleEnergyCollectorReportData(ChannelHandlerContext ctx, String xmlData) throws Exception {
		String xmlResponse = null;
		String buildId = FrameUtils.getNodeInfo(xmlData, "building_id");
		String collectorNo = FrameUtils.getNodeInfo(xmlData, "gateway_id");
		if (buildId == null || collectorNo == null) {
			logger.info("not find building_id or gateway_id  in the request xml !");
			// xmlResponse =
			// XmlMsgTemplate.HANDLERESULTOFREPORTDATAXML.replaceFirst("楼栋编号",
			// buildId)
			// .replaceFirst("采集器编号", collectorNo).replaceFirst("返回值", "1006");
			// byte[] msg = makeXmlResponseMsg(xmlResponse);
			// ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
			// logger.info(xmlResponse);
			return;
		}
		if (!KeepEnergyCollectorContextMap.getInstance().containsKey(ctx) || KeepEnergyCollectorContextMap.getInstance()
				.get(ctx).getStatus().equals(EnergyCollectorInteractiveStatus.AUTHENTICATION)) {
			xmlResponse = XmlMsgTemplate.HANDLERESULTOFREPORTDATAXML.replaceFirst("楼栋编号", buildId)
					.replaceFirst("采集器编号", collectorNo).replaceFirst("返回值", "1001");
			byte[] msg = makeXmlResponseMsg(xmlResponse);
			ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
			logger.info(xmlResponse);
			return;
		}
		if (KeepEnergyCollectorContextMap.getInstance().get(ctx).getStatus()
				.equals(EnergyCollectorInteractiveStatus.CONFIG)) {
			xmlResponse = XmlMsgTemplate.READCONFIGAGESXML;
			byte[] msg = makeXmlResponseMsg(xmlResponse);
			ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
			logger.info(xmlResponse);
			return;
		}
		BuildInfo buildInfo = services.buildInfoService.get(buildId);
		if (buildInfo == null) {
			xmlResponse = XmlMsgTemplate.HANDLERESULTOFREPORTDATAXML.replaceFirst("楼栋编号", buildId)
					.replaceFirst("采集器编号", collectorNo).replaceFirst("返回值", "1006");
			logger.info("the buildId not exist in system dataBase!");
			byte[] msg = makeXmlResponseMsg(xmlResponse);
			ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
			logger.info(xmlResponse);
			Thread.sleep(1000);
			xmlResponse = XmlMsgTemplate.READBUILDINFOXML;
			msg = makeXmlResponseMsg(xmlResponse);
			ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
			logger.info(xmlResponse);
			return;
		}
		ReceiptCollector rc = services.receiptCollectorService.getByBuildIdAndCollectorNo(buildId, collectorNo);
		if (rc == null) {
			xmlResponse = XmlMsgTemplate.HANDLERESULTOFREPORTDATAXML.replaceFirst("楼栋编号", buildId)
					.replaceFirst("采集器编号", collectorNo).replaceFirst("返回值", "1002");
			logger.info("the buildId not exist in system dataBase!");
			byte[] msg = makeXmlResponseMsg(xmlResponse);
			ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
			logger.info(xmlResponse);
			Thread.sleep(1000);
			xmlResponse = XmlMsgTemplate.READCOLLECTORINFOXML;
			msg = makeXmlResponseMsg(xmlResponse);
			ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
			logger.info(xmlResponse);
			return;
		}
		SAXBuilder builder = new SAXBuilder();
		StringReader reader = new StringReader(xmlData);
		Document document = builder.build(reader);// 获得文档对象
		Element root = document.getRootElement();// 获得根节点
		@SuppressWarnings("unchecked")
		List<Element> meterChildren = root.getChild("data").getChildren("meter");
		for (Element element : meterChildren) {
			String monitorId = element.getAttributeValue("id");
			ReceiptCircuit receiptCircuit = services.receiptCircuitService.getByBuildIdAndCollectorNo(buildId,
					collectorNo, monitorId);
			if (receiptCircuit == null) {
				xmlResponse = XmlMsgTemplate.HANDLERESULTOFREPORTDATAXML.replaceFirst("楼栋编号", buildId)
						.replaceFirst("采集器编号", collectorNo).replaceFirst("返回值", "1004");
				logger.info("the buildId not exist in system dataBase!");
				byte[] msg = makeXmlResponseMsg(xmlResponse);
				ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
				logger.info(xmlResponse);
				Thread.sleep(1000);
				xmlResponse = XmlMsgTemplate.READCOLLECTORINFOXML;
				msg = makeXmlResponseMsg(xmlResponse);
				ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
				logger.info(xmlResponse);
				continue;
			}
		}

	}

	// 处理能耗采集器的心跳报文
	private void handleEnergyCollectorNotify(ChannelHandlerContext ctx, String xmlData) throws Exception {
		String buildId = FrameUtils.getNodeInfo(xmlData, "building_id");
		if (buildId == null) {
			logger.info("not find building_id in the request xml !");
			return;
		}
		String collectorNo = FrameUtils.getNodeInfo(xmlData, "gateway_id");
		if (collectorNo == null) {
			logger.info("not find gateway_id in the request xml !");
			return;
		}
		BuildInfo buildInfo = services.buildInfoService.get(buildId);
		ReceiptCollector rc = services.receiptCollectorService.getByBuildIdAndCollectorNo(buildId, collectorNo);
		if (buildInfo == null || rc == null) {
			logger.info("the collector has not read build or collector config info!");
			return;
		}
		if (!KeepEnergyCollectorContextMap.getInstance().containsKey(ctx)
				|| KeepEnergyCollectorContextMap.getInstance().get(ctx).getStatus()
						.equals(EnergyCollectorInteractiveStatus.AUTHENTICATION)
				|| !buildId.equals(KeepEnergyCollectorContextMap.getInstance().get(ctx).getBuildId())
				|| !collectorNo.equals(KeepEnergyCollectorContextMap.getInstance().get(ctx).getCollectorNo())) {
			logger.info("the collector has not passed authentication!");
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
		String dateTimeStr = sdf.format(new Date());
		String xmlResponse = XmlMsgTemplate.NOTIFYXML.replaceFirst("楼栋编号", buildId).replaceFirst("采集器编号", collectorNo)
				.replaceFirst("服务器时间", dateTimeStr);
		byte[] msg = makeXmlResponseMsg(xmlResponse);
		ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
		logger.info(xmlResponse);
		updateEnergyCollectorStatus(rc, ctx);
	}

	// 更新能耗采集器的状态信息
	private void updateEnergyCollectorStatus(ReceiptCollector rc, ChannelHandlerContext ctx) {
		CollectorStatus collectorStatus = services.collectorStatusService.get(rc.getId());
		if (collectorStatus == null) {
			collectorStatus = new CollectorStatus();
			collectorStatus.setCollectorId(rc.getId());
			InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
			collectorStatus.setCollectorIp(address.getAddress().getHostAddress());
			collectorStatus.setCollectorPort(address.getPort());
			try {
				collectorStatus.setServerIp(InetAddress.getLocalHost().getHostAddress());
				collectorStatus.setServerPort(config.getServerPort());
			} catch (Exception e) {
				logger.error("unexpected exception", e);
			}
			collectorStatus.setActiveTime(new Date());
			services.collectorStatusService.save(collectorStatus);
		} else {
			collectorStatus.setActiveTime(new Date());
			services.collectorStatusService.update(collectorStatus);
		}
	}

	// 处理能耗采集器的Md5值的验证
	private void handleEnergyCollectorMd5Valid(ChannelHandlerContext ctx, String xmlData) throws Exception {
		if (!KeepEnergyCollectorContextMap.getInstance().containsKey(ctx)
				|| KeepEnergyCollectorContextMap.getInstance().get(ctx).getSequence() == null) {
			logger.info("not find the request xml brefore md5 xml!");
			return;
		}
		String buildId = FrameUtils.getNodeInfo(xmlData, "building_id");
		if (buildId == null) {
			logger.info("not find building_id in the request xml !");
			return;
		}
		String collectorNo = FrameUtils.getNodeInfo(xmlData, "gateway_id");
		if (collectorNo == null) {
			logger.info("not find gateway_id in the request xml !");
			return;
		}
		String md5Str = FrameUtils.getNodeInfo(xmlData, "md5");
		if (md5Str == null) {
			logger.info("not find md5 string in the request xml !");
			return;
		}
		String md5Local = MD5.encrypt(KeepEnergyCollectorContextMap.getInstance().get(ctx).getSequence()
				+ config.getEnergyCollectorMd5AttachedStr());
		String result = "pass";
		if (!md5Str.equals(md5Local)) {
			result = "fail";
		}
		String xmlResponse = XmlMsgTemplate.MD5VALIDRESPONSEXML.replaceFirst("楼栋编号", buildId)
				.replaceFirst("采集器编号", collectorNo).replaceFirst("验证结果", result);
		EnergyCollectorContext ecc = KeepEnergyCollectorContextMap.getInstance().get(ctx);
		if (KeepEnergyCollectorContextMap.getInstance().containsKey(ctx)) {
			// if
			// (!ecc.getStatus().equals(EnergyCollectorInteractiveStatus.AUTHENTICATION)
			// && collectorNo.equals(ecc.getCollectorNo()) &&
			// buildId.equals(ecc.getBuildId())) {
			// logger.info("energy collector has passed the authentication !
			// don't need to authenticate again!");
			// return;
			// }
		}
		byte[] msg = makeXmlResponseMsg(xmlResponse);
		ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
		logger.info(xmlResponse);
		if (result.equals("pass")) {
			ecc.setStatus(EnergyCollectorInteractiveStatus.CONFIG);
		}
		KeepEnergyCollectorContextMap.getInstance().put(ctx, ecc);
	}

	// 处理能耗采集器的身份认证信息
	private void handleEnergyCollectorRequest(ChannelHandlerContext ctx, String xmlData) throws Exception {
		String buildId = FrameUtils.getNodeInfo(xmlData, "building_id");
		if (buildId == null) {
			logger.info("not find building_id in the request xml !");
			return;
		}
		String collectorNo = FrameUtils.getNodeInfo(xmlData, "gateway_id");
		if (collectorNo == null) {
			logger.info("not find gateway_id in the request xml !");
			return;
		}
		String sequence = RandomString.create(16);
		String xmlResponse = XmlMsgTemplate.SQUENCEXML.replaceFirst("楼栋编号", buildId).replaceFirst("采集器编号", collectorNo)
				.replaceFirst("16位序列随机序列", sequence);
		EnergyCollectorContext ecc = null;
		if (KeepEnergyCollectorContextMap.getInstance().containsKey(ctx)) {
			ecc = KeepEnergyCollectorContextMap.getInstance().get(ctx);
			// if
			// (!ecc.getStatus().equals(EnergyCollectorInteractiveStatus.AUTHENTICATION)
			// && collectorNo.equals(ecc.getCollectorNo()) &&
			// buildId.equals(ecc.getBuildId())) {
			// logger.info("energy collector has passed the authentication !
			// don't need to authenticate again!");
			// return;
			// }
		}
		byte[] msg = makeXmlResponseMsg(xmlResponse);
		ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
		logger.info(xmlResponse);
		if (ecc == null) {
			ecc = new EnergyCollectorContext();
		}
		ecc.setBuildId(buildId);
		ecc.setCollectorNo(collectorNo);
		ecc.setDtuCHC(ctx);
		ecc.setSequence(sequence);
		ecc.setStatus(EnergyCollectorInteractiveStatus.AUTHENTICATION);
		KeepEnergyCollectorContextMap.getInstance().put(ctx, ecc);
	}

	// 组织能号采集器的返回报文
	private byte[] makeXmlResponseMsg(String xmlResponse) throws Exception {
		byte[] encodeBytes = AES_CBC_NoPadding.codeAES(xmlResponse.getBytes("utf-8"));
		byte[] msg = new byte[encodeBytes.length + 18];
		msg[0] = 0x68;
		msg[1] = 0x68;
		msg[2] = 0x16;
		msg[3] = 0x16;
		msg[4] = (byte) ((encodeBytes.length + 4) % 256);
		msg[5] = (byte) (((encodeBytes.length + 4) / 256) % 256);
		msg[6] = (byte) (((encodeBytes.length + 4) / 256 / 256) % 256);
		msg[7] = (byte) (((encodeBytes.length + 4) / 256 / 256 / 256) % 256);
		msg[8] = 0x00;
		msg[9] = 0x00;
		msg[10] = 0x00;
		msg[11] = 0x00;
		System.arraycopy(encodeBytes, 0, msg, 12, encodeBytes.length);
		byte[] crcBuf = CRC16.do_crc(msg, encodeBytes.length + 12);
		msg[msg.length - 6] = crcBuf[0];
		msg[msg.length - 5] = crcBuf[1];
		msg[msg.length - 4] = (byte) 0x55;
		msg[msg.length - 3] = (byte) 0xAA;
		msg[msg.length - 2] = (byte) 0x55;
		msg[msg.length - 1] = (byte) 0xAA;
		return msg;
	}

	// 对采集器加进行搭桥，以进行采集器加的远程操控
	private void resetBridgeOfMeterPlus(ChannelHandlerContext ctx, byte[] msg) throws UnsupportedEncodingException {
		String collectorNo = FrameUtils.getMeterPlusAddr(msg);
		if (collectorNo == null || collectorNo.equals("")) {
			logger.info("collectorNO is null!");
			return;
		}
		DtuPlusContext dpc = KeepDtuPlusContextMap.getInstance().get(collectorNo);
		if (dpc == null || dpc.getDtuChannel() == null || !dpc.getDtuChannel().channel().isActive()) {
			ChannelHandlerContextUtils.writeAndFlush(ctx, new byte[] { (byte) 0x01 });
			return;
		}
		DtuBridge.setDtuCHC(dpc.getDtuChannel());
		DtuBridge.setReqCHC(ctx);
		ChannelHandlerContextUtils.writeAndFlush(ctx, new byte[] { (byte) 0x00 });
	}

	// 将远程操控的命令去掉报文头后转发给和传DTU
	private void transferHeChuanDtu(ChannelHandlerContext ctx, byte[] msg) {
		if (msg.length < 6) {
			return;
		}
		byte[] arr = new byte[msg.length - 5];
		for (int i = 5; i < msg.length; i++) {
			arr[i - 5] = msg[i];
		}
		DtuBridge.writeAndFlush(ctx, arr);
	}

	// 搭桥，用来进行DTU的远程操控
	private void resetBridge(ChannelHandlerContext ctx, byte[] msg) {
		String dtuNo = null;
		if ((msg[0] & 0xFF) != 0x55) {
			dtuNo = FrameUtils.getDtuNo(msg);
		} else {
			dtuNo = FrameUtils.getCollectorNo(msg);
		}
		if (dtuNo == null) {
			return;
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DtuContext dtuContext = KeepContextMap.getInstance().get(dtuNo);
		if (dtuContext == null || dtuContext.getDtuCHC() == null || !dtuContext.getDtuCHC().channel().isActive()) {
			ChannelHandlerContextUtils.writeAndFlush(ctx, new byte[] { (byte) 0x01 });
			return;
		}
		DtuBridge.setDtuCHC(dtuContext.getDtuCHC());
		DtuBridge.setReqCHC(ctx);
		ChannelHandlerContextUtils.writeAndFlush(ctx, new byte[] { (byte) 0x00 });
	}

	// 处理上传的数据报文
	private void readDevice(ChannelHandlerContext ctx, byte[] msg) {
		String dtuNo = FrameUtils.getDtuNo(msg);
		if (dtuNo == null) {
			return;
		}
		updateDtuStatus(dtuNo, ctx);

		if (msg.length >= 10 && (msg[8] == (byte) 0x88) && (msg[9] == (byte) 0x05)) {
			MeterXinhongboDsm meterXinhongboDsm = new MeterXinhongboDsm();
			meterXinhongboDsm.processReadingFrame(msg);
			logger.info(dtuNo + ":" + msg);//DTU调试代码
			byte[] bytes = new byte[15];
			bytes[0] = msg[8];
			bytes[1] = msg[9];
			bytes[2] = (byte) 0x00;
			bytes[3] = (byte) 0x01;
			System.arraycopy(msg, 14, bytes, 4, 7);
			if (meterXinhongboDsm.isComplete()) {
				bytes[11] = (byte) 0x01;
			} else {
				bytes[11] = (byte) 0x00;
			}
			int sum = 0;
			for (int i = 1; i < bytes.length - 3; i++) {
				sum = sum + (bytes[i] & 0xFF);
			}
			bytes[12] = (byte) (sum % 256);
			bytes[13] = (byte) (sum / 256);
			bytes[14] = (byte) 0x16;
			ChannelHandlerContextUtils.writeAndFlush(ctx, bytes);
			return;
		}

		DtuContext dtuContext = KeepContextMap.getInstance().get(dtuNo);
		if (dtuContext == null) {
			return;
		}
		Device device = dtuContext.getCurrentDevice();
		if (device == null || device.isComplete()) {
			return;
		}
		device.processReadingFrame(msg);
	}

	// 处理dtu的心跳报文
	private void heartbeat(ChannelHandlerContext ctx, byte[] msg) {
		String dtuNo = FrameUtils.getDtuNo(msg);
		if (dtuNo == null) {
			return;
		}
		updateDtuStatus(dtuNo, ctx);

		ChannelHandlerContextUtils.writeAndFlush(ctx, new byte[] { (byte) 0x02 });
	}

	// 处理采集器（dtu）注册信息
	private void register(ChannelHandlerContext ctx, byte[] msg) {
		String dtuNo = FrameUtils.getDtuNo(msg);
		if (dtuNo == null) {
			return;
		}
		updateDtuStatus(dtuNo, ctx);

		DtuContext dtuContext = new DtuContext();
		dtuContext.setDtuNo(dtuNo);
		dtuContext.setDtuCHC(ctx);
		dtuContext.setDelay(new Random().nextInt(10) + 1);
		KeepContextMap.getInstance().put(dtuNo, dtuContext);
		ChannelHandlerContextUtils.writeAndFlush(ctx, new byte[] { (byte) 0x01 });
	}

	// 更新采集器在线状态
	private void updateDtuStatus(String dtuNo, ChannelHandlerContext ctx) {
		ReceiptCollector collector = services.receiptCollectorService.getByDtuNo(dtuNo);
		if (collector == null) {
			return;
		}
		CollectorStatus collectorStatus = services.collectorStatusService.get(collector.getId());
		if (collectorStatus == null) {
			collectorStatus = new CollectorStatus();
			collectorStatus.setCollectorId(collector.getId());
			InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
			collectorStatus.setCollectorIp(address.getAddress().getHostAddress());
			collectorStatus.setCollectorPort(address.getPort());
			try {
				collectorStatus.setServerIp(InetAddress.getLocalHost().getHostAddress());
				collectorStatus.setServerPort(config.getServerPort());
			} catch (Exception e) {
				logger.error("unexpected exception", e);
			}
			collectorStatus.setActiveTime(new Date());
			services.collectorStatusService.save(collectorStatus);
		} else {
			collectorStatus.setActiveTime(new Date());
			services.collectorStatusService.update(collectorStatus);
		}
	}

	// 处理远程控制所发的命令
	private void command(ChannelHandlerContext ctx, String json) {
		logger.info(json);
		CommandRequest request = JsonUtils.readValue(json, CommandRequest.class);
		if (request == null || request.getType() == null || request.getCircuitId() == null) {
			ChannelHandlerContextUtils.writeAndFlushJson(ctx, CommandResponse.ERR1001);
			return;
		}
		ReceiptCircuit circuit = services.receiptCircuitService.get(request.getCircuitId());
		if (circuit == null) {
			ChannelHandlerContextUtils.writeAndFlushJson(ctx, CommandResponse.ERR1004);
			return;
		}
		ReceiptMeter meter = circuit.getReceiptMeter();
		ReceiptCollector collector = meter.getReceiptCollector();
		DtuContext dtuContext = KeepContextMap.getInstance().get(collector.getCollectorNo());
		if (dtuContext == null || dtuContext.getDtuCHC() == null || !dtuContext.getDtuCHC().channel().isActive()) {
			ChannelHandlerContextUtils.writeAndFlushJson(ctx, CommandResponse.ERR1002);
			return;
		}
		if (dtuContext.getCmdCHC() != null) {
			ChannelHandlerContextUtils.writeAndFlushJson(ctx, CommandResponse.ERR1003);
			return;
		}
		ConcurrentLinkedQueue<Device> cmdQueue = dtuContext.getCmdQueue();
		if (meter.getProtocolType().equals("RELAY")) {
			if ("SWITCH_ON".equals(request.getType())) {
				cmdQueue.add(new RelayOn(meter, circuit));
				cmdQueue.add(new Relay(collector, meter));
			}
			if ("SWITCH_OFF".equals(request.getType())) {
				cmdQueue.add(new RelayOff(meter, circuit));
				cmdQueue.add(new Relay(collector, meter));
			}
		}
		if (meter.getProtocolType().equals("RELAY_NHB")) {
			if ("SWITCH_ON".equals(request.getType())) {
				cmdQueue.add(new RelayXHBOn(meter, circuit));
				cmdQueue.add(new RelayXHB(collector, meter));
			}
			if ("SWITCH_OFF".equals(request.getType())) {
				cmdQueue.add(new RelayXHBOff(meter, circuit));
				cmdQueue.add(new RelayXHB(collector, meter));
			}
			if ("RESET_SWITCH_ON".equals(request.getType())) {
				cmdQueue.add(new RecloseResetOn(meter));
				cmdQueue.add(new RelayXHB(collector, meter));
			}
			if ("RESET_SWITCH_OFF".equals(request.getType())) {
				cmdQueue.add(new RecloseResetOff(meter));
				cmdQueue.add(new RelayXHB(collector, meter));
			}
			if ("START_SWITCH_ON".equals(request.getType())) {
				cmdQueue.add(new RecloseStartOn(meter));
				cmdQueue.add(new RelayXHB(collector, meter));
			}
			if ("START_SWITCH_OFF".equals(request.getType())) {
				cmdQueue.add(new RecloseStartOff(meter));
				cmdQueue.add(new RelayXHB(collector, meter));
			}
			if ("CONTROL_SWITCH_ON".equals(request.getType())) {
				cmdQueue.add(new RecloseControlOn(meter));
				cmdQueue.add(new RelayXHB(collector, meter));
			}
			if ("CONTROL_SWITCH_OFF".equals(request.getType())) {
				cmdQueue.add(new RecloseControlOff(meter));
				cmdQueue.add(new RelayXHB(collector, meter));
			}
		}
		if (meter.getProtocolType().equals("RECLOSER")) {
			if ("SWITCH_ON".equals(request.getType())) {
				cmdQueue.add(new RecloseOn(meter));
				cmdQueue.add(new Reclose(collector, meter));
			}
			if ("SWITCH_OFF".equals(request.getType())) {
				cmdQueue.add(new RecloseOff(meter));
				cmdQueue.add(new Reclose(collector, meter));
			}
			if ("RESET_SWITCH_ON".equals(request.getType())) {
				cmdQueue.add(new RecloseResetOn(meter));
				cmdQueue.add(new Reclose(collector, meter));
			}
			if ("RESET_SWITCH_OFF".equals(request.getType())) {
				cmdQueue.add(new RecloseResetOff(meter));
				cmdQueue.add(new Reclose(collector, meter));
			}
			if ("START_SWITCH_ON".equals(request.getType())) {
				cmdQueue.add(new RecloseStartOn(meter));
				cmdQueue.add(new Reclose(collector, meter));
			}
			if ("START_SWITCH_OFF".equals(request.getType())) {
				cmdQueue.add(new RecloseStartOff(meter));
				cmdQueue.add(new Reclose(collector, meter));
			}
			if ("CONTROL_SWITCH_ON".equals(request.getType())) {
				cmdQueue.add(new RecloseControlOn(meter));
				cmdQueue.add(new Reclose(collector, meter));
			}
			if ("CONTROL_SWITCH_OFF".equals(request.getType())) {
				cmdQueue.add(new RecloseControlOff(meter));
				cmdQueue.add(new Reclose(collector, meter));
			}
		}
		dtuContext.setCmdCHC(ctx);
	}

	// 处理采集器加的数据信息
	private void handleDtuPlusDataInfo(ChannelHandlerContext ctx, byte[] msg) {
		DtuPlusContext dtuPlusContext = getMeterPlus(ctx);
		if (dtuPlusContext == null) {
			return;
		}
		// logger.info("read " + dtuPlusContext.getDtuNo() + " data here!");
		MeterPlus meterPlus = dtuPlusContext.getMeterPlus();
		if (meterPlus == null) {
			return;
		}
		meterPlus.handleDtuDataInfo(msg, dtuPlusContext.getDtuNo());
		updateDtuStatus(dtuPlusContext.getDtuNo(), ctx);
		dtuPlusContext.setRight(true);
	}

	// 处理采集器加的设备配置信息
	private void handleDtuPlusDeviceConfigInfo(ChannelHandlerContext ctx, byte[] msg) {
		DtuPlusContext dtuPlusContext = getMeterPlus(ctx);
		if (dtuPlusContext == null) {
			return;
		}
		// logger.info("read " + dtuPlusContext.getDtuNo() + " device config
		// here!");
		MeterPlus meterPlus = dtuPlusContext.getMeterPlus();
		if (meterPlus == null) {
			return;
		}
		meterPlus.handleDtuDeviceConfigInfo(msg, dtuPlusContext.getDtuNo());
		updateDtuStatus(dtuPlusContext.getDtuNo(), ctx);
		dtuPlusContext.setRight(true);
	}

	// 处理采集器加的接口信息
	private void handleDtuPlusInterfaceInfo(ChannelHandlerContext ctx, byte[] msg) {
		DtuPlusContext dtuPlusContext = getMeterPlus(ctx);
		if (dtuPlusContext == null) {
			return;
		}
		// logger.info("read " + dtuPlusContext.getDtuNo() + " interface
		// here!");
		MeterPlus meterPlus = dtuPlusContext.getMeterPlus();
		if (meterPlus == null) {
			return;
		}
		meterPlus.handDtuInterfaceInfo(msg, dtuPlusContext.getDtuNo());
		updateDtuStatus(dtuPlusContext.getDtuNo(), ctx);
		dtuPlusContext.setRight(true);
	}

	// 处理采集器加配置信息
	private void handleDtuPlusConfigInfo(ChannelHandlerContext ctx, byte[] msg) {
		DtuPlusContext dtuPlusContext = getMeterPlus(ctx);
		if (dtuPlusContext == null) {
			return;
		}
		MeterPlus meterPlus = dtuPlusContext.getMeterPlus();
		if (meterPlus == null) {
			return;
		}
		meterPlus.handleDtuConfigInfo(msg, dtuPlusContext.getDtuNo());
		updateDtuStatus(dtuPlusContext.getDtuNo(), ctx);
		dtuPlusContext.setRight(true);
		// logger.info("read " + dtuPlusContext.getDtuNo() + " config here!");
	}

	// 根据数据通道获取采集器加的设备上下文信息
	private DtuPlusContext getMeterPlus(ChannelHandlerContext ctx) {
		Enumeration<DtuPlusContext> elements = KeepDtuPlusContextMap.getInstance().elements();
		while (elements.hasMoreElements()) {
			DtuPlusContext dtuContext = elements.nextElement();
			if (ctx.equals(dtuContext.getDtuChannel())) {
				return dtuContext;
			}
		}
		return null;
	}

	// 采集器加的解密方法
	public void decrypt(byte[] buf, int token) {
		int org;

		for (int i = 0; i < buf.length; i++) {
			org = buf[i];
			buf[i] = (byte) (0xff - (org ^ token));
			token = org;
		}
	}

	// 采集器加注册的处理方法
	private void DtuPlusRegisterHandler(ChannelHandlerContext ctx, byte[] msg) {
		try {
			byte[] data = new byte[msg.length - 5];
			System.arraycopy(msg, 5, data, 0, data.length);
			decrypt(data, 0x21);
			String xmlData = new String(data, "utf-8");
			// logger.info(xmlData);
			//logger.warn("heartbeat:" + xmlData);
			SAXBuilder builder = new SAXBuilder();
			StringReader reader = new StringReader(xmlData);
			Document document = builder.build(reader);// 获得文档对象
			Element root = document.getRootElement();// 获得根节点
			@SuppressWarnings("unchecked")
			List<Element> nodes = root.getChildren();
			if (!nodes.get(0).getName().equals("c_addr")) {
				return;
			}
			String dtuNo = nodes.get(0).getText();
			logger.warn(dtuNo + " : heartbeat received");
			DtuPlusContext dpc = new DtuPlusContext();
			dpc.setDtuNo(dtuNo);
			dpc.setDtuChannel(ctx);
			// dpc.setPacketLastTime(System.currentTimeMillis());
			dpc.setHeartlastTime(System.currentTimeMillis());
			MeterPlus meterPlus = new MeterPlus();
			String age = nodes.get(1).getText();
			meterPlus.setDtuAges(age);
			ReceiptCollector rc = services.receiptCollectorService.getByDtuNo(dtuNo);
			if (rc == null || !rc.getAges().toString().equals(age)) {
				meterPlus.setFuncCode(0x23);
			} else {
				meterPlus.setFuncCode(0x51);
			}
			dpc.setMeterPlus(meterPlus);
			KeepDtuPlusContextMap.getInstance().put(dtuNo, dpc);
			updateDtuStatus(dtuNo, ctx);
			dpc.setRight(true);
		} catch (Exception ex) {
			logger.error("dtu plus resister error", ex);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("unexpected exception", cause);
	}
}
