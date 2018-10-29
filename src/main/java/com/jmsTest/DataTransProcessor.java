package com.jmsTest;

import java.util.Map;

public interface DataTransProcessor {
	boolean support(TransData var1);

	void process(Object var1, Map<String, String> var2) throws Exception;
}
