package com.xhb.sockserv;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel socketChannel) {
		ChannelPipeline pipeline = socketChannel.pipeline();
		pipeline.addLast(SocketRequestDecoder.class.getName(), new SocketRequestDecoder());
		pipeline.addLast(SocketServerHandler.class.getName(), new SocketServerHandler());
	}

}
