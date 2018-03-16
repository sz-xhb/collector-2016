package com.xhb.sockserv.meter;

public interface Device {

	void processReadingFrame(byte[] readingFrame);

	byte[] nextWritingFrame();

	boolean isComplete();

	boolean isIgnoreResponse();

}
