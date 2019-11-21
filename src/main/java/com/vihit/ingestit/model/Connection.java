package com.vihit.ingestit.model;

import java.util.Map;

public class Connection {

	public String name;
	public Map<String,String> props;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getProps() {
		return props;
	}
	public void setProps(Map<String, String> props) {
		this.props = props;
	}
	@Override
	public String toString() {
		return "Connection [name=" + name + ", props=" + props + "]";
	}
	
}
