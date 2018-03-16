package com.xhb.sockserv.keep;

import java.util.Enumeration;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xhb.sockserv.config.ApplicationContext;
import com.xhb.sockserv.dtu.DtuBridge;
import com.xhb.sockserv.dtu.DtuPlusContext;
import com.xhb.sockserv.util.ChannelHandlerContextUtils;

import io.netty.channel.ChannelHandlerContext;

public class KeepDtuPlusTimeTask extends TimerTask {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private long readHistoryPerid = 900000L;
	@Override
	public void run() {
		try {
			Enumeration<DtuPlusContext> elements = KeepDtuPlusContextMap.getInstance().elements();
			while (elements.hasMoreElements()) {
				DtuPlusContext dtuContext = elements.nextElement();
				ChannelHandlerContext dtuCHC = dtuContext.getDtuChannel();
				if (dtuCHC == null || !dtuCHC.channel().isActive() || DtuBridge.isBridging(dtuCHC)) {
					continue;
				}
				
				if(dtuContext.getRetry() > 4){
					logger.error("重试三次，关闭连接");
					dtuCHC.channel().disconnect();
					dtuCHC.close();
				}else{
					long period = System.currentTimeMillis() - dtuContext.getPacketLastTime();
					long heartPeriod = System.currentTimeMillis() - dtuContext.getHeartlastTime();
					readHistoryPerid = getDtuPlusPeriod(dtuContext.getDtuNo());
					if(dtuContext.isRight() == false){
						dtuContext.setRetry(dtuContext.getRetry() + 1);
					}
					if (period + 20 >= readHistoryPerid || dtuContext.getMeterPlus().isCollectorDataAgain()) {
						byte[] buffer = dtuContext.getMeterPlus().getPacketToWrite();
						if (buffer == null) {
							return;
						}
						//logger.warn("send msg to " + dtuContext.getDtuNo() + ":"+buffer);
						ChannelHandlerContextUtils.writeAndFlush(dtuCHC, buffer);
						dtuContext.setRight(false);
						if (buffer[2] == 0x51) {
							dtuContext.setPacketLastTime(System.currentTimeMillis());
							dtuContext.getMeterPlus().setCollectorDataAgain(false);
						}
						dtuContext.setHeartlastTime(System.currentTimeMillis());
					} else if (heartPeriod + 20 >= ApplicationContext.getConfig().getFrameHeartWritingPeriod()) {
						ChannelHandlerContextUtils.writeAndFlush(dtuCHC,
								new byte[] { 0x55, (byte) 0xAA, 0x21, 0x00, 0x00 });
						dtuContext.setHeartlastTime(System.currentTimeMillis());
						dtuContext.setRight(false);
					}
					
					
				}
				
				
			}
		} catch (Exception e) {
			logger.error("unexpected exception", e);
		}
	}
	private long getDtuPlusPeriod(String dtuNo) {
		return ApplicationContext.getConfig().getFrameDtuPlusWritingPeriod();
	}

}
