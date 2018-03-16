package com.xhb.sockserv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public abstract class ChannelHandlerContextUtils {

	private static Logger logger = LoggerFactory.getLogger(ChannelHandlerContextUtils.class);

	public static ChannelFuture writeAndFlush(ChannelHandlerContext ctx, byte[] msg) {
		//logger.info("{}", FrameUtils.toString(msg));
		logger.warn("send msg to 采集器:" + FrameUtils.toString(msg));
		ByteBuf byteBuf = ctx.alloc().buffer();
		byteBuf.writeBytes(msg);
		return ctx.writeAndFlush(byteBuf);
	}

	public static ChannelFuture writeAndFlushJson(ChannelHandlerContext ctx, Object value) {
		String s = JsonUtils.writeValueAsString(value);
		logger.info(s);
		return writeAndFlush(ctx, s.getBytes());
	}

}
