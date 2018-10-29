package com.jmsTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataTransReceiver {
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	DataTransProcessorCollector collector;
	@Autowired
	JmsTemplate jmsTemplate;
	@Autowired
	ObjectMapper objectMapper;
	
	@JmsListener(destination = "inca.saas.set._cset_")
	public void process(String content) {
		try {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("receive: {}", content);
			}

			TransData td = (TransData) this.objectMapper.readValue(content, TransData.class);
			this.process(td);
		} catch (Throwable var3) {
			this.logger.error("error trans data", var3);
		}

	}

	public void process(TransData content) {
		String setid = content.getSetId();
	}
}