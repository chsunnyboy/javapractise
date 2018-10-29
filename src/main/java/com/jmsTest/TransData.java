package com.jmsTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TransData {
	String id;
	String setId;
	Set<String> customerIdList = new HashSet();
	String desc;
	String intent;
	Map<String, String> properties = new HashMap();
	Object data;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSetId() {
		return this.setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public Set<String> getCustomerIdList() {
		return this.customerIdList;
	}

	public void setCustomerIdList(Set<String> customerIdList) {
		this.customerIdList = customerIdList;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIntent() {
		return this.intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public Map<String, String> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setProperty(String key, String value) {
		this.properties.put(key, value);
	}

	public String getProperty(String key) {
		return (String) this.properties.get(key);
	}

	public String toJson() throws Exception {
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(this);
		return json;
	}

	public void addCustomerId(String id) {
		this.customerIdList.add(id);
	}
}
