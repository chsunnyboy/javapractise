package com.jmsTest;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class DataTransProcessorCollectorImpl implements DataTransProcessorCollector, BeanPostProcessor {
	List<DataTransProcessor> processors = new ArrayList();

	public List<DataTransProcessor> collect(TransData transdata) {
		List<DataTransProcessor> col = new ArrayList();
		this.processors.forEach((p) -> {
			if (p.support(transdata)) {
				col.add(p);
			}

		});
		return col;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof DataTransProcessor) {
			DataTransProcessor processor = (DataTransProcessor) bean;
			this.processors.add(processor);
		}

		return bean;
	}
}