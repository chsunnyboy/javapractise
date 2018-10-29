package com.jmsTest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(JmsTest.FUNC_PATH)
public class JmsTest {
	public static final String FUNC_PATH = "/api/testJms";
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@RequestMapping("/testJms")
	@ResponseBody
	public String testJms() {
		String dest ="inca.saas.set.jms";
		jmsTemplate.send(dest, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TransData td = new TransData();
				td.setData("sync data completed");
				td.setIntent("isDemoCus");
				td.setSetId("SET0");
				try {
					return session.createTextMessage(td.toJson());
				} catch (Exception e) {
					e.printStackTrace();
					throw new JMSException(e.getMessage());
				}
			}
		});
		return "success";
	}
}
