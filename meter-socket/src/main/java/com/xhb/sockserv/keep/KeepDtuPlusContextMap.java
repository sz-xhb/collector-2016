package com.xhb.sockserv.keep;

import java.util.concurrent.ConcurrentHashMap;

import com.xhb.sockserv.dtu.DtuPlusContext;

public abstract class KeepDtuPlusContextMap {

	private static ConcurrentHashMap<String,DtuPlusContext> instance = new ConcurrentHashMap<String, DtuPlusContext>();

	public static ConcurrentHashMap<String, DtuPlusContext> getInstance() {
		return instance;
	}
}
