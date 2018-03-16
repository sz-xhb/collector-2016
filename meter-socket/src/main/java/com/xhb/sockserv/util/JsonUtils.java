package com.xhb.sockserv.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JsonUtils {

	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	private static ObjectMapper mapper = new ObjectMapper();

	public static <T> T readValue(String content, Class<T> valueType) {
		try {
			return mapper.readValue(content, valueType);
		} catch (Exception e) {
			logger.error("unexpected exception", e);
		}
		return null;
	}

	public static String writeValueAsString(Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			logger.error("unexpected exception", e);
		}
		return null;
	}

}
