package com.vihit.ingestit.model;

import java.util.Map;
import java.io.Serializable;

public class Pipeline implements Serializable {

	public Map<String, String> in;
	public Map<String, String> out;

	public Map<String, String> getIn() {
		return in;
	}

	public void setIn(Map<String, String> in) {
		this.in = in;
	}

	public Map<String, String> getOut() {
		return out;
	}

	public void setOut(Map<String, String> out) {
		this.out = out;
	}

	@Override
	public String toString() {
		return "Pipeline [in=" + in + ", out=" + out + "]";
	}
}
