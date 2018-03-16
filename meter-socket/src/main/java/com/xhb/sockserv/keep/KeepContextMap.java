package com.xhb.sockserv.keep;

import java.util.concurrent.ConcurrentHashMap;

import com.xhb.sockserv.dtu.DtuContext;


public abstract class KeepContextMap {

	private static ConcurrentHashMap<String, DtuContext> instance = new ConcurrentHashMap<String, DtuContext>();

	public static ConcurrentHashMap<String, DtuContext> getInstance() {
		return instance;
	}

}
