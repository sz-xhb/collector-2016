package com.xhb.sockserv;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Timer;

import com.xhb.sockserv.config.ApplicationContext;
import com.xhb.sockserv.keep.KeepDtuPlusTimeTask;
import com.xhb.sockserv.keep.KeepWritingTimerTask;

public class SocketServer {

	public static void main(String[] args) throws Exception {
		new SocketServer().run();
	}

	public void run() throws Exception {
		long frameWritingPeriod = ApplicationContext.getConfig().getFrameWritingPeriod();
		int port = ApplicationContext.getConfig().getServerPort();
		ApplicationContext.makeModbusGenericProMap();
		
		Timer timer = new Timer();
		timer.schedule(new KeepDtuPlusTimeTask(), 30000L, frameWritingPeriod);
		
		Timer timer2 = new Timer();
		timer2.schedule(new KeepWritingTimerTask(), 30000L, frameWritingPeriod);
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup(50);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new SocketServerInitializer());
			b.bind(port).sync().channel().closeFuture().sync();
		} finally {
			System.out.println("shutdown gracefully!");
			timer.cancel();timer2.cancel();
			timer.purge();timer2.purge();
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
