package com.xhb.sockserv.keep;

import java.util.concurrent.ConcurrentHashMap;

import com.xhb.sockserv.dtu.EnergyCollectorContext;

import io.netty.channel.ChannelHandlerContext;

public abstract class KeepEnergyCollectorContextMap {
	
	private static ConcurrentHashMap<ChannelHandlerContext, EnergyCollectorContext> instance = new ConcurrentHashMap<ChannelHandlerContext, EnergyCollectorContext>();
	
	public static ConcurrentHashMap<ChannelHandlerContext, EnergyCollectorContext> getInstance() {
		return instance;
	}
}
