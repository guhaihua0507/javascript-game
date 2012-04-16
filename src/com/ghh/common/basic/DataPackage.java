package com.ghh.common.basic;

public class DataPackage {
	private StringBuffer data = new StringBuffer();

	public void addValue(String name, String d) {
		if (data.length() > 0) {
			data.append(",");
		}
		data.append(name + ":'" + d + "'");
	}

	public void addObject(String name, String json) {
		if (data.length() > 0) {
			data.append(",");
		}
		data.append(name + ":" + json);
	}
	
	public String getDataString() {
		return "{" + data.toString() + "}";
	}
}
