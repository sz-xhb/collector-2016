package com.xhb.sockserv.dtu;

import com.xhb.sockserv.util.ChannelHandlerContextUtils;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public abstract class DtuBridge {

	private static ChannelHandlerContext dtuCHC;

	private static ChannelHandlerContext reqCHC;

	public static ChannelHandlerContext getDtuCHC() {
		return dtuCHC;
	}

	public static void setDtuCHC(ChannelHandlerContext dtuCHC) {
		DtuBridge.dtuCHC = dtuCHC;
	}

	public static ChannelHandlerContext getReqCHC() {
		return reqCHC;
	}

	public static void setReqCHC(ChannelHandlerContext reqCHC) {
		DtuBridge.reqCHC = reqCHC;
	}

	public static ChannelFuture writeAndFlush(ChannelHandlerContext ctx, byte[] msg) {
		if (ctx == dtuCHC && reqCHC != null && reqCHC.channel().isActive()) {
			return ChannelHandlerContextUtils.writeAndFlush(reqCHC, msg);
		}
		if (ctx == reqCHC && dtuCHC != null && dtuCHC.channel().isActive()) {
			return ChannelHandlerContextUtils.writeAndFlush(dtuCHC, msg);
		}
		return null;
	}

	public static boolean isBridging(ChannelHandlerContext ctx) {
		return (ctx == dtuCHC && reqCHC != null && reqCHC.channel().isActive())
				|| (ctx == reqCHC && dtuCHC != null && dtuCHC.channel().isActive());
	}

}
