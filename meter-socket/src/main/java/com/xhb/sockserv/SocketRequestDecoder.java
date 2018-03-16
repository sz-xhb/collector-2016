package com.xhb.sockserv;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SocketRequestDecoder extends ByteToMessageDecoder {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() <= 0) {
			return;
		}

		if (in.readableBytes() >= 18 && in.getByte(in.readerIndex()) == (byte) 0x68
				&& in.getByte(in.readerIndex() + 1) == (byte) 0x68 && in.getByte(in.readerIndex() + 2) == (byte) 0x16
				&& in.getByte(in.readerIndex() + 3) == (byte) 0x16
		// && in.getByte(in.writerIndex() - 4) == (byte) 0x55
		// && in.getByte(in.writerIndex() - 3) == (byte) 0xAA
		// && in.getByte(in.writerIndex() - 2) == (byte) 0x55
		// && in.getByte(in.writerIndex() - 1) == (byte) 0xAA
		) {
			// logger.info("" + (in.getByte(in.readerIndex() + 4) & 0xff));
			// int dataLength = (in.getByte(in.readerIndex() + 4) & 0xff) +
			// (in.getByte(in.readerIndex() + 5) & 0xff) * 256
			// + (in.getByte(in.readerIndex() + 6) & 0xff) * 256 * 256
			// + (in.getByte(in.readerIndex() + 7) & 0xff) * 256 * 256 * 256;

			// if (in.readableBytes() == dataLength + 18) {
			// byteBuf = in.readBytes(in.readableBytes());
			// }else if (in.readableBytes() > dataLength + 18){
			// byteBuf = in.readBytes(dataLength + 18);
			// }else {
			// return;
			// }
			int readindex = in.readerIndex();
			while (readindex + 3 < in.writerIndex()) {
				if (in.getByte(readindex) == (byte) 0x55 && in.getByte(readindex + 1) == (byte) 0xAA
						&& in.getByte(readindex + 2) == (byte) 0x55 && in.getByte(readindex + 3) == (byte) 0xAA) {
					break;
				}
				readindex++;
			}
			if (readindex + 3 >= in.writerIndex()) {
				return;
			}
			ByteBuf byteBuf = in.readBytes(readindex + 4 - in.readerIndex());
			byte[] bytes = byteBuf.array();
			out.add(bytes);
			return;
		}

		if (in.readableBytes() >= 1 && in.getByte(in.readerIndex()) == (byte) 0x68
				&& in.getByte(in.writerIndex() - 1) != (byte) 0x16) {
			return;
		}
		 if (in.readableBytes() >= 3
		 && in.getByte(in.readerIndex()) == (byte) 0xAA
		 && in.getByte(in.readerIndex() + 1) == (byte) 0x18
		 && in.getByte(in.readerIndex() + 2) == (byte) 0x05
		 && in.getByte(in.writerIndex() - 1) != (byte) 0x55) {
		 return;
		 }
		 if (in.readableBytes() >= 3
		 && in.getByte(in.readerIndex()) == (byte) 0xAA
		 && in.getByte(in.readerIndex() + 1) == (byte) 0x55
		 && in.getByte(in.readerIndex() + 2) == (byte) 0xAA
		 && in.getByte(in.readerIndex() + 3) == (byte) 0x55
		 && (in.getByte(in.writerIndex() - 4) != (byte) 0x68
		 || in.getByte(in.writerIndex() - 3) != (byte) 0x68
		 || in.getByte(in.writerIndex() - 2) != (byte) 0x16
		 || in.getByte(in.writerIndex() - 1) != (byte) 0x16)) {
		 return;
		 }
		if (in.readableBytes() >= 5 && in.getByte(in.readerIndex()) == (byte) 0x55
				&& in.getByte(in.readerIndex() + 1) == (byte) 0xAA) {
			int packetLength = (in.getByte(in.readerIndex() + 3) & 0xff) * 256
					+ (in.getByte(in.readerIndex() + 4) & 0xff);
			if (packetLength + 5 > in.readableBytes()) {
				return;
			}
			ByteBuf byteBuf = in.readBytes(packetLength + 5);
			byte[] bytes = byteBuf.array();
			out.add(bytes);
			return;

		}
		 ByteBuf byteBuf = in.readBytes(in.readableBytes());
		 byte[] bytes = byteBuf.array();
		 out.add(bytes);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("unexpected exception", cause);
		ctx.close();
	}

}
