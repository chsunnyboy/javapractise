package com.jmsTest;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.stereotype.Component;

@Component
public class DataTransDestinationResolver extends DynamicDestinationResolver {
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	public static final String SET_PLACEHOLDER = "_cset_";
	//inca.saas.set.jms.SET0
	public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain)
			throws JMSException {
		if (destinationName.indexOf("_cset_") != -1) {
			String old = new String(destinationName);
			destinationName = destinationName.replaceFirst("_cset_", "jms.SET0");
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("convert dest:{} to:{}", old, destinationName);
			}
		}

		return super.resolveDestinationName(session, destinationName, pubSubDomain);
	}
}
